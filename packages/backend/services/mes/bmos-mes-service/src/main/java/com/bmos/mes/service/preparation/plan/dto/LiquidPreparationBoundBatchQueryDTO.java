package com.bmos.mes.service.preparation.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@ApiModel("配液计划已添加批次查询DTO")
@Builder
@Getter
public class LiquidPreparationBoundBatchQueryDTO {

    @ApiModelProperty("配液计划id")
    @NotNull
    private Long preparationPlanId;

    @ApiModelProperty("配方物料id")
    @NotNull
    private Long formulaMaterialId;

}
