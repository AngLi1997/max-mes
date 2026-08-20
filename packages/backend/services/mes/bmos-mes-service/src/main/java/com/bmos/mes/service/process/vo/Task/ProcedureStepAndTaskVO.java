package com.bmos.mes.service.process.vo.Task;

import com.bmos.mes.service.process.vo.ProcedureStepVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@ApiModel("查询工序步骤/任务集合vo")
public class ProcedureStepAndTaskVO {

    @ApiModelProperty("步骤详情")
    private List<ProcedureStepVO> stepList;

    @ApiModelProperty("任务详情")
    private List<ProcedureStepVO> taskList;
}
