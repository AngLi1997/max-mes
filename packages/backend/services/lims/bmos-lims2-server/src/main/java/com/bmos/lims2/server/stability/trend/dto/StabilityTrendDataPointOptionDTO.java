package com.bmos.lims2.server.stability.trend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性趋势查询数据点下拉选项DTO
 */
@Getter
@Setter
public class StabilityTrendDataPointOptionDTO {

    @ApiModelProperty("数据点ID")
    private Long id;

    @ApiModelProperty("数据点名称")
    private String name;
}
