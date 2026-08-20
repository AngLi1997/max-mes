package com.bmos.lims2.server.inspect.sample.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.constants.InspectItemConstants;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.item.entity.InspectItem;
import com.bmos.lims2.server.inspect.item.mapper.InspectItemMapper;
import com.bmos.lims2.server.inspect.order.converter.SampleConverter;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.entity.InspectionSampling;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.order.mapper.InspectionSamplingMapper;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.inspect.sample.dto.*;
import com.bmos.lims2.server.inspect.sample.service.SampleService;
import com.bmos.lims2.server.inspect.sample.dto.SampleScanResultDTO;
import com.bmos.lims2.server.task.service.TaskService;
import com.bmos.lims2.server.platform.system.code.PlatformCodeFeignClient;
import com.bmos.lims2.server.platform.system.code.dto.BatchNextCodeVO;
import com.bmos.lims2.server.platform.system.code.dto.NextCodeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.unit.service.UnitCache;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 样品管理Service实现类
 * @Author: yigaohui
 * @Date: 2025/01/29 10:30
 */
@Service
@Slf4j
public class SampleServiceImpl implements SampleService {

    /**
     * 样品编号规则代码
     */
    private static final String SAMPLE_CODE_RULE = "SAMPLE_NO";

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private InspectionSamplingMapper inspectionSamplingMapper;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private PlatformCodeFeignClient platformCodeFeignClient;

    @Autowired
    private InspectItemMapper inspectItemMapper;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private TaskService taskService;


    @Autowired
    private com.bmos.lims2.server.inspect.sample.ledger.service.SampleLedgerService sampleLedgerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SampleDTO> generateSamplesFromSampling(Long inspectionOrderId, List<Long> samplingIds) {
        if (inspectionOrderId == null || samplingIds == null || samplingIds.isEmpty()) {
            log.warn("生成样品参数为空，检验单ID：{}，取样ID列表：{}", inspectionOrderId, samplingIds);
            return new ArrayList<>();
        }

        log.info("开始根据取样计划生成样品，检验单ID：{}，取样计划数量：{}", inspectionOrderId, samplingIds.size());

        // 查询检验单信息（用于留样期限计算）
        InspectionOrder inspectionOrder = inspectionOrderMapper.selectById(inspectionOrderId);
        if (inspectionOrder == null) {
            log.warn("检验单不存在，检验单ID：{}", inspectionOrderId);
            return new ArrayList<>();
        }

        List<SampleDTO> generatedSamples = new ArrayList<>();

        try {
            // 查询取样计划信息
            List<InspectionSampling> samplingList = samplingIds.stream()
                    .map(id -> inspectionSamplingMapper.selectById(id))
                    .filter(sampling -> sampling != null)
                    .collect(Collectors.toList());

            if (samplingList.isEmpty()) {
                log.warn("未找到有效的取样计划，取样ID列表：{}", samplingIds);
                return generatedSamples;
            }

            // 为每个取样计划生成对应数量的样品
            for (InspectionSampling sampling : samplingList) {
                int sampleCount = sampling.getSampleCount();
                log.info("为取样计划ID：{}生成{}个样品", sampling.getId(), sampleCount);
                BatchNextCodeVO batchNextCodeVO = generateSampleNoBatch(sampleCount);
                for (int i = 0; i < sampleCount; i++) {
                    // 获取样品编号（未确认）
//                    NextCodeVO sampleCodeVO = generateSampleNo();
//                    sampleCodes.add(sampleCodeVO);

                    // 创建样品
                    Sample sample = new Sample();
                    sample.setInspectionOrderId(inspectionOrderId);
                    sample.setSampleNo(batchNextCodeVO.getNos().get(i).getNo());
                    sample.setSampleName("样品-" + batchNextCodeVO.getNos().get(i).getNo());
                    // 设置初始状态标志位：未取样、未接收、未分样、未领取、未作废
                    sample.setSampled(false);
                    sample.setReceived(false);
                    sample.setCollected(false);
                    sample.setDiscarded(false);
                    sample.setInspectItemId(sampling.getInspectItemId());
                    sample.setPlanQuantity(sampling.getPlannedQuantity());  // 设置计划取样量
                    sample.setUnitId(sampling.getUnitId());  // 修改为Unit而不是UnitId
                    sample.setCurrentQuantity(sampling.getPlannedQuantity());  // 初始化当前数量等于计划数量
                    sample.setRemark("根据取样计划自动生成");
                    sample.setTagPrinted(false);
                    sample.setDestroyed(false);  // 初始化为未销毁

                    // 如果是留样检验项目，设置留样期限（有效期至+1年）
                    if (sampling.getInspectItemId() != null &&
                        sampling.getInspectItemId().equals(InspectItemConstants.RETENTION_INSPECT_ITEM_ID) &&
                        inspectionOrder.getRetentionExpiryDate() != null) {
                        sample.setRetentionExpiryDate(inspectionOrder.getRetentionExpiryDate().plusYears(1));
                        log.info("留样样品，有效期至：{}，留样期限：{}",
                            inspectionOrder.getRetentionExpiryDate(),
                            sample.getRetentionExpiryDate());
                    }

                    // 保存样品
                    sampleMapper.insert(sample);

                    // 转换为DTO
                    SampleDTO sampleDTO = SampleConverter.INSTANCE.toDTO(sample);
                    generatedSamples.add(sampleDTO);

                    log.info("样品创建成功，ID：{}，编号：{}", sample.getId(), sample.getSampleNo());
                }

                // 更新取样计划，关联第一个样品的信息（如果只有一个样品）或者关联到第一个样品
                if (sampleCount > 0 && !generatedSamples.isEmpty()) {
                    SampleDTO firstSample = generatedSamples.get(generatedSamples.size() - sampleCount);
                    sampling.setSampleId(firstSample.getId());
                    sampling.setSampleNo(firstSample.getSampleNo());
                    inspectionSamplingMapper.updateById(sampling);
                    log.info("取样计划ID：{}关联样品，样品ID：{}，样品编号：{}",
                            sampling.getId(), firstSample.getId(), firstSample.getSampleNo());
                }
                this.confirmSampleNoBatch(batchNextCodeVO);
            }

            // 确认所有样品编号已使用

            log.info("样品生成完成，检验单ID：{}，生成样品数量：{}", inspectionOrderId, generatedSamples.size());

        } catch (Exception e) {
            log.error("生成样品失败，检验单ID：{}，错误：{}", inspectionOrderId, e.getMessage(), e);
            // 生成失败时不确认编号，让编号可以被重复使用
            throw e;
        }

        return generatedSamples;
    }

