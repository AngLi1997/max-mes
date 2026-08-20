package com.bmos.mes.service.lotrelease.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 批签发模板分类修改DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 10:46
 */
@Data
@ApiModel("批签发模板分类修改DTO")
public class LotReleaseTemplateCategoryEditDTO {

    @ApiModelProperty(value = "id", example = "1")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "批签发模板分类名称", example = "批签发模板分类名称")
    @NotBlank
    @Length(max = 100)
    private String name;
}
