package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 房间绑定工位入参
 */
@Getter
@Setter
@ApiModel("房间绑定工位入参")
public class RoomBindStationDTO {

    /**
     * 房间id
     */
    @ApiModelProperty("房间id")
    @NotNull
    private Long id;

    /**
     * 工位id列表
     */
    @ApiModelProperty("工位id列表")
    @NotNull
    private List<Long> stationIdList;
}
