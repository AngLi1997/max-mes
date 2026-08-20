package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("步骤班次查询列表")
@Data
public class ProcedureStepChangeNumberQueryDTO {

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("工序步骤模型id")
    @NotNull
    private Long procedureStepModelId;

}
