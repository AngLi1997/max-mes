package com.bmos.mes.service.process.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("工序步骤组件配置集合查询")
public class ProcedureStepConfigListQueryDTO {

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺版本")
    @NotEmpty
    private String processVersion;

    @ApiModelProperty("是否可复用")
    @NotNull
    private Boolean reusable;

    @ApiModelProperty("工序步骤id")
    @NotNull
    private Long procedureStepId;

    @ApiModelProperty("工序步骤模型id")
    private Long procedureStepModelId;
}
