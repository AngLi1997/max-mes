package com.bmos.mes.service.workflow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName WorkFlowStepProgressVO
 * @Description 工步生产进度vo
 * @Author Ren Jin Guang
 * @Date 2024/8/23 16:18
 */
@Setter
@Getter
@ToString
@ApiModel("工步生产进度vo")
public class WorkFlowStepProgressVO {

    @ApiModelProperty("实例id集合")
    private List<String> executionIdList;

    @ApiModelProperty("计划id")
    private Long planId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("任务数据")
    private List<TaskProgressVO> taskProgressList;

    @ApiModelProperty("工步数据")
    private List<StepProgressVO> stepProgressList;
}
