package com.bmos.mes.service.workflow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("待办VO")
public class WorkflowFreshTodoPageVO {

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("生产工艺id")
    private Long processId;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("生产工艺版本")
    private String processVersion;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("计划id")
    private Long id;

    @ApiModelProperty("产线名称")
    private String lineName;

    @ApiModelProperty("待办步骤/任务信息")
    private List<WorkflowTodoPageVO> todoPageVOList;



}
