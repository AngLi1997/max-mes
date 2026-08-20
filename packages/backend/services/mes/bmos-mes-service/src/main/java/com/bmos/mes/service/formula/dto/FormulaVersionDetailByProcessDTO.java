package com.bmos.mes.service.formula.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("根据工艺版本查询配方详情DTO")
@Data
public class FormulaVersionDetailByProcessDTO {

    @ApiModelProperty(value = "工艺id", required = true)
    @NotNull
    private Long processId;

    @ApiModelProperty(value = "工艺版本", required = true)
    @NotEmpty
    private String processVersion;

}
