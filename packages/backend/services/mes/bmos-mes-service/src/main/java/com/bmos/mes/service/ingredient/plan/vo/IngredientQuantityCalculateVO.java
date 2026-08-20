package com.bmos.mes.service.ingredient.plan.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("配料量计算VO")
@Data
public class IngredientQuantityCalculateVO {

    @ApiModelProperty("配料量")
    private BigDecimal ingredientQuantity;

    @ApiModelProperty("理论量(配料量对应)")
    private BigDecimal theoreticalQuantity;

}
