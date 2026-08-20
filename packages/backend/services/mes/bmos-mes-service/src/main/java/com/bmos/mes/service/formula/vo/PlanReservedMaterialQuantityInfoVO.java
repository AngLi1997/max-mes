package com.bmos.mes.service.formula.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("生产批次已预定物料量信息VO")
@Data
public class PlanReservedMaterialQuantityInfoVO {

    @ApiModelProperty("已预定暂存量(理论量)")
    private BigDecimal reservedQuantity;

    @ApiModelProperty("计划应校验批量")
    private BigDecimal planNeedQuantity;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

}
