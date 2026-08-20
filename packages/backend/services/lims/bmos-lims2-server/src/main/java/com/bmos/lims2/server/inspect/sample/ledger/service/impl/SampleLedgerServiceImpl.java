package com.bmos.lims2.server.inspect.sample.ledger.service.impl;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.server.inspect.sample.ledger.entity.SampleLedger;
import com.bmos.lims2.server.inspect.sample.ledger.mapper.SampleLedgerMapper;
import com.bmos.lims2.server.inspect.sample.ledger.service.SampleLedgerService;
import com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.lims2.server.material.mapper.MaterialMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 样品台账服务实现
 * @Author: yigaohui
 * @Date: 2025/09/05 10:00
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SampleLedgerServiceImpl implements SampleLedgerService {

    private final SampleLedgerMapper sampleLedgerMapper;
    private final InspectionOrderMapper inspectionOrderMapper;
    private final SampleMapper sampleMapper;
    private final MaterialMapper materialMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordEvent(Long inspectionOrderId, Long sampleId, SampleLedgerOperationTypeEnum operationType, String remark) {
        SampleLedger ledger = new SampleLedger();
        ledger.setInspectionOrderId(inspectionOrderId);
        ledger.setSampleId(sampleId);
        // 填充快照字段
        Sample sample = sampleId == null ? null : sampleMapper.selectById(sampleId);
        if (sample != null) {
            ledger.setSampleNo(sample.getSampleNo());
            ledger.setQuantity(sample.getQuantity());
            ledger.setRecycleQuantity(sample.getRecycleQuantity());
            ledger.setUnitId(sample.getUnitId());
        }
        InspectionOrder order = inspectionOrderId == null ? null : inspectionOrderMapper.selectById(inspectionOrderId);
        if (order != null) {
            ledger.setMaterialId(order.getMaterialId());
            if (order.getMaterialId() != null) {
                Material material = materialMapper.selectById(order.getMaterialId());
                if (material != null) {
                    ledger.setMaterialCode(material.getCode());
                }
            }
        }
        ledger.setOperationType(operationType);
        // 依照查询规则：SAMPLED=1, RECEIVED=2, DIVIDED=3, COLLECTED=4, RECYCLED=5, PROCESSED=6, 其它=0
        ledger.setStatus(mapStatus(operationType));
        ledger.setOperationTime(LocalDateTime.now());
        if (SysUserHolder.getUser() != null) {
            ledger.setOperatorId(SysUserHolder.getUser().getUserId());
            ledger.setOperatorName(SysUserHolder.getUser().getUserName());
        }
        ledger.setRemark(remark);
        sampleLedgerMapper.insert(ledger);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordEventWithSnapshot(Long inspectionOrderId,
                                 Long sampleId,
                                 SampleLedgerOperationTypeEnum operationType,
                                 String quantityOverride,
                                 String recycleQuantityOverride,
                                 String remark) {
        SampleLedger ledger = new SampleLedger();
        ledger.setInspectionOrderId(inspectionOrderId);
        ledger.setSampleId(sampleId);
        // 填充快照字段（可被覆盖）
        Sample sample = sampleId == null ? null : sampleMapper.selectById(sampleId);
        if (sample != null) {
            ledger.setSampleNo(sample.getSampleNo());
            ledger.setQuantity(sample.getQuantity());
            ledger.setRecycleQuantity(sample.getRecycleQuantity());
            ledger.setUnitId(sample.getUnitId());
        }
        if (quantityOverride != null) {
            ledger.setQuantity(quantityOverride);
        }
        if (recycleQuantityOverride != null) {
            ledger.setRecycleQuantity(recycleQuantityOverride);
        }
        InspectionOrder order = inspectionOrderId == null ? null : inspectionOrderMapper.selectById(inspectionOrderId);
        if (order != null) {
            ledger.setMaterialId(order.getMaterialId());
            if (order.getMaterialId() != null) {
                Material material = materialMapper.selectById(order.getMaterialId());
                if (material != null) {
                    ledger.setMaterialCode(material.getCode());
                }
            }
        }
        ledger.setOperationType(operationType);
        ledger.setStatus(mapStatus(operationType));
        ledger.setOperationTime(LocalDateTime.now());
        if (SysUserHolder.getUser() != null) {
            ledger.setOperatorId(SysUserHolder.getUser().getUserId());
            ledger.setOperatorName(SysUserHolder.getUser().getUserName());
        }
        ledger.setRemark(remark);
        sampleLedgerMapper.insert(ledger);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordEventWithSnapshot(Long inspectionOrderId,
                                 Long sampleId,
                                 SampleLedgerOperationTypeEnum operationType,
                                 String quantityOverride,
                                 String recycleQuantityOverride,
                                 String consumedQuantityOverride,
                                 String remark) {
        SampleLedger ledger = new SampleLedger();
        ledger.setInspectionOrderId(inspectionOrderId);
        ledger.setSampleId(sampleId);
        // 填充快照字段（可被覆盖）
        Sample sample = sampleId == null ? null : sampleMapper.selectById(sampleId);
        if (sample != null) {
            ledger.setSampleNo(sample.getSampleNo());
            ledger.setQuantity(sample.getQuantity());
            ledger.setRecycleQuantity(sample.getRecycleQuantity());
            ledger.setUnitId(sample.getUnitId());
        }
        if (quantityOverride != null) {
            ledger.setQuantity(quantityOverride);
        }
        if (recycleQuantityOverride != null) {
            ledger.setRecycleQuantity(recycleQuantityOverride);
        }
        if (consumedQuantityOverride != null) {
            ledger.setConsumedQuantity(consumedQuantityOverride);
        }
        InspectionOrder order = inspectionOrderId == null ? null : inspectionOrderMapper.selectById(inspectionOrderId);
        if (order != null) {
            ledger.setMaterialId(order.getMaterialId());
            if (order.getMaterialId() != null) {
                Material material = materialMapper.selectById(order.getMaterialId());
                if (material != null) {
                    ledger.setMaterialCode(material.getCode());
                }
            }
        }
        ledger.setOperationType(operationType);
        ledger.setStatus(mapStatus(operationType));
        ledger.setOperationTime(LocalDateTime.now());
        if (SysUserHolder.getUser() != null) {
            ledger.setOperatorId(SysUserHolder.getUser().getUserId());
            ledger.setOperatorName(SysUserHolder.getUser().getUserName());
        }
        ledger.setRemark(remark);
        sampleLedgerMapper.insert(ledger);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordEventWithSnapshot(Long inspectionOrderId,
                                 Long sampleId,
                                 SampleLedgerOperationTypeEnum operationType,
                                 String quantityOverride,
                                 String recycleQuantityOverride,
                                 String consumedQuantityOverride,
                                 Integer statusOverride,
                                 String remark) {
        SampleLedger ledger = new SampleLedger();
        ledger.setInspectionOrderId(inspectionOrderId);
        ledger.setSampleId(sampleId);
        // 填充快照字段（可被覆盖）
        Sample sample = sampleId == null ? null : sampleMapper.selectById(sampleId);
        if (sample != null) {
            ledger.setSampleNo(sample.getSampleNo());
            ledger.setQuantity(sample.getQuantity());
            ledger.setRecycleQuantity(sample.getRecycleQuantity());
            ledger.setUnitId(sample.getUnitId());
        }
        if (quantityOverride != null) {
            ledger.setQuantity(quantityOverride);
        }
        if (recycleQuantityOverride != null) {
            ledger.setRecycleQuantity(recycleQuantityOverride);
        }
        if (consumedQuantityOverride != null) {
            ledger.setConsumedQuantity(consumedQuantityOverride);
        }
        InspectionOrder order = inspectionOrderId == null ? null : inspectionOrderMapper.selectById(inspectionOrderId);
        if (order != null) {
            ledger.setMaterialId(order.getMaterialId());
            if (order.getMaterialId() != null) {
                Material material = materialMapper.selectById(order.getMaterialId());
                if (material != null) {
                    ledger.setMaterialCode(material.getCode());
                }
            }
        }
        ledger.setOperationType(operationType);
        ledger.setStatus(statusOverride != null ? statusOverride : mapStatus(operationType));
        ledger.setOperationTime(LocalDateTime.now());
        if (SysUserHolder.getUser() != null) {
            ledger.setOperatorId(SysUserHolder.getUser().getUserId());
            ledger.setOperatorName(SysUserHolder.getUser().getUserName());
        }
        ledger.setRemark(remark);
        sampleLedgerMapper.insert(ledger);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordEvents(List<SampleLedger> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        for (SampleLedger e : events) {
            if (e.getOperationTime() == null) {
                e.setOperationTime(LocalDateTime.now());
            }
            if (e.getOperatorId() == null && SysUserHolder.getUser() != null) {
                e.setOperatorId(SysUserHolder.getUser().getUserId());
                e.setOperatorName(SysUserHolder.getUser().getUserName());
            }
            // 若未显式设置状态，则按操作类型映射补齐
            if (e.getStatus() == null) {
                e.setStatus(mapStatus(e.getOperationType()));
            }
            // 若缺少快照字段则尽量补齐
            if (e.getSampleId() != null) {
                Sample sample = sampleMapper.selectById(e.getSampleId());
                if (sample != null) {
                    if (e.getSampleNo() == null) e.setSampleNo(sample.getSampleNo());
                    if (e.getQuantity() == null) e.setQuantity(sample.getQuantity());
                    if (e.getRecycleQuantity() == null) e.setRecycleQuantity(sample.getRecycleQuantity());
                    if (e.getUnitId() == null) e.setUnitId(sample.getUnitId());
                    if (e.getInspectionOrderId() == null) e.setInspectionOrderId(sample.getInspectionOrderId());
                }
            }
            if (e.getInspectionOrderId() != null) {
                InspectionOrder order = inspectionOrderMapper.selectById(e.getInspectionOrderId());
                if (order != null) {
                    if (e.getMaterialId() == null) e.setMaterialId(order.getMaterialId());
                    if (e.getMaterialCode() == null && order.getMaterialId() != null) {
                        Material material = materialMapper.selectById(order.getMaterialId());
                        if (material != null) {
                            e.setMaterialCode(material.getCode());
                        }
                    }
                }
            }
        }
        sampleLedgerMapper.batchInsert(events);
    }

    @Override
    public List<SampleLedger> listByOrderId(Long orderId) {
        return sampleLedgerMapper.listByOrderId(orderId);
    }

    private Integer mapStatus(SampleLedgerOperationTypeEnum op) {
        if (op == null) return 0;
        switch (op) {
            case SAMPLED: return 1;
            case RECEIVED: return 2;
            case DIVIDED: return 3;
            case COLLECTED: return 4;
            case RECYCLED: return 5;
            case PROCESSED: return 6;
            default: return 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePreviousConsumption(Long sampleId, String consumedQuantity) {
        if (sampleId == null) {
            return;
        }
        SampleLedger last = sampleLedgerMapper.selectLastBySampleId(sampleId);
        if (last == null) {
            return;
        }
        // 更新上一条台账的消耗量
        sampleLedgerMapper.updateConsumedQuantityById(last.getId(), consumedQuantity);
    }
}


