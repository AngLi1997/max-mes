package com.bmos.platform.service.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@ApiModel("保存物料分类DTO")
public class MaterialCategorySaveDTO {

    @ApiModelProperty(value = "名称",required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "编码",required = true)
    @NotBlank
    private String code;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty(value = "业务注册")
    private boolean businessRegister;

    @ApiModelProperty("业务名称")
    private String businessName;
}
