package com.bmos.mes.service.weigh.centre2.execute.controller.vo;

import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class WeighRequirementVO {
    /**
     * 需求ID
     */
    private Long id;
    /**
     * 需求量
     */
    private BigDecimal requirementQuantity;
    /**
     * 单位id
     */
    private Long unitId;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 物料合并编码
     */
    private String materialMergeCode;
    /**
     * 物料批次id
     */
    private Long storageMaterialBatchId;
    /**
     * 物料批次编号
     */
    private String storageMaterialBatchNo;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 产品编码
     */
    private String productMaterialMergeCode;
    /**
     * 产品名称
     */
    private String productMaterialName;
    /**
     * 生产批号
     */
    private String batchNo;
    /**
     * 生产计划时间
     */
    private LocalDate planDate;
    /**
     * 需求状态
     */
    private RequirementStatusEnum requirementStatus;
    /**
     * 需求已称量的量
     */
    private BigDecimal weighedQuantity;
    /**
     * 配料允差类型
     */
    private ToleranceTypeEnum chargeMixtureToleranceType;
    /**
     * 配料允差上限
     */
    private BigDecimal chargeMixtureToleranceUpper;
    /**
     * 配料允差下限
     */
    private BigDecimal chargeMixtureToleranceLower;
    /**
     * 称量需求内物料件的上限
     */
    private BigDecimal chargeUpperQuality;
    /**
     * 称量需求内物料件的下限
     */
    private BigDecimal chargeLowerQuality;
    /**
     * 未称量的允差上限
     */
    private BigDecimal notWeighToleranceUpper;
    /**
     * 未称量的允差下限
     */
    private BigDecimal notWeighToleranceLower;
    /**
     * 称量需求内物料件的总量
     */
    private BigDecimal quality;

    /**
     * 未称量的量
     */
    private BigDecimal notWeighQuality;

    /**
     * 整个工单添加的物料总量
     */
    private BigDecimal ticketQuality;

    /**
     * 整个工单剩余的物料总量
     */
    private BigDecimal remainingQuality;

    /**
     * 称量需求内物料件的数量
     */
    private Long storageMaterialCount;
    /**
     * 需求用途
     */
    private String requirementUsage;
    /**
     * 备注
     */
    private String remark;
    /**
     * 称量人id
     */
    private String weighUserId;

    /**
     * 需求完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 是否最后一个完成的需求
     */
    private Boolean lastFlg;
} 