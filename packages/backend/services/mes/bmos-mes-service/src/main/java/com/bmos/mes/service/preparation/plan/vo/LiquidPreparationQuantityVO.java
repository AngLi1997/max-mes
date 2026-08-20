package com.bmos.mes.service.preparation.plan.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("配液量列表VO")
public class LiquidPreparationQuantityVO {

    @ApiModelProperty("物料批次id")
    private Long materialBatchId;

    @ApiModelProperty("配液量")
    private BigDecimal preparationQuantity;

    @ApiModelProperty(value = "配置点", hidden = true)
    private BigDecimal configurePoint;


}