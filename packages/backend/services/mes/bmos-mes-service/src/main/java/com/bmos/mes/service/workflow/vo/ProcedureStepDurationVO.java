package com.bmos.mes.service.workflow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("工序与工步执行时长")
@Data
public class ProcedureStepDurationVO {

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("工步id")
    private Long procedureStepId;

    @ApiModelProperty("工步nodeId")
    private String procedureStepNodeId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工序nodeId")
    private String procedureNodeId;

    @ApiModelProperty("工序执行时间")
    private Long procedureDuration;

    @ApiModelProperty("工序执行时间单位")
    private String procedureTimeUnit;

    @ApiModelProperty("工序步骤执行时间")
    private Long procedureStepDuration;

    @ApiModelProperty("工序步骤执行时间单位")
    private String procedureStepTimeUnit;

    @ApiModelProperty("工序名称")
    private String procedureModelName;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺版本")
    private String processVersion;

    @ApiModelProperty("节点类型")
    private String nodeFunction;

    @ApiModelProperty("工步排序号")
    private Integer sort;

    @ApiModelProperty("工序基础表id")
    private Long procedureId;

}
