package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备信息VO
 */
@Getter
@Setter
@ApiModel("设备信息VO")
public class EquipmentPrintInfoVO {

    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    private Long equipmentId;

    /**
     * 设备Code
     */
    @ApiModelProperty("设备Code")
    private String equipmentCode;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;

    /**
     * 打印日期
     */
    @ApiModelProperty("打印日期")
    private String printDate;

}
