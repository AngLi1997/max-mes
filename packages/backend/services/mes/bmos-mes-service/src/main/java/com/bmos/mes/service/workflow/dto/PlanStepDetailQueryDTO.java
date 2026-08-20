package com.bmos.mes.service.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("生产计划工步详情查询DTO")
@Data
public class PlanStepDetailQueryDTO {

    @ApiModelProperty("工步模型id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty("生产计划(生产指令单)id")
    @NotNull
    private Long productPlanId;

}
