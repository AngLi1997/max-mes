package com.bmos.lims2.server.inspect.division.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.lims2.server.inspect.division.dto.SampleDivisionRemainDTO;
import com.bmos.lims2.server.inspect.division.service.SampleDivisionService;
import com.bmos.lims2.server.inspect.sample.dto.SampleDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.inspect.division.dto.SampleDivisionDTO;
import com.bmos.lims2.server.platform.system.code.PlatformCodeFeignClient;
import com.bmos.lims2.server.platform.system.code.dto.BatchNextCodeVO;
import com.bmos.unit.service.UnitCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import com.bmos.lims2.server.inspect.common.util.QuantityUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 分样服务实现类
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
@Service
@Slf4j
public class SampleDivisionServiceImpl implements SampleDivisionService {

    /**
     * 样品编号规则代码
     */
    private static final String SAMPLE_CODE_RULE = "SAMPLE_NO";

    @Autowired
    private SampleMapper sampleMapper;


    @Autowired
    private UnitCache unitCache;

    @Autowired
    private PlatformCodeFeignClient platformCodeFeignClient;

    @Autowired
    private com.bmos.lims2.server.inspect.sample.ledger.service.SampleLedgerService sampleLedgerService;

    @Override
    public SampleDivisionRemainDTO calculateRemaining(Long originalSampleId, List<SampleDivisionDTO.DivisionResultDTO> divisionResults) {
        if (originalSampleId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "原样品ID不能为空");
        }

