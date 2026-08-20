package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("修改设备分类入参")
public class CategoryUpdateDTO {

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id", required = true)
    @NotNull
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", required = true)
    @NotBlank
    private String name;

}
