package com.bmos.mes.service.workflow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("工序重做DTO")
public class WorkflowRestartDTO {

    @ApiModelProperty("执行实例id")
    @NotEmpty
    private String executionId;

    @ApiModelProperty("工序模型id")
    @NotNull
    private Long procedureModelId;

    @ApiModelProperty("计划id")
    @NotNull
    private Long planId;

    @ApiModelProperty("工序状态，4:已完成")
    @NotNull
    private Integer state;

    @ApiModelProperty("工序换班次数")
    @NotNull
    private Integer procedureChangeNumber;

    @ApiModelProperty("工艺换班次数")
    @NotNull
    private Integer processChangeNumber;

}
