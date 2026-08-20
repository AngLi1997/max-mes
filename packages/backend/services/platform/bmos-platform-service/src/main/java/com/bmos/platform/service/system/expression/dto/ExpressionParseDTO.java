package com.bmos.platform.service.system.expression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ApiModel("ExpressionParseDTO:公式解析DTO")
public class ExpressionParseDTO {
    @NotBlank
    @ApiModelProperty(value = "表达式", required = true)
    private String expression;
}
