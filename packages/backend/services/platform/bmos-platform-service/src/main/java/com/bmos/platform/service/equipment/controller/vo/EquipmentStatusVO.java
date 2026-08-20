package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备标签的状态VO
 */
@Getter
@Setter
@ApiModel("设备标签下的设备状态入参")
public class EquipmentStatusVO {

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
     * 是否必填
     */
    @ApiModelProperty("是否必填")
    private Boolean required;

    @ApiModelProperty("完成状态")
    private Boolean finishStatus;

    /**
     * 有效期
     */
    private LocalDateTime expireDateTime;

}
