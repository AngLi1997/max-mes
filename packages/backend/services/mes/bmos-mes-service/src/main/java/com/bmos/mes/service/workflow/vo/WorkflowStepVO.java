package com.bmos.mes.service.workflow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工序步骤执行vo")
public class WorkflowStepVO {

    @ApiModelProperty("是否展示强制完成")
    private Boolean pauseFlag;

    @ApiModelProperty("条件信息")
    private String conditionString;

    @ApiModelProperty("数据")
    private List<WorkflowNodeVO> nodeList;

}
