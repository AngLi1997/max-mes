package com.bmos.mes.service.ingredient.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("配料计划:获取可用及已添加批次查询DTO")
@Data
public class IngredientAvailableAndBoundBatchQueryDTO {

    @ApiModelProperty("配料计划id")
    @NotNull
    private Long ingredientPlanId;

    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

    @ApiModelProperty("生产批次id")
    private Long batchId;

}
