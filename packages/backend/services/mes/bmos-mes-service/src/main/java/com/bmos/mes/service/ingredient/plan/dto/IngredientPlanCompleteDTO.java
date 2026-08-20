package com.bmos.mes.service.ingredient.plan.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@ApiModel("完成配料计划DTO")
public class IngredientPlanCompleteDTO extends BusinessDataHandleBaseDTO {

    @ApiModelProperty("领料单id")
    @NotNull
    private Long ingredientPlanId;

    @ApiModelProperty("计划人id")
    private Long userId;
}
