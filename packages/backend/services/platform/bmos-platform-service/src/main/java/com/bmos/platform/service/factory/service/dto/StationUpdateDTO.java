package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设备工位修改DTO
 */
@Getter
@Setter
@ApiModel("修改设备工位入参")
public class StationUpdateDTO {

    /**
     * 工位id
     */
    @ApiModelProperty(value = "工位id", required = true)
    @NotNull
    private Long id;


    /**
     * 工位名称
     */
    @ApiModelProperty(value = "工位名称", required = true)
    @NotBlank
    private String name;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

}
