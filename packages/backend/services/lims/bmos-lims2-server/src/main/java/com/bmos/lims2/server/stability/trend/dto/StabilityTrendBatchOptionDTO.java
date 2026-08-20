package com.bmos.lims2.server.stability.trend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性趋势查询批号下拉选项DTO
 */
@Getter
@Setter
public class StabilityTrendBatchOptionDTO {

    @ApiModelProperty("批号")
    private String batchNo;
}
