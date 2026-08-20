package com.bmos.mes.service.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@ApiModel(value = "流程处理dto")
public class CompleteDTO {

    @ApiModelProperty(value = "实例id")
    @NotBlank
    private String processInstanceId;

    @ApiModelProperty(value = "任务id")
    @NotBlank
    private String taskId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "审批意见")
    private String comment;
}
