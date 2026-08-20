package com.bmos.mes.service.process.dto;

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
@ApiModel("查询工序步骤记录项VO")
public class ProcedureStepRecordItemQueryDTO {

    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long processId;

    @ApiModelProperty(value = "工艺版本号",required = true)
    @NotEmpty
    private String processVersion;

    @ApiModelProperty(value = "工序步骤流程节点",required = true)
    @NotEmpty
    private String nodeId;

    @ApiModelProperty(value = "生产计划id", required = true)
    @NotNull
    private Long productPlanId;
}