    @Override
    public List<SampleDTO> getSamplesByInspectionOrderId(Long inspectionOrderId) {
        if (inspectionOrderId == null) {
            return new ArrayList<>();
        }

        // 使用带关联的查询，直接返回包含检验项目编码/名称、单位名称等扩展信息
        List<SampleDTO> samples = sampleMapper.selectByInspectionOrderIdWithRelation(inspectionOrderId);
        // 兜底填充单位名称（全局/扩展单位处理）
        for (SampleDTO s : samples) {
            if (s.getUnitId() != null && (s.getUnitName() == null || s.getUnitName().trim().isEmpty())) {
                s.setUnitName(unitCache.getGlobalUnitName(s.getUnitId()));
            }
        }
        return samples;
    }

    /**
     * 生成样品编号（未确认使用）
     *
     * @return NextCodeVO 包含编号信息
     */
    private NextCodeVO generateSampleNo() {
        log.info("调用平台接口获取样品编号，编号规则代码：{}", SAMPLE_CODE_RULE);
        return platformCodeFeignClient.getSampleNextUseNo(SAMPLE_CODE_RULE);
    }

    /**
     * 生成样品编号（未确认使用）
     *
     * @return NextCodeVO 包含编号信息
     */
    private BatchNextCodeVO generateSampleNoBatch(int num) {
        log.info("调用平台接口获取样品编号，编号规则代码：{}", SAMPLE_CODE_RULE);
        return platformCodeFeignClient.getSampleNextUseNoBatch(SAMPLE_CODE_RULE, num);
    }

