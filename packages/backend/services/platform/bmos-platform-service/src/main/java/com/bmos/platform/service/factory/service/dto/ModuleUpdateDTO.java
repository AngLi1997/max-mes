package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("修改设备工厂模型入参")
public class ModuleUpdateDTO {

    /**
     * 模型id
     */
    @ApiModelProperty(value = "模型id", required = true)
    @NotNull
    private Long id;

    /**
     * 模型名称
     */
    @ApiModelProperty(value = "模型名称", required = true)
    @NotBlank
    private String name;

}
