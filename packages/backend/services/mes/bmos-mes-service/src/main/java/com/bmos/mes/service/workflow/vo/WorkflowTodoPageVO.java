package com.bmos.mes.service.workflow.vo;

import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@ApiModel("待办VO")
public class WorkflowTodoPageVO {

    @ApiModelProperty("工序节点名称")
    private String procedureName;

    @ApiModelProperty("工步节点名称")
    private String procedureStepName;

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("产品Id")
    private Long productId;

    @ApiModelProperty("工序步骤id")
    private Long procedureStepId;

    @ApiModelProperty("工序步骤模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("计划编号")
    private String planNo;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty("包装规格")
    private String packingSpecification;

    @ApiModelProperty("生产工艺id")
    private Long processId;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("生产工艺版本")
    private String processVersion;


    @ApiModelProperty("生产计划id")
    private Long planId;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("生产计划是否已暂停")
    private Boolean executePaused;

    @ApiModelProperty("工序执行时间")
    private Long procedureDuration;

    @ApiModelProperty("工序执行时间单位")
    private String procedureTimeUnit;

    @ApiModelProperty("工序步骤执行时间")
    private Long procedureStepDuration;

    @ApiModelProperty("工序步骤执行时间单位")
    private String procedureStepTimeUnit;

    @ApiModelProperty("激活状态,true:已激活；false:未激活")
    private Boolean activeState;

    @ApiModelProperty("任务实例id")
    private String executionId;

    @ApiModelProperty("工序待办数量")
    private Integer procedureChangeNumber;

    @ApiModelProperty("工艺待办数量")
    private Integer processChangeNumber;

    @ApiModelProperty("节点功能")
    private ProcedureStepNodeFunctionEnum nodeFunction;

    @ApiModelProperty("排序号")
    private Integer sort;

    @ApiModelProperty("工序基础表id")
    private Long procedureId;

    @ApiModelProperty("产线id")
    private Long productionLineId;

    @ApiModelProperty("计划详情id")
    private Long productionPlanItemId;

    @ApiModelProperty("工序计划开始时长")
    private LocalDate procedureStartTime;
}
