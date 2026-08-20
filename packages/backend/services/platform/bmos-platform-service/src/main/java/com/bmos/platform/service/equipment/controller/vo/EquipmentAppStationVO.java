package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("工位返回结果VO")
public class EquipmentAppStationVO {

    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    private Long stationId;

    @ApiModelProperty("工位code")
    private String stationCode;

    /**
     * 工位名称
     */
    @ApiModelProperty("工位名称")
    private String name;

}
