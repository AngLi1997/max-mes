package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 设备故障入参
 */
@Getter
@Setter
@ApiModel("设备故障入参")
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentOperateDTO {

    @ApiModelProperty(value = "设备id", required = true)
    @NotNull
    private Long id;

}
