package com.bmos.mes.service.weigh.centre2.execute.controller.vo;

import com.bmos.mes.common.enums.formula.ToleranceTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("工单余料信息VO")
public class TicketOddmentInfoVO {
    /**
     * 工单id
     */
    @ApiModelProperty("工单id")
    private Long ticketId;

    @ApiModelProperty("物料合并编码")
    private String materialMergeCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料批号")
    private String storageMaterialBatchNo;

    @ApiModelProperty("添加物料总量")
    private BigDecimal ticketQuality;

    @ApiModelProperty("目标量")
    private BigDecimal requirementQuantity;

    @ApiModelProperty("整个工单剩余的物料总量")
    private BigDecimal remainingQuality;

    @ApiModelProperty("已称量量")
    private BigDecimal weighedQuantity;

    @ApiModelProperty("未称量量")
    private BigDecimal notWeighQuality;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("配料允差类型")
    private ToleranceTypeEnum chargeMixtureToleranceType;

    @ApiModelProperty("配料允差上限")
    private BigDecimal chargeMixtureToleranceUpper;

    @ApiModelProperty("配料允差下限")
    private BigDecimal chargeMixtureToleranceLower;

    @ApiModelProperty("称量需求内物料件的上限")
    private BigDecimal chargeUpperQuality;

    @ApiModelProperty("称量需求内物料件的下限")
    private BigDecimal chargeLowerQuality;

    /**
     * 未称量的允差上限
     */
    @ApiModelProperty("未称量的允差上限")
    private BigDecimal notWeighToleranceUpper;
    /**
     * 未称量的允差下限
     */
    @ApiModelProperty("未称量的允差下限")
    private BigDecimal notWeighToleranceLower;

    @ApiModelProperty("称量人id")
    private String weighUserId;
} 