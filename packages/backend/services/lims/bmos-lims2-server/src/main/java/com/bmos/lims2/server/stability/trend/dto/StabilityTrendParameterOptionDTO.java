package com.bmos.lims2.server.stability.trend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性趋势查询分析项下拉选项DTO
 */
@Getter
@Setter
public class StabilityTrendParameterOptionDTO {

    @ApiModelProperty("分析项ID")
    private Long id;

    @ApiModelProperty("分析项编码")
    private String code;

    @ApiModelProperty("分析项名称")
    private String name;
}
