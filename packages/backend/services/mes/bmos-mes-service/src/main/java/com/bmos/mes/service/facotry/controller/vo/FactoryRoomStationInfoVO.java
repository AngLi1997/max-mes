package com.bmos.mes.service.facotry.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 房间vo
 */
@Getter
@Setter
@ApiModel(value = "房间vo")
public class FactoryRoomStationInfoVO {

    /**
     * 房间id
     */
    @ApiModelProperty(value = "房间id")
    private Long id;

    /**
     * 房间名称
     */
    @ApiModelProperty(value = "房间名称")
    private String name;

    /**
     * 房间code
     */
    @ApiModelProperty(value = "房间code")
    private String code;

    /**
     * 工位信息
     */
    @ApiModelProperty(value = "工位信息")
    private List<FactoryStationVO> stationVOList;

}
