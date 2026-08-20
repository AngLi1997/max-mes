package com.bmos.mes.service.workflow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "待办数量返回vo")
public class WorkFlowTodoCountVO {

    @ApiModelProperty("工作流待办")
    private Integer flowTotal;

    @ApiModelProperty("任务待办数量")
    private Integer taskTotal;

    @ApiModelProperty("总量")
    private Integer count;
}
