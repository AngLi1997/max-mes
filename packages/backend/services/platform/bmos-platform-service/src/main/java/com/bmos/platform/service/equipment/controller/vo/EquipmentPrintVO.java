package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 获取打印机设备
 */
@Getter
@Setter
@ApiModel(value = "获取打印机设备")
public class EquipmentPrintVO {

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Long id;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String name;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ip;

    /**
     * 端口
     */
    @ApiModelProperty(value = "端口")
    private String port;

    /**
     * dpi
     */
    @ApiModelProperty(value = "dpi")
    private String dpi;
}
