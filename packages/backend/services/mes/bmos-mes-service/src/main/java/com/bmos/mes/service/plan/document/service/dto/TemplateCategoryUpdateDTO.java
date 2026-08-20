package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 模板分类更新DTO
 */
@Getter
@Setter
@ApiModel(value = "模板分类更新DTO")
public class TemplateCategoryUpdateDTO{

    /**
     * 模板分类id
     */
    @ApiModelProperty("模板分类id")
    @NotNull
    private Long id;

    /**
     * 模板分类名称
     */
    @ApiModelProperty("模板分类名称")
    @NotEmpty
    private String name;

}
