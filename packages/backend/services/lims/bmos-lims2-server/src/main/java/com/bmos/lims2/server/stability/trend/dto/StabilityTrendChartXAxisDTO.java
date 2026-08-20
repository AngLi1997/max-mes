package com.bmos.lims2.server.stability.trend.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性趋势图表X轴数据点
 */
@Getter
@Setter
public class StabilityTrendChartXAxisDTO {

    private Integer timeValue;
    private String timeUnit;
    /** 显示标签，如"0月"、"3月"、"1年" */
    private String label;
}
