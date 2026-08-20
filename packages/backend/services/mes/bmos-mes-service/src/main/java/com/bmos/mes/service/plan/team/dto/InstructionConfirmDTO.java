package com.bmos.mes.service.plan.team.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("指令单批量确认DTO")
public class InstructionConfirmDTO {

    @ApiModelProperty("指令单id")
    @NotNull
    private Long instructionId;

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

}
