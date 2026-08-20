package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设备模型VO
 */
@Getter
@Setter
@ApiModel("设备模型VO")
public class ModuleVO {

    /**
     * 模型id
     */
    @ApiModelProperty("模型id")
    private Long id;

    /**
     * 父级id 若没有父级则为0
     */
    @ApiModelProperty(value = "父级模型名称 没有则为null")
    private Long parentName;

    /**
     * 模型编码
     */
    @ApiModelProperty(value = "模型编码")
    private String code;

    /**
     * 模型名称
     */
    @ApiModelProperty(value = "模型编码")
    private String name;

    /**
     * 模型类型
     */
    @ApiModelProperty(value = "模型类型")
    private Integer type;

}
