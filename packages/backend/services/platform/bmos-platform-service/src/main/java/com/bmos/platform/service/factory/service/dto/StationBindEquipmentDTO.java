package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 工位绑定设备入参
 */
@Getter
@Setter
@ApiModel("工位绑定设备入参")
public class StationBindEquipmentDTO {

    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    @NotNull
    private Long stationId;

    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    @NotEmpty
    private List<Long> equipmentId;

}
