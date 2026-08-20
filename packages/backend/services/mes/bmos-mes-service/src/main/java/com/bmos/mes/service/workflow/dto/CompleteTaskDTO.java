package com.bmos.mes.service.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("生产执行完成任务DTO")
public class CompleteTaskDTO {

    @ApiModelProperty(value = "流程实例id",required = true)
    private String processInstanceId;

    @ApiModelProperty(value = "任务Id",required = true)
    @NotEmpty
    private String taskId;

    @ApiModelProperty(value = "工步模型id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty(value = "生产计划id", required = true)
    @NotNull
    private Long productPlanId;

    @NotNull
    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @NotNull
    @ApiModelProperty("工步换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("是否强制完成,当为true时将不会判断完成条件")
    private Boolean isCoerceComplete;
}
