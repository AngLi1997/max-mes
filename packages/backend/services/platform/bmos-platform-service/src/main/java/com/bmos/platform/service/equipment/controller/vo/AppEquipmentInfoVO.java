package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel("app端打印机设备返回vo")
public class AppEquipmentInfoVO {

    @ApiModelProperty("设备id")
    private Long id;

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("设备编号")
    private String code;
}
