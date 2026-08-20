package com.bmos.mes.service.workflow.vo;

import com.bmos.mes.service.plan.info.vo.PlanPageVO;
import com.bmos.mybatis.page.CommonPage;
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
public class WorkFlowToDoVO {

    @ApiModelProperty("生产前确认数据")
    private List<PlanPageVO> planStartList;

    @ApiModelProperty("待办数据")
    private CommonPage<WorkflowFreshTodoPageVO> freshTodoVo;

    @ApiModelProperty("当前待办数据数量")
    private Integer presentTodoCount;

    @ApiModelProperty("计划待办数据数量")
    private Integer futureTodoCount;

    @ApiModelProperty("待办总量")
    private Integer count;

    public Integer getCount(){
        return presentTodoCount + futureTodoCount;
    }



}
