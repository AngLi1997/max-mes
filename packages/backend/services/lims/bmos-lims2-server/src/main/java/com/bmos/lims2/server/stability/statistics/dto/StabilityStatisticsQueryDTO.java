package com.bmos.lims2.server.stability.statistics.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性统计查询DTO
 */
@Getter
@Setter
public class StabilityStatisticsQueryDTO {

    @ApiModelProperty(value = "检品ID（必填）", required = true)
    private Long materialId;

    @ApiModelProperty(value = "稳定性考察计划ID（必填）", required = true)
    private Long planId;

    @ApiModelProperty(value = "试验类型（必填）", required = true)
    private String experimentType;

    @ApiModelProperty(value = "储存条件（必填）", required = true)
    private String storageCondition;
}
