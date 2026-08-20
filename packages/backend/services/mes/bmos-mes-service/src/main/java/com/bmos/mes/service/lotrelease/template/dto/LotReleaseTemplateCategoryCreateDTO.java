package com.bmos.mes.service.lotrelease.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 批签发模板分类创建DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 10:46
 */
@ApiModel("批签发模板分类创建DTO")
@Data
public class LotReleaseTemplateCategoryCreateDTO {

    @ApiModelProperty(value = "父级id", example = "1")
    private Long parentId;

    @ApiModelProperty(value = "批签发模板分类名称", example = "批签发模板分类名称")
    @NotBlank
    @Length(max = 100)
    private String name;
}
