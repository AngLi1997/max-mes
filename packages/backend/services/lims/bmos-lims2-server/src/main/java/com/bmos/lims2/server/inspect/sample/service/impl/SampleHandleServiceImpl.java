package com.bmos.lims2.server.inspect.sample.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.inspect.sample.dto.*;
import com.bmos.lims2.server.inspect.sample.service.SampleHandleService;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 样品处理服务实现
 * @Author: yigaohui
 * @Date: 2025/02/01 10:52
 */
@Service
public class SampleHandleServiceImpl implements SampleHandleService {

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private com.bmos.lims2.server.inspect.sample.ledger.service.SampleLedgerService sampleLedgerService;

    @Override
    public CommonPage<SampleHandleListDTO> getHandlePageList(SampleHandlePageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<SampleHandleListDTO> list = sampleMapper.selectHandlePageList(queryDTO);
        for (SampleHandleListDTO item : list) {
            if (item.getUnitId() != null) {
                item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
            }
            if (item.getRecycleUnitId() != null) {
                item.setRecycleUnitName(unitCache.getGlobalUnitName(item.getRecycleUnitId()));
            }
        }
        return CommonPage.convertPage(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRecycle(SampleBatchRecycleDTO dto) {
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "回收条目列表不能为空");
        }
        LocalDateTime now = LocalDateTime.now();
        java.util.List<Long> ids = dto.getItems().stream().map(SampleRecycleItemDTO::getSampleId).collect(java.util.stream.Collectors.toList());
        List<Sample> samples = sampleMapper.selectBatchIds(ids);
        // 校验检验单是否已结束（终止、样品审核通过、或完成）
        validateOrdersEnded(samples);
        java.util.Map<Long, SampleRecycleItemDTO> idToItem = dto.getItems().stream()
                .collect(java.util.stream.Collectors.toMap(SampleRecycleItemDTO::getSampleId, x -> x, (a, b) -> b));
        for (Sample s : samples) {
            if (!Boolean.TRUE.equals(s.getReceived())) {
                throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
            }
            if (Boolean.TRUE.equals(s.getProcessed())) {
                throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
            }
            // 标记回收
            s.setRecycled(true);
            s.setRecycledTime(now);
            s.setRecycledBy(SysUserHolder.getUser().getUserName());
            SampleRecycleItemDTO item = idToItem.get(s.getId());
            if (item == null) {
                throw new BmosException(LimsResponseCode.INVALID_PARAM, "缺少样品条目:" + s.getSampleNo());
            }
            // 校验回收余量（需与样品数量同单位比较）
            BigDecimal sampleQuantity = new BigDecimal(s.getQuantity() == null ? "0" : s.getQuantity());
            BigDecimal recycleQuantity = new BigDecimal(item.getRecycleQuantity());
            Long recycleUnitId = item.getRecycleUnitId();
            Long sampleUnitId = s.getUnitId();
            BigDecimal recycleInSampleUnit = unitCache.convert(recycleQuantity, recycleUnitId, sampleUnitId);
            if (recycleInSampleUnit.compareTo(sampleQuantity) > 0) {
                throw new BmosException(LimsResponseCode.RECYCLE_QUANTITY_EXCEED_SAMPLE_QUANTITY);
            }
            s.setRecycleQuantity(item.getRecycleQuantity());
            s.setRecycleUnitId(item.getRecycleUnitId());
            s.setRecycleRemark(item.getRecycleRemark());
            // 计算消耗量 = 样品数量 - 回收余量（统一为样品单位）
            BigDecimal consumed = sampleQuantity.subtract(recycleInSampleUnit);
            if (consumed.compareTo(BigDecimal.ZERO) < 0) {
                consumed = BigDecimal.ZERO;
            }
            s.setConsumedQuantity(consumed.stripTrailingZeros().toPlainString());
        }
        sampleMapper.updateBatch(samples);
        // 台账：回收（不在本条记录消耗量，先将消耗量回填到上一条台账，再插入回收台账）
        for (Sample s : samples) {
            try {
                SampleRecycleItemDTO item = idToItem.get(s.getId());
                // 先将计算得到的消耗量回填到上一条台账
                sampleLedgerService.updatePreviousConsumption(s.getId(), s.getConsumedQuantity());
                // 此处同时覆盖消耗量为本次计算值
                sampleLedgerService.recordEventWithSnapshot(
                        s.getInspectionOrderId(),
                        s.getId(),
                        com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum.RECYCLED,
                        item.getRecycleQuantity(),
                        item.getRecycleQuantity(),
                        null,
                        item.getRecycleRemark());
            } catch (Exception ex) {
                // 忽略台账异常，不影响主流程
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchProcess(SampleBatchProcessDTO dto) {
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "处理条目列表不能为空");
        }
        LocalDateTime now = LocalDateTime.now();
        java.util.List<Long> ids = dto.getItems().stream().map(SampleProcessItemDTO::getSampleId).collect(java.util.stream.Collectors.toList());
        List<Sample> samples = sampleMapper.selectBatchIds(ids);
        // 校验检验单是否已结束
        validateOrdersEnded(samples);
        java.util.Map<Long, SampleProcessItemDTO> idToItem = dto.getItems().stream()
                .collect(java.util.stream.Collectors.toMap(SampleProcessItemDTO::getSampleId, x -> x, (a, b) -> b));
        for (Sample s : samples) {
            if (!Boolean.TRUE.equals(s.getReceived())) {
                throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
            }
            if (!Boolean.TRUE.equals(s.getRecycled())) {
                throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
            }
            // 标记处理
            s.setProcessed(true);
            s.setProcessTime(now);
            s.setProcessBy(SysUserHolder.getUser().getUserName());
            SampleProcessItemDTO item = idToItem.get(s.getId());
            if (item == null) {
                throw new BmosException(LimsResponseCode.INVALID_PARAM, "缺少样品条目:" + s.getSampleNo());
            }
            s.setProcessMethod(item.getProcessMethod());
            s.setProcessRemark(item.getProcessRemark());
        }
        sampleMapper.updateBatch(samples);
        // 台账：已处理（数量置为0，回收量保留样品上的最终回收量；若已有消耗量则一并记录到台账）
        for (Sample s : samples) {
            try {
                sampleLedgerService.recordEventWithSnapshot(
                        s.getInspectionOrderId(),
                        s.getId(),
                        com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum.PROCESSED,
                        "0",
                        s.getRecycleQuantity(),
                        s.getRecycleQuantity(),
                        s.getProcessRemark());
            } catch (Exception ex) {
                // 忽略台账异常
            }
        }
    }

    @Override
    public SampleScanResultDTO getSampleDetail(Long sampleId) {
        if (sampleId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品ID不能为空");
        }
        SampleScanResultDTO dto = sampleMapper.selectSampleScanResultById(sampleId);
        if (dto == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "样品不存在");
        }
        if (dto.getUnitId() != null) {
            dto.setUnitName(unitCache.getGlobalUnitName(dto.getUnitId()));
        }
        if (dto.getRecycleUnitId() != null) {
            dto.setRecycleUnitName(unitCache.getGlobalUnitName(dto.getRecycleUnitId()));
        }
        return dto;
    }

    @Override
    public SampleHandleStatusCountDTO getHandleStatusCount(SampleHandlePageQueryDTO queryDTO) {
        return sampleMapper.selectHandleStatusCount(queryDTO);
    }

    private void validateOrdersEnded(List<Sample> samples) {
        java.util.Set<Long> orderIds = samples.stream()
                .map(Sample::getInspectionOrderId)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());
        if (orderIds.isEmpty()) {
            return;
        }
        for (Long orderId : orderIds) {
            InspectionOrder io = inspectionOrderMapper.selectById(orderId);
            if (io == null) {
                continue;
            }
            boolean ended = Boolean.TRUE.equals(io.getFinished())
                    || io.getTerminateTime() != null
                    || io.getSampleAuditTime() != null;
            if (!ended) {
                throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
            }
        }
    }
}


