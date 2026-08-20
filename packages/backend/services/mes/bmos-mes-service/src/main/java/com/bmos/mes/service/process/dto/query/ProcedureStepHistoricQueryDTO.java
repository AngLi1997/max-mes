package com.bmos.mes.service.process.dto.query;

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
@ApiModel("历史工序步骤查询DTO")
public class ProcedureStepHistoricQueryDTO {

    @ApiModelProperty(value = "工序id",required = true)
    @NotNull
    private Long procedureId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty(value = "工序模型id",required = true)
    @NotNull
    private Long procedureModelId;
}
