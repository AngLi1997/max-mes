package com.bmos.lims2.server.inspect.sample.ledger.service;

import com.bmos.lims2.server.inspect.sample.ledger.entity.SampleLedger;
import com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum;

import java.util.List;

/**
 * @Description: 样品台账服务
 * @Author: yigaohui
 * @Date: 2025/09/05 10:00
 */
public interface SampleLedgerService {

    void recordEvent(Long inspectionOrderId, Long sampleId, SampleLedgerOperationTypeEnum operationType, String remark);

    /**
     * 记录台账事件（可覆盖数量/回收量快照，用于回收/处理等特殊展示场景）
     */
    void recordEventWithSnapshot(Long inspectionOrderId,
                                 Long sampleId,
                                 SampleLedgerOperationTypeEnum operationType,
                                 String quantityOverride,
                                 String recycleQuantityOverride,
                                 String remark);

    /**
     * 记录台账事件（可覆盖数量/回收量/消耗量快照）
     */
    void recordEventWithSnapshot(Long inspectionOrderId,
                                 Long sampleId,
                                 SampleLedgerOperationTypeEnum operationType,
                                 String quantityOverride,
                                 String recycleQuantityOverride,
                                 String consumedQuantityOverride,
                                 String remark);

    /**
     * 记录台账事件（可覆盖数量/回收量/消耗量/状态快照）
     */
    void recordEventWithSnapshot(Long inspectionOrderId,
                                 Long sampleId,
                                 SampleLedgerOperationTypeEnum operationType,
                                 String quantityOverride,
                                 String recycleQuantityOverride,
                                 String consumedQuantityOverride,
                                 Integer statusOverride,
                                 String remark);

    void recordEvents(List<SampleLedger> events);

    List<SampleLedger> listByOrderId(Long orderId);

    /**
     * 回收需求变更：在回收时不写本行消耗量，而是更新上一条台账的消耗量
     */
    void updatePreviousConsumption(Long sampleId, String consumedQuantity);
}


