package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("查看工序DTO")
@Data
public class ProcedureViewQueryDTO {

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺版本")
    @NotBlank
    private String processVersion;


    @ApiModelProperty("工序模型id")
    private Long procedureModelId;

}
