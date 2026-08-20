package com.bmos.lims2.web.stability.trend.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("稳定性趋势图表结果（ECharts结构）")
public class StabilityTrendChartResultRespVO {

    @ApiModelProperty("X轴时间点列表（已排序）")
    private List<XAxisItem> xAxis;

    @ApiModelProperty("系列列表（每个批号一条折线）")
    private List<SeriesItem> series;

    @Getter
    @Setter
    public static class XAxisItem {
        @ApiModelProperty("时间点数值")
        private Integer timeValue;
        @ApiModelProperty("时间单位（DAY/MONTH/YEAR）")
        private String timeUnit;
        @ApiModelProperty("显示标签，如\"0月\"、\"3月\"、\"1年\"")
        private String label;
    }

    @Getter
    @Setter
    public static class SeriesItem {
        @ApiModelProperty("批号")
        private String batchNo;
        @ApiModelProperty("与X轴对齐的数据值列表，无数据的时间点为null")
        private List<String> data;
    }
}
