package com.bmos.platform.service.system.expression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ApiModel("ExpressionCategorySaveDTO:公式分类保存DTO")
public class ExpressionCategorySaveDTO {
    @NotBlank
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty("父级id")
    private Long parentId;
}
