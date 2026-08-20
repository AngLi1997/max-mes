package com.bmos.lims2.server.stability.trend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性趋势查询DTO
 */
@Getter
@Setter
public class StabilityTrendQueryDTO {

    @ApiModelProperty(value = "稳定性方案版本ID（必填）", required = true)
    private Long versionId;

    @ApiModelProperty(value = "试验类型（必填）", required = true)
    private String experimentType;

    @ApiModelProperty(value = "储存条件（必填）", required = true)
    private String storageCondition;

    @ApiModelProperty(value = "分析项ID（必填）", required = true)
    private Long parameterId;

    @ApiModelProperty(value = "数据点名称（必填）", required = true)
    private String dataPointName;

    @ApiModelProperty("批号（可选）")
    private String batchNo;
}
