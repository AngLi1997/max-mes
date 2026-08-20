package com.bmos.platform.service.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("编辑物料分类DTO")
public class MaterialCategoryUpdateDTO {

    @ApiModelProperty(value = "id",required = true)
    @NotNull
    private Long id;


    @ApiModelProperty(value = "名称",required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "编码",required = true)
    @NotBlank
    private String code;
}
