package com.bmos.mes.service.process.vo.Task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel("复制工艺/新增工艺绑定任务节点id以及步骤id实体")
public class TaskOrStepIdHandelVO {

    @ApiModelProperty("旧工序id")
    private Long historyProcedureId;

    @ApiModelProperty("新的工序id")
    private Long newProcedureId;

    @ApiModelProperty("旧步骤id")
    private Long historyStepModelId;

    @ApiModelProperty("新的步骤id")
    private Long newStepModelId;

}