        // 查询原样品信息
        Sample originalSample = sampleMapper.selectById(originalSampleId);
        if (originalSample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "原样品不存在");
        }
        if (!originalSample.getSampled()) {
            throw new BmosException(LimsResponseCode.DIVIDE_ERROR_SAMPLE_NOT_SAMPLED, originalSample.getSampleNo());
        }

        // 样品状态不正确，不能进行分样
        if (!originalSample.getReceived()) {
            throw new BmosException(LimsResponseCode.DIVIDE_ERROR_SAMPLE_NOT_RECEIVED, originalSample.getSampleNo());
        }

        SampleDivisionRemainDTO remainDTO = new SampleDivisionRemainDTO();
        BigDecimal originalQuantityBD = QuantityUtils.toBigDecimal(originalSample.getQuantity());
        remainDTO.setOriginalQuantity(originalQuantityBD);
        remainDTO.setOriginalUnitId(originalSample.getUnitId());
        remainDTO.setOriginalUnitName(unitCache.getGlobalUnitName(originalSample.getUnitId()));

        // 计算已分样总量（需要转换单位），分样总量=分样量×样品份数
        BigDecimal totalDividedQuantity = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(divisionResults)) {
            for (SampleDivisionDTO.DivisionResultDTO result : divisionResults) {
                if (result.getQuantity() != null && result.getUnitId() != null) {
                    Integer sampleCount = result.getSampleCount() == null ? 1 : result.getSampleCount();
                    BigDecimal perPortionQuantity = QuantityUtils.toBigDecimal(result.getQuantity());
                    BigDecimal totalForEntry = perPortionQuantity.multiply(new BigDecimal(sampleCount));
                    // 将分样量转换为原样品的单位
                    BigDecimal convertedQuantity = unitCache.convert(
                            totalForEntry,
                            result.getUnitId(),
                            originalSample.getUnitId()
                    );
                    totalDividedQuantity = totalDividedQuantity.add(convertedQuantity);
                }
            }
        }

        remainDTO.setDividedQuantity(totalDividedQuantity);
        remainDTO.setRemainingQuantity(originalQuantityBD.subtract(totalDividedQuantity));

        return remainDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SampleDTO> saveSampleDivision(SampleDivisionDTO divisionDTO) {
        if (divisionDTO == null || divisionDTO.getOriginalSampleId() == null) {
            throw new BmosException(LimsResponseCode.DIVIDED_ERROR_DATA_EMPTY);
        }

        if (CollectionUtils.isEmpty(divisionDTO.getDivisionResults())) {
            throw new BmosException(LimsResponseCode.DIVIDE_ERROR_RESULT_EMPTY);
        }

        log.info("开始保存分样结果，原样品ID：{}", divisionDTO.getOriginalSampleId());

        // 查询原样品
        Sample originalSample = sampleMapper.selectById(divisionDTO.getOriginalSampleId());
        if (originalSample == null) {
            throw new BmosException(LimsResponseCode.INVALID_SAMPLE_NOT_EXITS);
        }


        if (!originalSample.getSampled()) {
            throw new BmosException(LimsResponseCode.DIVIDE_ERROR_SAMPLE_NOT_SAMPLED, originalSample.getSampleNo());
        }

        // 样品状态不正确，不能进行分样
        if (!originalSample.getReceived()) {
            throw new BmosException(LimsResponseCode.DIVIDE_ERROR_SAMPLE_NOT_RECEIVED, originalSample.getSampleNo());
        }


        if (originalSample.getDiscarded()) {
            throw new BmosException(LimsResponseCode.DIVIDE_ERROR_SAMPLE_ALREADY_DISCARD, originalSample.getSampleNo());
        }

//        if (originalSample.getDivided()) {
//            throw new BmosException(LimsResponseCode.DIVIDE_ERROR_SAMPLE_ALREADY_DIVIDED, originalSample.getSampleNo());
//
//        }


        // 校验分样总量不超过原样品量，并计算总分样量（按原样品单位）
        BigDecimal originalQuantity = QuantityUtils.toBigDecimal(originalSample.getQuantity());
        Long originalUnitId = originalSample.getUnitId();

        BigDecimal totalDivisionQuantity = BigDecimal.ZERO;
        for (SampleDivisionDTO.DivisionResultDTO result : divisionDTO.getDivisionResults()) {
            BigDecimal perPortion = QuantityUtils.toBigDecimal(result.getQuantity());
            if (result.getQuantity() == null || perPortion.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BmosException(LimsResponseCode.DIVIDE_ERROR_RESULT_QUANTITY_ERROR);
            }
            if (result.getUnitId() == null) {
                throw new BmosException(LimsResponseCode.DIVIDE_ERROR_RESULT_UNIT_ERROR);
            }
            int sampleCount = result.getSampleCount() == null ? 1 : result.getSampleCount();
            BigDecimal totalForEntry = perPortion.multiply(new BigDecimal(sampleCount));
            BigDecimal convertedQuantity = unitCache.convert(totalForEntry, result.getUnitId(), originalUnitId);
            totalDivisionQuantity = totalDivisionQuantity.add(convertedQuantity);
        }

        if (totalDivisionQuantity.compareTo(originalQuantity) > 0) {
            throw new BmosException(
                    LimsResponseCode.DIVIDE_ERROR_SAMPLE_DIVISION_QUANTITY_EXCEED,
                    originalSample.getSampleNo(),
                    totalDivisionQuantity.toString() + unitCache.getGlobalUnitName(originalUnitId),
                    originalQuantity.toString() + unitCache.getGlobalUnitName(originalUnitId)
            );
        }

        // 生成子样品编号（总份数=所有条目的样品份数之和），如存在正余量则为余量再申请一个编号
        BigDecimal remainingQuantity = originalQuantity.subtract(totalDivisionQuantity);
        boolean hasRemaining = remainingQuantity.compareTo(BigDecimal.ZERO) > 0;
        int divisionCount = divisionDTO.getDivisionResults().stream()
                .map(r -> r.getSampleCount() == null ? 1 : r.getSampleCount())
                .reduce(0, Integer::sum) + (hasRemaining ? 1 : 0);
        BatchNextCodeVO batchNextCodeVO = platformCodeFeignClient.getSampleNextUseNoBatch(SAMPLE_CODE_RULE, divisionCount);

        List<SampleDTO> childSamples = new ArrayList<>();
        try {
            // 创建子样品：每份生成一个样品
            int codeIndex = 0;
            for (SampleDivisionDTO.DivisionResultDTO result : divisionDTO.getDivisionResults()) {
                int count = result.getSampleCount() == null ? 1 : result.getSampleCount();
                for (int j = 0; j < count; j++) {
                    Sample childSample = new Sample();
                    childSample.setInspectionOrderId(originalSample.getInspectionOrderId());
                    childSample.setSampleNo(batchNextCodeVO.getNos().get(codeIndex).getNo());
                    childSample.setSampleName("分样-" + batchNextCodeVO.getNos().get(codeIndex).getNo());
                    childSample.setParentSampleId(originalSample.getId());
                    childSample.setParentSampleNo(originalSample.getSampleNo());
                    childSample.setInspectItemId(result.getInspectItemId());
                    // 每一份的数量等于分样量（per portion）
                    childSample.setQuantity(result.getQuantity());
                    childSample.setCurrentQuantity(result.getQuantity());  // 初始化当前数量等于数量
                    childSample.setUnitId(result.getUnitId());
                    childSample.setRemark(result.getRemark());

                    // 继承原样品的状态（已取样、已接收），但未分样、未领取、未作废
                    childSample.setSampled(originalSample.getSampled());
                    childSample.setReceived(originalSample.getReceived());
                    childSample.setDividerName(SysUserHolder.getUser().getUserName());
                    childSample.setDivideTime(LocalDateTime.now());
                    childSample.setDivided(true);
                    childSample.setCollected(false);
                    childSample.setDiscarded(false);

                    // 继承原样品的取样和接收信息
                    childSample.setSamplerId(originalSample.getSamplerId());
                    childSample.setSamplerName(originalSample.getSamplerName());
                    childSample.setSamplingTime(originalSample.getSamplingTime());
                    childSample.setReceiverId(originalSample.getReceiverId());
                    childSample.setReceiverName(originalSample.getReceiverName());
                    childSample.setReceiveTime(originalSample.getReceiveTime());

                    // 继承留样相关字段（如果原样品是留样样品）
                    childSample.setRetentionExpiryDate(originalSample.getRetentionExpiryDate());
                    childSample.setStorageLocation(originalSample.getStorageLocation());
                    childSample.setDestroyed(false);  // 子样品初始化为未销毁

                    sampleMapper.insert(childSample);
                    childSamples.add(BeanUtil.copyProperties(childSample, SampleDTO.class));
                    log.info("创建子样品成功，ID：{}，编号：{}，父样品ID：{}",
                            childSample.getId(), childSample.getSampleNo(), originalSample.getId());
                    // 台账：子样分样
                    try {
                        sampleLedgerService.recordEvent(
                                originalSample.getInspectionOrderId(),
                                childSample.getId(),
                                com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum.DIVIDED,
                                "分样生成");
                    } catch (Exception ex) {
                        log.warn("记录样品台账失败（子样生成），子样ID：{}，原因：{}", childSample.getId(), ex.getMessage());
                    }
                    codeIndex++;
                }
            }

            // 创建余量样品（如存在正余量）
            if (hasRemaining) {
                Sample remainingSample = new Sample();
                remainingSample.setInspectionOrderId(originalSample.getInspectionOrderId());
                remainingSample.setSampleNo(batchNextCodeVO.getNos().get(codeIndex).getNo());
                remainingSample.setSampleName("分样-余量-" + batchNextCodeVO.getNos().get(codeIndex).getNo());
                remainingSample.setParentSampleId(originalSample.getId());
                remainingSample.setParentSampleNo(originalSample.getSampleNo());
                remainingSample.setInspectItemId(originalSample.getInspectItemId());
                remainingSample.setQuantity(QuantityUtils.toCanonicalString(remainingQuantity));
                remainingSample.setCurrentQuantity(QuantityUtils.toCanonicalString(remainingQuantity));  // 初始化当前数量等于数量
                remainingSample.setUnitId(originalUnitId);
                remainingSample.setRemark("分样余量");

                remainingSample.setSampled(originalSample.getSampled());
                remainingSample.setReceived(originalSample.getReceived());
                remainingSample.setDividerName(SysUserHolder.getUser().getUserName());
                remainingSample.setDivideTime(LocalDateTime.now());
                remainingSample.setDivided(true);
                remainingSample.setCollected(false);
                remainingSample.setDiscarded(false);

                remainingSample.setSamplerId(originalSample.getSamplerId());
                remainingSample.setSamplerName(originalSample.getSamplerName());
                remainingSample.setSamplingTime(originalSample.getSamplingTime());
                remainingSample.setReceiverId(originalSample.getReceiverId());
                remainingSample.setReceiverName(originalSample.getReceiverName());
                remainingSample.setReceiveTime(originalSample.getReceiveTime());

                // 继承留样相关字段（如果原样品是留样样品）
                remainingSample.setRetentionExpiryDate(originalSample.getRetentionExpiryDate());
                remainingSample.setStorageLocation(originalSample.getStorageLocation());
                remainingSample.setDestroyed(false);  // 余量样品初始化为未销毁

                sampleMapper.insert(remainingSample);
                childSamples.add(BeanUtil.copyProperties(remainingSample, SampleDTO.class));
                log.info("创建余量样品成功，ID：{}，编号：{}，父样品ID：{}，余量：{}{}",
                        remainingSample.getId(), remainingSample.getSampleNo(), originalSample.getId(),
                        remainingQuantity.toString(), unitCache.getGlobalUnitName(originalUnitId));
                // 台账：余量子样分样
                try {
                    sampleLedgerService.recordEvent(
                            originalSample.getInspectionOrderId(),
                            remainingSample.getId(),
                            com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum.DIVIDED,
                            "分样生成-余量");
                } catch (Exception ex) {
                    log.warn("记录样品台账失败（余量子样生成），样品ID：{}，原因：{}", remainingSample.getId(), ex.getMessage());
                }
            }

            // 标记原样品为已分样和作废
            originalSample.setDivided(true);
            originalSample.setDividerName(SysUserHolder.getUser().getUserName());
            originalSample.setDivideTime(LocalDateTime.now());
            originalSample.setDiscarded(true);
            originalSample.setDiscardedBy(SysUserHolder.getUser().getUserName());
            originalSample.setDiscardedTime(LocalDateTime.now());
            originalSample.setDiscardedReason("分样操作");
            // 分样后原样品视为全部消耗：消耗量=样品数量
            originalSample.setConsumedQuantity(originalSample.getQuantity());
            // 分样后原样品的当前数量为0（全部分出）
            originalSample.setCurrentQuantity("0");

            sampleMapper.updateById(originalSample);
            // 台账：仅记录“原样作废”，并覆盖消耗量为样品数量，状态覆盖为已分样（不再生成数量为0的分样台账）
            try {
                sampleLedgerService.recordEventWithSnapshot(
                        originalSample.getInspectionOrderId(),
                        originalSample.getId(),
                        com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum.DISCARDED,
                        "0",
                        null,
                        originalSample.getQuantity(),
                        3,
                        "分样操作");
            } catch (Exception ex) {
                log.warn("记录样品台账失败（原样作废），样品ID：{}，原因：{}", originalSample.getId(), ex.getMessage());
            }

            // 确认样品编号已使用
            platformCodeFeignClient.confirmSampleNoBatch(batchNextCodeVO);

            log.info("分样操作完成，原样品ID：{}，生成子样品数量：{}", originalSample.getId(), divisionCount);
            return childSamples;
        } catch (Exception e) {
            log.error("分样操作失败，原样品ID：{}，错误：{}", originalSample.getId(), e.getMessage(), e);
            throw e;
        }
    }
}