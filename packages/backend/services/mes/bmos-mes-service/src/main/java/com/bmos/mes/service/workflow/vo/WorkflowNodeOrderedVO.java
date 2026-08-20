package com.bmos.mes.service.workflow.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("执行工序已排序VO")
public class WorkflowNodeOrderedVO {

    private List<WorkflowNodeVO> running;

    private List<WorkflowNodeVO> completed;

    private List<WorkflowNodeVO> notActive;
}
