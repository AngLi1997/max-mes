package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工步模型列表VO")
public class ProcedureStepModelListVO {

    @ApiModelProperty("工步模型id")
    private Long id;

    @ApiModelProperty("工序id")
    private Long procedureId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

    @ApiModelProperty("工步id")
    private Long procedureStepId;

    @ApiModelProperty("工步名")
    private String name;

}
