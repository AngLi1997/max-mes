package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ApiModel("模板分类保存DTO")
public class TemplateCategorySaveDTO {

    /**
     * 模板分类名称
     */
    @ApiModelProperty("模板分类名称")
    @NotEmpty
    private String name;

    /**
     * 父级模板分类id
     */
    @ApiModelProperty("父级模板id")
    private Long parentId;

}
