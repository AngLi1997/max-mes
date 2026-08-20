package com.bmos.mes.service.ingredient.plan.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("配料量列表VO")
@Data
public class IngredientQuantityListVO {

    @ApiModelProperty("物料批次id")
    private Long materialBatchId;

    @ApiModelProperty("配料量")
    private BigDecimal ingredientQuantity;

    @ApiModelProperty("理论量")
    private BigDecimal theoreticalQuantity;


}
