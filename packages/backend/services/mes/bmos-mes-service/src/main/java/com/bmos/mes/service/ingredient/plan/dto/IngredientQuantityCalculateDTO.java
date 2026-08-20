package com.bmos.mes.service.ingredient.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("配料量批量计算DTO")
@Data
public class IngredientQuantityCalculateDTO {

    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

    @ApiModelProperty("配料计划id")
    @NotNull
    private Long ingredientPlanId;

    @ApiModelProperty("批次id列表")
    @NotEmpty
    private List<Long> materialBatchIdList;

}
