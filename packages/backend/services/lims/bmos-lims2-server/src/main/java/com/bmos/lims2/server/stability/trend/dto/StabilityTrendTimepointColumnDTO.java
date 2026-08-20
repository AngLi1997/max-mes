package com.bmos.lims2.server.stability.trend.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性趋势查询时间点列
 */
@Getter
@Setter
public class StabilityTrendTimepointColumnDTO {

    private Integer timeValue;
    private String timeUnit;
    private String label;
}
