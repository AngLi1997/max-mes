package com.bmos.platform.service.system.expression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("ExpressionCategoryUpdateDTO:公式分类更新DTO")
public class ExpressionCategoryUpdateDTO {
    @NotNull
    @ApiModelProperty(value = "父级id", required = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    @ApiModelProperty(value = "父级id", required = true)
    private Long parentId;
}
