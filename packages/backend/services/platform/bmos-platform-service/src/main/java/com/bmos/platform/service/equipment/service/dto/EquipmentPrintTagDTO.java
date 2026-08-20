package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("设备标签打印接口")
public class EquipmentPrintTagDTO {

    /**
     * 设备标签打印
     */
    private Long equipmentId;

}
