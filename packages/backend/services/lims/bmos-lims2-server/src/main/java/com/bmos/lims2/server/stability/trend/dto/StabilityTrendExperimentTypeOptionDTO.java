package com.bmos.lims2.server.stability.trend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性趋势查询试验类型下拉选项DTO
 */
@Getter
@Setter
public class StabilityTrendExperimentTypeOptionDTO {

    @ApiModelProperty("试验类型")
    private String experimentType;

    @ApiModelProperty("试验类型名称")
    private String experimentTypeName;

    @ApiModelProperty("储存条件")
    private String storageCondition;
}
