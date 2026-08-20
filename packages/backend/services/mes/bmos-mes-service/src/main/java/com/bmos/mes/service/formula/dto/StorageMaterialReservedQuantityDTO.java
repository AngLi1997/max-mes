package com.bmos.mes.service.formula.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("生产批次预定暂存量查询DTO")
@Data
public class StorageMaterialReservedQuantityDTO {

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    @ApiModelProperty("校验量")
    private BigDecimal checkQuantity;

}
