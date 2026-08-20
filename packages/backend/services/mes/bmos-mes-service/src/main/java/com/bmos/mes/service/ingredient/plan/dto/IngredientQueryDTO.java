package com.bmos.mes.service.ingredient.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@ApiModel("配料计划详情查询DTO")
public class IngredientQueryDTO {

    @ApiModelProperty("工序步骤模型id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty("组件id")
    @NotNull
    private Long componentId;

    @ApiModelProperty("组件标识id")
    @NotNull
    private Long fieldId;

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("复制版本")
    private Long copyVersion;

}
