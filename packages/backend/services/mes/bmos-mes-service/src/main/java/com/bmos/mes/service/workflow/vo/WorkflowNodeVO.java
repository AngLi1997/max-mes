package com.bmos.mes.service.workflow.vo;

import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.ProcessStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@ApiModel("执行工序VO")
@Builder
public class WorkflowNodeVO {
    @Tolerate
    public WorkflowNodeVO() {
    }

    @ApiModelProperty("工序名称")
    private String name;

    @ApiModelProperty("工序步骤id")
    private Long procedureStepId;

    @ApiModelProperty("执行实例id")
    private String executionId;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("计划id")
    private Long planId;

    @ApiModelProperty("节点id")
    private String nodeId;

    private Integer state;

    private ProcessStateEnum stateEnum;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("完成时间")
    private LocalDateTime endTime;

    @ApiModelProperty("激活状态")
    private Boolean activeState;

    @ApiModelProperty("工序步骤模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("步骤节点功能")
    private ProcedureStepNodeFunctionEnum nodeFunction;

    @ApiModelProperty("工序排序号")
    private Integer sort;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    public ProcessStateEnum getStateEnum() {
        return ProcessStateEnum.getEnumByValue(state);
    }

}
