package com.bmos.lims2.server.eln.record.dto;

import com.bmos.lims2.server.eln.record.entity.formula.ComponentFormulaConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("公式计算预览DTO")
public class FunctionCalculatePreviewDTO {

    @ApiModelProperty("输入值")
    @NotBlank
    private String input;

    @ApiModelProperty("公式计算参数")
    @NotNull
    private ComponentFormulaConfig formulaConfig;

    @ApiModelProperty("公式枚举value")
    @NotBlank
    private String functionValue;

}
