package com.bmos.lims2.server.stability.trend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 稳定性趋势图表结果（ECharts结构）
 */
@Getter
@Setter
public class StabilityTrendChartResultDTO {

    /** X轴时间点列表（已排序） */
    private List<StabilityTrendChartXAxisDTO> xAxis;
    /** 系列列表（每个批号一条折线） */
    private List<StabilityTrendChartSeriesDTO> series;
}
