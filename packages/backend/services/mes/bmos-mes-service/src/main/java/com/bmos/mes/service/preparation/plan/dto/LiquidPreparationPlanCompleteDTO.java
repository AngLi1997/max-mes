package com.bmos.mes.service.preparation.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("完成配液计划DTO")
public class LiquidPreparationPlanCompleteDTO {

    @ApiModelProperty("配液单id")
    @NotNull
    private Long preparationPlanId;

    @ApiModelProperty("计划人id")
    @NotNull
    private Long userId;


}
