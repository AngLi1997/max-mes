package com.bmos.mes.service.ingredient.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("配料单绑定物料批次DTO")
@Data
public class IngredientBindMaterialBatchDTO {

    @ApiModelProperty("配料计划id")
    private Long ingredientPlanId;

    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    @ApiModelProperty("批次列表")
    private List<BindMaterialBatchDTO> materialBatchList;

}
