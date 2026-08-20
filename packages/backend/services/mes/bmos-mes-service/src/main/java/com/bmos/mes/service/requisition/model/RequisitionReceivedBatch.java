package com.bmos.mes.service.requisition.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 领料计划 待领批次
 */
@Getter
@Setter
@TableName("bm_requisition_received_batch")
public class RequisitionReceivedBatch extends BaseDO {

    /**
     * 领料单id
     */
    private Long requisitionPlanId;

    /**
     * 批次id
     */
    private Long inventoryBatchId;

    /**
     * 货品批次号
     */
    private String inventoryBatchNo;

    /**
     * 该批次总发放物料量(标准单位量)
     */
    private BigDecimal quantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 原厂批号
     */
    private String factoryBatchNo;

    /**
     * 生产日期
     */
    private LocalDate produceDate;

    /**
     * 有效日期
     */
    private LocalDate expiredDate;

    /**
     * 水分(%)
     */
    private BigDecimal hydration;

    /**
     * 无水含量(%)
     */
    private BigDecimal noHydrationContent;

    /**
     * 报告单编号
     */
    private String reportNo;

    /**
     * 放行单编号
     */
    private String licenceNo;

    /**
     * 货品合并编码
     */
    private String cargoMergeCode;

    /**
     * 货品名称
     */
    private String cargoName;

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

}
