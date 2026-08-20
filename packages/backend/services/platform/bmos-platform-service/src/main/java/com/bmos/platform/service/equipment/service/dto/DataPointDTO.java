package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author yigaohui
 * @date 数据点位DTO
 **/
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("设备数据点位DTO")
public class DataPointDTO {

    @ApiModelProperty("设备id")
    private Long equipmentId;

    @ApiModelProperty("采集项id")
    private Long acquisitionPointId;
}
