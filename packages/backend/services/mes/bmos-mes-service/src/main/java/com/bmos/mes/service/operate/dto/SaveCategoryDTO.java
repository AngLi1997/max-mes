package com.bmos.mes.service.operate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@ApiModel(value = "新增分类dto")
public class SaveCategoryDTO {

    @ApiModelProperty(value = "上级分类id")
    @NotNull
    private Long parentId;

    @ApiModelProperty(value = "分类名称")
    @NotBlank
    private String name;
}
