package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设备工位创建DTO
 */
@Getter
@Setter
@ApiModel("创建设备工位入参")
public class StationSaveDTO {

    /**
     * 模型id
     */
    @ApiModelProperty(value = "模型id", required = true)
    @NotNull
    private Long moduleId;

    /**
     * 工位名称
     */
    @ApiModelProperty(value = "工位名称", required = true)
    @NotBlank
    private String name;

    /**
     * 工位编码
     */
    @ApiModelProperty(value = "工位编码", required = true)
    @NotBlank
    private String code;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

}
