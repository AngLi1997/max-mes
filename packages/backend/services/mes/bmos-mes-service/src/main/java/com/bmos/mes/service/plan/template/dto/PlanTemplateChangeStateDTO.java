package com.bmos.mes.service.plan.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("生产计划模板状态更改DTO")
@Data
public class PlanTemplateChangeStateDTO {

    @ApiModelProperty("生产计划模板id")
    @NotNull
    private Long id;

    @ApiModelProperty("修改状态")
    @NotNull
    private Boolean state;

}
