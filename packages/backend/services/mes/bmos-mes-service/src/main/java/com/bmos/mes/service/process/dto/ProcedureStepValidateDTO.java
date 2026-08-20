package com.bmos.mes.service.process.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("工序步骤校验dto")
public class ProcedureStepValidateDTO {

    @ApiModelProperty("工序id")
    @NotNull
    private Long procedureId;

    @ApiModelProperty("名称")
    @NotBlank
    private String name;

}
