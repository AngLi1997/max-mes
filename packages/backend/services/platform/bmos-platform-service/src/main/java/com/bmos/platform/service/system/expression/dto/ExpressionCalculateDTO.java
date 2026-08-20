package com.bmos.platform.service.system.expression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel("表达式计算DTO")
@Data
public class ExpressionCalculateDTO {

    @ApiModelProperty("表达式")
    @NotBlank
    private String expression;

    @ApiModelProperty("修约方式code")
    private String roundingCode;

    @ApiModelProperty("精度")
    private Integer scale;

    @ApiModelProperty("表达式键值")
    private List<kvDTO> keyValueList;

    @Data
    public static class kvDTO{
        private String key;
        private String value;
    }

}
