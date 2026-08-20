package com.bmos.lims2.server.stability.trend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 稳定性趋势图表系列（一个批号对应一条折线）
 */
@Getter
@Setter
public class StabilityTrendChartSeriesDTO {

    private String batchNo;
    /** 与X轴对齐的数据值列表，无数据的时间点为null */
    private List<String> data;
}
