package com.bmos.mes.service.process.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(value = "添加步骤实例表dto")
public class SaveStepModelInstanceDTO {

    @ApiModelProperty("计划id")
    private Long planId;

    @ApiModelProperty("工步模型id")
    private Long stepModelId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("完成状态：true已完成")
    private Boolean completeState;


}
