package com.bmos.platform.service.system.expression.dto;

import com.bmos.expression.pojo.KeyValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("ExpressionUpdateDTO:公式更新DTO")
public class ExpressionUpdateDTO {
    @NotNull
    @ApiModelProperty(value = "id", required = true)
    private Long id;
    @NotBlank
    @ApiModelProperty(value = "名称", required = true)
    private String name;
    @NotBlank
    @ApiModelProperty(value = "计算结果", required = true)
    private String result;
    @NotBlank
    @ApiModelProperty(value = "公式表达式", required = true)
    private String expression;

    @NotEmpty
    @ApiModelProperty(value = "公式表达式解析结果", required = true)
    private List<KeyValue<String, String>> expressionParse;
}
