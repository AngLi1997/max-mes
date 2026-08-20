package com.bmos.lims2.server.inspect.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("任务-录入人映射对象")
public class TaskOperatorDTO {

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("录入人ID")
    private String operatorId;
}


