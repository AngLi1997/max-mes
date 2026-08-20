package com.bmos.mes.service.execute.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("执行工步VO")
@Data
public class ProcedureStepViewVO {

    @ApiModelProperty("工步名称")
    private String procedureStepName;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("工步id")
    private Long procedureStepId;

    @ApiModelProperty("记录项id")
    private Long recordItemId;

    @ApiModelProperty("记录版本id")
    private Long recordVersionId;

    @ApiModelProperty("节点id")
    private String nodeId;

}
