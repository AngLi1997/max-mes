package com.bmos.mes.service.preparation.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("配液计划实例查询DTO")
@Data
public class LiquidPreparationPlanInstanceQueryDTO {

    @ApiModelProperty("工步模型id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty("组件id")
    @NotNull
    private Long componentId;

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("复制版本")
    @NotNull
    private Long copyVersion;

}
