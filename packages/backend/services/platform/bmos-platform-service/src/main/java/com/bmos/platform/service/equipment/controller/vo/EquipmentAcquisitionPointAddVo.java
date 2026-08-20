package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 设备绑定采集点位VO
 *
 * @author yigaohui
 * @date 2024/4/22
 **/
@ApiModel("设备绑定采集点位Vo")
@Data
@Accessors(chain = true)
public class EquipmentAcquisitionPointAddVo {
    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    private Long equipmentId;
    /**
     * 设备编码
     */
    @ApiModelProperty("设备编码")
    private String equipmentCode;
    /**
     * 采集点id
     */
    @ApiModelProperty("采集点位id")
    private Long acquisitionPointId;
    /**
     * 采集点编码
     */
    @ApiModelProperty("采集点位编码")
    private String acquisitionPointCode;
}
