package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 新增设备标签的状态入参DTO
 */
@Getter
@Setter
@ApiModel("设备标签下的设备状态入参")
public class EquipmentStatusDTO {

    /**
     * 设备状态code
     */
    @ApiModelProperty(value = "设备状态code", notes = "此为内置，具体设备状态可以直接由此来进行判断")
    private String code;

    /**
     * 设备状态名称
     */
    @ApiModelProperty("设备状态名称")
    private String name;

    /**
     * 是否内置
     */
    @ApiModelProperty("是否内置")
    private Boolean embed;

    /**
     * 属性值/默认效期
     */
    @ApiModelProperty("属性值/默认效期")
    private String value;

    /**
     * 新增时其默认设备状态
     */
    @ApiModelProperty(value = "新增时其默认设备状态", required = true)
    private Boolean finishStatus;

}
