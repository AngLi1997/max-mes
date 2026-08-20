package com.bmos.mes.service.ingredient.plan.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel("配料量列表批量计算")
@Data
public class IngredientQuantityListCalculateVO {

    @ApiModelProperty("已选理论量")
    private BigDecimal chosenTheoreticalQuantity;

    @ApiModelProperty("配料总量")
    private BigDecimal ingredientTotalQuantity;

    @ApiModelProperty("配料量列表VO")
    private List<IngredientQuantityListVO> ingredientQuantityList;

}
