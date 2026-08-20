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
@ApiModel("生产执行完成执行任务DTO")
public class CompleteTaskByExecutionDTO {

    @ApiModelProperty(value = "流程实例id",required = true)
    private String processInstanceId;

    @ApiModelProperty(value = "任务Id",required = true)
    @NotEmpty
    private String executionId;

    @ApiModelProperty(value = "工步模型id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty(value = "生产计划id", required = true)
    @NotNull
    private Long productPlanId;

    @NotNull
    @ApiModelProperty(value = "工艺换班次数", required = true)
    private Integer processChangeNumber;

    @NotNull
    @ApiModelProperty(value = "工序换班次数", required = true)
    private Integer procedureChangeNumber;

    @NotNull
    @ApiModelProperty(value = "流程状态")
    private Integer state;

    @ApiModelProperty(value = "是否强制完成")
    private Boolean isCoerceComplete;
}
