package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 获取当前人员绑定的所有设备工位信息入参
 */
@Getter
@Setter
@ApiModel("获取当前设备下的所有设备工位信息入参")
public class EquipmentAppStationDTO {

    @ApiModelProperty("设备id")
    @NotNull
    private Long equipmentId;

}
