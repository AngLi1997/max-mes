package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 产线绑定工位DTO
 */
@Getter
@Setter
@ApiModel("产线绑定工位DTO")
public class LineBindStationDTO {

    @ApiModelProperty("产线ID")
    private Long id;

    @ApiModelProperty("工位id集合")
    private List<Long> stationIdList;
}
