package com.bmos.mes.service.plan.instruction.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("InstructionUpdateDTO:生产计划指令单更新DTO")
public class InstructionUpdateDTO {
    @NotNull
    @ApiModelProperty("指令单id")
    private Long id;

    @NotNull
    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @NotNull
    @ApiModelProperty("负责人")
    private Long principal;
}
