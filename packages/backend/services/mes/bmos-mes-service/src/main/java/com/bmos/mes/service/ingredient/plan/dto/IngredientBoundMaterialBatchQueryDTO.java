package com.bmos.mes.service.ingredient.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("配料单已绑定批次查询DTO")
public class IngredientBoundMaterialBatchQueryDTO {

    @ApiModelProperty("配料计划id")
    @NotNull
    private Long ingredientPlanId;

    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

}