    /**
     * 确认样品编号已使用
     *
     * @param batchNextCodeVO 编号信息
     */
    private void confirmSampleNoBatch(BatchNextCodeVO batchNextCodeVO) {
        log.info("确认样品编号已使用，编号：{}", batchNextCodeVO.getNos());
        platformCodeFeignClient.confirmSampleNoBatch(batchNextCodeVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSampleInfo(SampleUpdateDTO sampleUpdateDTO) {
        List<SampleDTO> sampleDTOS = sampleUpdateDTO.getSamples();
        if (CollectionUtil.isEmpty(sampleDTOS)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品不能为空");
        }

        List<Long> sampleIds = sampleDTOS.stream().map(SampleDTO::getId).collect(Collectors.toList());
        List<Sample> samples = sampleMapper.selectBatchIds(sampleIds);
        Map<Long, Sample> sampleMap = samples.stream().collect(Collectors.toMap(Sample::getId, sample -> sample));

        sampleDTOS.forEach(sampleDTO -> {
            Sample sample = sampleMap.get(sampleDTO.getId());
            if (sample == null) {
                throw new BmosException(LimsResponseCode.INVALID_SAMPLE_NOT_EXITS, sampleDTO.getSampleNo());
            }
            if (sample.getSampled()) {
                throw new BmosException(LimsResponseCode.INVALID_SAMPLE_ALREADY_SAMPLED, sampleDTO.getSampleNo());
            }
            sample.setQuantity(sampleDTO.getQuantity());
        });

        sampleMapper.updateBatch(samples);
        log.info("样品标记为已取样，样品ID：{}", samples.stream().map(Sample::getSampleNo).collect(Collectors.joining(",")));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void samplingConfirm(SampleUpdateDTO sampleUpdateDTO) {
        List<SampleDTO> sampleDTOS = sampleUpdateDTO.getSamples();
        if (CollectionUtil.isEmpty(sampleDTOS)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品不能为空");
        }

        List<Long> sampleIds = sampleDTOS.stream().map(SampleDTO::getId).collect(Collectors.toList());
        List<Sample> samples = sampleMapper.selectBatchIds(sampleIds);
        Map<Long, Sample> sampleMap = samples.stream().collect(Collectors.toMap(Sample::getId, sample -> sample));

        sampleDTOS.forEach(sampleDTO -> {
            Sample sample = sampleMap.get(sampleDTO.getId());
            if (sample == null) {
                throw new BmosException(LimsResponseCode.INVALID_SAMPLE_NOT_EXITS, sampleDTO.getSampleNo());
            }
            if (sample.getSampled()) {
                throw new BmosException(LimsResponseCode.INVALID_SAMPLE_ALREADY_SAMPLED, sampleDTO.getSampleNo());
            }
            sample.setSamplerId(SysUserHolder.getUser().getUserId());
            sample.setSampled(true);
            sample.setQuantity(sampleDTO.getQuantity());
            sample.setSamplerName(SysUserHolder.getUser().getUserName());
            sample.setSamplingTime(LocalDateTime.now());
        });

        sampleMapper.updateBatch(samples);
        log.info("样品标记为已取样，样品ID：{}", samples.stream().map(Sample::getSampleNo).collect(Collectors.joining(",")));

        // 记录样品台账：取样
        for (Sample sample : samples) {
            try {
                sampleLedgerService.recordEvent(sample.getInspectionOrderId(), sample.getId(),
                        com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum.SAMPLED, null);
            } catch (Exception ex) {
                log.warn("记录样品台账失败（取样），样品ID：{}，原因：{}", sample.getId(), ex.getMessage());
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmTagPrinted(SampleUpdateDTO sampleUpdateDTO) {
        List<SampleDTO> sampleDTOS = sampleUpdateDTO.getSamples();
        if (CollectionUtil.isEmpty(sampleDTOS)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品不能为空");
        }

        List<Long> sampleIds = sampleDTOS.stream().map(SampleDTO::getId).collect(Collectors.toList());
        List<Sample> samples = sampleMapper.selectBatchIds(sampleIds);
        Map<Long, Sample> sampleMap = samples.stream().collect(Collectors.toMap(Sample::getId, sample -> sample));

        sampleDTOS.forEach(sampleDTO -> {
            Sample sample = sampleMap.get(sampleDTO.getId());
            if (sample == null) {
                throw new BmosException(LimsResponseCode.INVALID_SAMPLE_NOT_EXITS, sampleDTO.getSampleNo());
            }
            sample.setTagPrinted(true);
        });

        sampleMapper.updateBatch(samples);
        log.info("样品标签标记为已打印，样品编号：{}", samples.stream().map(Sample::getSampleNo).collect(Collectors.joining(",")));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markSampleAsReceived(Long sampleId, LocalDateTime receiveTime) {
        if (sampleId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品ID不能为空");
        }

        Sample sample = sampleMapper.selectById(sampleId);
        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "样品不存在");
        }

        sample.setReceived(true);
        sample.setReceiverId(SysUserHolder.getUser().getUserId());
        sample.setReceiverName(SysUserHolder.getUser().getUserName());
        sample.setReceiveTime(receiveTime);

        sampleMapper.updateById(sample);

        // 任务创建改为在样品领取阶段触发，这里不再生成任务
        // 记录样品台账
        try {
            sampleLedgerService.recordEvent(sample.getInspectionOrderId(), sampleId, com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum.RECEIVED, null);
        } catch (Exception ex) {
            log.warn("记录样品台账失败，样品接收，样品ID：{}，原因：{}", sampleId, ex.getMessage());
        }

        log.info("样品标记为已接收，样品编号：{}", sampleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateSamples(SampleBatchUpdateDTO batchUpdateDTO) {
        if (batchUpdateDTO == null || batchUpdateDTO.getInspectionOrderId() == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "请验单ID不能为空");
        }

        if (batchUpdateDTO.getSamples() == null || batchUpdateDTO.getSamples().isEmpty()) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品操作列表不能为空");
        }

        // 验证请验单是否存在
        InspectionOrder inspectionOrder = inspectionOrderMapper.selectById(batchUpdateDTO.getInspectionOrderId());
        if (inspectionOrder == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "请验单不存在");
        }

        // 获取当前请验单的所有样品
        List<Sample> existingSamples = sampleMapper.selectList(
                new LambdaQueryWrapper<Sample>()
                        .eq(Sample::getInspectionOrderId, batchUpdateDTO.getInspectionOrderId())
        );

        // 记录前端提交的样品ID（用于判断哪些样品被删除了）
        Set<Long> submittedSampleIds = new HashSet<>();

        // 处理前端提交的每个样品
        for (SampleBatchUpdateDTO.SampleInfoDTO sampleInfo : batchUpdateDTO.getSamples()) {
            if (sampleInfo.getSampleId() == null) {
                // 新增样品（没有sampleId）
                addSample(batchUpdateDTO.getInspectionOrderId(), sampleInfo);
            } else {
                // 更新现有样品
                updateSample(sampleInfo);
                submittedSampleIds.add(sampleInfo.getSampleId());
            }
        }

        // 删除前端没有提交的现有样品（前端删除的样品不会传到后端）
        for (Sample existingSample : existingSamples) {
            if (!submittedSampleIds.contains(existingSample.getId())) {
                // 检查是否可以删除（已取样的样品不能删除）
                if (!existingSample.getSampled()) {
                    deleteSample(existingSample.getId());
                    log.info("删除前端未提交的样品，样品ID：{}，样品编号：{}", existingSample.getId(), existingSample.getSampleNo());
                } else {
                    log.warn("样品已取样，无法删除，样品ID：{}，样品编号：{}", existingSample.getId(), existingSample.getSampleNo());
                }
            }
        }

        log.info("批量更新样品完成，请验单ID：{}，操作数量：{}", batchUpdateDTO.getInspectionOrderId(), batchUpdateDTO.getSamples().size());
    }

    /**
     * 新增样品
     */
    private void addSample(Long inspectionOrderId, SampleBatchUpdateDTO.SampleInfoDTO sampleInfo) {
        Sample sample = new Sample();
        sample.setInspectionOrderId(inspectionOrderId);
        sample.setSampleNo(sampleInfo.getSampleNo());
        sample.setInspectItemId(sampleInfo.getInspectionItemId());
        if (sampleInfo.getPlanQuantity() != null) {
            sample.setPlanQuantity(sampleInfo.getPlanQuantity());
        }
        sample.setQuantity(sampleInfo.getActualQuantity());
        if (sampleInfo.getUnitId() != null) {
            sample.setUnitId(sampleInfo.getUnitId());
        }
        sample.setRemark(sampleInfo.getRemark());
        sample.setSampled(false);
        sample.setReceived(false);
        sample.setCollected(false);
        sample.setDiscarded(false);
        sample.setTagPrinted(false);

        // 如果没有提供样品编号，自动生成
        if (sample.getSampleNo() == null || sample.getSampleNo().trim().isEmpty()) {
            sample.setSampleNo(generateSampleNo().getNo());
        }

        sampleMapper.insert(sample);
        log.info("新增样品，请验单ID：{}，样品编号：{}", inspectionOrderId, sample.getSampleNo());
    }

    /**
     * 更新样品
     */
    private void updateSample(SampleBatchUpdateDTO.SampleInfoDTO sampleInfo) {
        if (sampleInfo.getSampleId() == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "更新操作必须提供样品ID");
        }

        Sample sample = sampleMapper.selectById(sampleInfo.getSampleId());
        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "样品不存在");
        }

        // 更新样品信息
        if (sampleInfo.getInspectionItemId() != null) {
            sample.setInspectItemId(sampleInfo.getInspectionItemId());
        }
        if (sampleInfo.getPlanQuantity() != null) {
            sample.setPlanQuantity(sampleInfo.getPlanQuantity());
        }
        if (sampleInfo.getActualQuantity() != null) {
            sample.setQuantity(sampleInfo.getActualQuantity());
        }
        if (sampleInfo.getUnitId() != null) {
            sample.setUnitId(sampleInfo.getUnitId());
        }
        if (sampleInfo.getRemark() != null) {
            sample.setRemark(sampleInfo.getRemark());
        }

        sampleMapper.updateById(sample);
        log.info("更新样品，样品ID：{}，样品编号：{}", sample.getId(), sample.getSampleNo());
    }

    /**
     * 删除样品
     */
    private void deleteSample(Long sampleId) {
        if (sampleId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "删除操作必须提供样品ID");
        }

        Sample sample = sampleMapper.selectById(sampleId);
        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "样品不存在");
        }

        // 检查样品状态，已取样的样品不能删除
        if (sample.getSampled()) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }

        sampleMapper.deleteById(sampleId);
        log.info("删除样品，样品ID：{}，样品编号：{}", sampleId, sample.getSampleNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUnsampledSample(Long sampleId) {
        // 复用已有的删除校验逻辑：仅未取样的允许删除
        deleteSample(sampleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SampleDTO> addSamples(Long orderId, List<SampleAddDTO> sampleAddDTOS) {
        log.info("开始生成样品,请验单id：{}", orderId);

        List<SampleDTO> generatedSamples = new ArrayList<>();

        try {
            for (SampleAddDTO sampleAddDTO : sampleAddDTOS) {
                BatchNextCodeVO batchNextCodeVO = generateSampleNoBatch(sampleAddDTO.getCount());
                int sampleCount = sampleAddDTO.getCount();
                log.info("为检验单生成样品：{}生成{}个样品", orderId, sampleCount);

                for (int i = 0; i < sampleCount; i++) {
                    // 创建样品
                    Sample sample = new Sample();
                    sample.setInspectionOrderId(orderId);
                    sample.setSampleNo(batchNextCodeVO.getNos().get(i).getNo());
                    sample.setSampleName("样品-" + batchNextCodeVO.getNos().get(i).getNo());
                    // 设置初始状态标志位：未取样、未接收、未分样、未领取、未作废
                    sample.setSampled(false);
                    sample.setReceived(false);
                    sample.setCollected(false);
                    sample.setDiscarded(false);
                    sample.setInspectItemId(sampleAddDTO.getInspectItemId());
                    sample.setPlanQuantity(sampleAddDTO.getPlanQuantity());  // 设置计划取样量
                    sample.setQuantity(sampleAddDTO.getQuantity());    // 设置实际取样量（可为空）
                    sample.setUnitId(sampleAddDTO.getUnitId());
                    sample.setRemark("根据取样计划自动生成");
                    sample.setTagPrinted(false);

                    // 保存样品
                    sampleMapper.insert(sample);

                    // 转换为DTO
                    SampleDTO sampleDTO = SampleConverter.INSTANCE.toDTO(sample);
                    generatedSamples.add(sampleDTO);

                    log.info("样品创建成功，ID：{}，编号：{}", sample.getId(), sample.getSampleNo());
                }
                confirmSampleNoBatch(batchNextCodeVO);
            }

            // 补充前端所需扩展字段（单位名称、检验项目编码/名称）
            // 单位名称：走缓存
            for (SampleDTO s : generatedSamples) {
                if (s.getUnitId() != null) {
                    s.setUnitName(unitCache.getGlobalUnitName(s.getUnitId()));
                }
            }
            // 检验项目编码/名称：一次性查出
            List<Long> inspectItemIds = generatedSamples.stream()
                    .map(SampleDTO::getInspectItemId)
                    .filter(java.util.Objects::nonNull)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
            if (!inspectItemIds.isEmpty()) {
                List<InspectItem> items = inspectItemMapper.selectBatchIds(inspectItemIds);
                java.util.Map<Long, InspectItem> idToItem = items.stream()
                        .collect(java.util.stream.Collectors.toMap(InspectItem::getId, it -> it));
                for (SampleDTO s : generatedSamples) {
                    InspectItem it = idToItem.get(s.getInspectItemId());
                    if (it != null) {
                        s.setInspectItemName(it.getName());
                        s.setInspectItemCode(it.getCode());
                    }
                }
            }

            log.info("样品生成完成，检验单ID：{}，生成样品数量：{}", orderId, generatedSamples.size());

        } catch (Exception e) {
            log.error("生成样品失败，检验单ID：{}，错误：{}", orderId, e.getMessage(), e);
            // 生成失败时不确认编号，让编号可以被重复使用
            throw e;
        }

        return generatedSamples;
    }


    @Override
    public SampleScanResultDTO scanSample(String sampleNo) {
        if (!StringUtils.hasText(sampleNo)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品编号不能为空");
        }

        log.info("扫描样品二维码，样品编号：{}", sampleNo);

        // 根据样品编号查询样品详细信息
        SampleScanResultDTO scanResult = sampleMapper.selectSampleScanResult(sampleNo);
        if (scanResult == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "样品不存在，样品编号：" + sampleNo);
        }
        scanResult.setUnitName(unitCache.getGlobalUnitName(scanResult.getUnitId()));
        if (scanResult.getRecycleUnitId() != null) {
            scanResult.setRecycleUnitName(unitCache.getGlobalUnitName(scanResult.getRecycleUnitId()));
        }

        return scanResult;
    }


    @Override
    public SamplePrintTagResultDTO samplePrintTag(String sampleNo) {
        if (!StringUtils.hasText(sampleNo)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品编号不能为空");
        }

        log.info("扫描样品二维码，样品编号：{}", sampleNo);

        // 根据样品编号查询样品详细信息
        SamplePrintTagResultDTO resultDTO = sampleMapper.selectSamplePrintTagResult(sampleNo);
        if (resultDTO == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "样品不存在，样品编号：" + sampleNo);
        }
        resultDTO.setUnitName(unitCache.getGlobalUnitName(resultDTO.getUnitId()));
        if (resultDTO.getRecycleUnitId() != null) {
            resultDTO.setRecycleUnitName(unitCache.getGlobalUnitName(resultDTO.getRecycleUnitId()));
        }
        resultDTO.setFullMaterialName(resultDTO.getMaterialCode() + "-" + resultDTO.getMaterialName());
        if (resultDTO.getQuantity() != null) {
            resultDTO.setQuantityWithUnit(resultDTO.getQuantity() + resultDTO.getUnitName());
        }
        if (resultDTO.getPlanQuantity() != null) {
            resultDTO.setPlanQuantityWithUnit(resultDTO.getPlanQuantity() + resultDTO.getUnitName());
        }
        resultDTO.setInspectItemInfo(resultDTO.getInspectItemCode() + "-" + resultDTO.getInspectItemName());

        return resultDTO;
    }

    @Override
    public List<SampleScanResultDTO> getSamplesByOrderFromSampleNo(String sampleNo) {
        if (!StringUtils.hasText(sampleNo)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品编号不能为空");
        }

        log.info("根据样品编号查询请验单下所有样品（修改后的扫码接口），样品编号：{}", sampleNo);

        // 先根据样品编号查询样品信息
        Sample sample = sampleMapper.selectBySampleNo(sampleNo);
        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "样品不存在，样品编号：" + sampleNo);
        }

        // 根据检验单ID查询该检验单下的所有样品的详细信息（与扫码接口保持一致）
        List<SampleScanResultDTO> allSamples = sampleMapper.selectSampleScanResultsByOrderId(sample.getInspectionOrderId());

        // 为每个样品设置单位名称
        for (SampleScanResultDTO scanResult : allSamples) {
            scanResult.setUnitName(unitCache.getGlobalUnitName(scanResult.getUnitId()));
            if (scanResult.getRecycleUnitId() != null) {
                scanResult.setRecycleUnitName(unitCache.getGlobalUnitName(scanResult.getRecycleUnitId()));
            }
        }

        return allSamples;
    }

    @Override
    public CommonPage<SampleCollectionListDTO> getCollectionPageList(SampleCollectionPageQueryDTO queryDTO) {
        if (queryDTO == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "查询参数不能为空");
        }

        log.info("分页查询可领取样品列表，查询条件：{}", queryDTO);

        // 设置分页参数
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 执行查询
        List<SampleCollectionListDTO> list = sampleMapper.selectCollectionPageList(queryDTO);

        for (SampleCollectionListDTO sample : list) {
            sample.setUnitName(unitCache.getGlobalUnitName(sample.getUnitId()));
        }

        // 转换分页结果
        CommonPage<SampleCollectionListDTO> result = CommonPage.convertPage(list);

        log.info("查询可领取样品列表完成，总数：{}", result.getTotal());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCollectSamples(SampleBatchCollectionDTO batchCollectionDTO) {
        if (batchCollectionDTO == null || CollectionUtil.isEmpty(batchCollectionDTO.getSampleIds())) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品ID列表不能为空");
        }

        if (batchCollectionDTO.getCollectorId() == null || batchCollectionDTO.getCollectorName() == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "领取人信息不能为空");
        }

        log.info("开始批量领取样品，样品数量：{}，领取人：{}",
                batchCollectionDTO.getSampleIds().size(), batchCollectionDTO.getCollectorName());

        // 查询要领取的样品
        List<Sample> samples = sampleMapper.selectBatchIds(batchCollectionDTO.getSampleIds());
        if (samples.size() != batchCollectionDTO.getSampleIds().size()) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "部分样品不存在");
        }

        // 验证样品状态
        LocalDateTime collectTime = LocalDateTime.now();
        for (Sample sample : samples) {
            // 检查样品是否已取样
            if (!sample.getSampled()) {
                throw new BmosException(LimsResponseCode.COLLECT_ERROR_SAMPLE_NOT_SAMPLED, sample.getSampleNo());
            }

            // 检查样品是否已接收且未领取
            if (!sample.getReceived()) {
                throw new BmosException(LimsResponseCode.COLLECT_ERROR_SAMPLE_NOT_RECEIVED,
                        sample.getSampleNo());
            }
            if (sample.getCollected()) {
                throw new BmosException(LimsResponseCode.COLLECT_ERROR_SAMPLE_ALREADY_COLLECTED,
                        sample.getSampleNo());
            }
            if (sample.getDiscarded()) {
                throw new BmosException(LimsResponseCode.COLLECT_ERROR_SAMPLE_DISCARDED, sample.getSampleNo());
            }

            // 更新样品状态
            sample.setCollected(true);
            sample.setCollectorId(batchCollectionDTO.getCollectorId());
            sample.setCollectorName(batchCollectionDTO.getCollectorName());
            sample.setCollectTime(collectTime);
        }

        // 批量更新样品状态
        sampleMapper.updateBatch(samples);

        log.info("批量领取样品完成，领取样品数量：{}，领取人：{}，领取时间：{}",
                samples.size(), batchCollectionDTO.getCollectorName(), collectTime);

        // 记录样品台账：领取
        for (Sample sample : samples) {
            try {
                sampleLedgerService.recordEvent(sample.getInspectionOrderId(), sample.getId(),
                        com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum.COLLECTED, null);
            } catch (Exception ex) {
                log.warn("记录样品台账失败（批量领取），样品ID：{}，原因：{}", sample.getId(), ex.getMessage());
            }
        }

        // 在样品领取阶段创建任务：对本次领取的每个样品触发任务生成
        try {
            for (Sample sample : samples) {
                try {
                    taskService.generateTasksAfterSampleReceived(sample.getId(), SysUserHolder.getUser());
                } catch (Exception inner) {
                    log.error("生成任务失败（样品领取后），样品ID：{}", sample.getId(), inner);
                }
            }
        } catch (Exception e) {
            log.error("批量生成任务失败（样品领取后）", e);
        }
    }

}
