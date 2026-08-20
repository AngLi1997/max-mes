package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@ApiModel("组件执行设备列表查询DTO")
public class ExecuteEquipmentQueryDTO {

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("组件id")
    @NotNull
    private Long componentId;

    @ApiModelProperty("工步模型id")
    @NotNull
    private Long procedureStepModelId;

}
