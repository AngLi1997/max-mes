package com.bmos.lims2.server.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("检品分类更新VO")
public class MaterialCategoryUpdateDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "分类名称", required = true)
    @Length(max = 30)
    @NotBlank
    private String name;

}
