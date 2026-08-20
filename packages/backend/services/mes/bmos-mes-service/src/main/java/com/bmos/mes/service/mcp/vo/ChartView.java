package com.bmos.mes.service.mcp.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/6/12 11:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChartView<T> {

    /**
     * 视图类型
     * 默认为 "chart"，表示图表视图
     */
    private String viewType = "chart";

    /**
     * 图表类型
     * 例如：line, bar, pie 等
     */
    private String chartType;

    /**
     * 图表标题
     */
    private String chartTitle;

    /**
     * 图表数据
     */
    private T data;

    public ChartView(String chartType, String chartTitle, T data) {
        this.chartType = chartType;
        this.chartTitle = chartTitle;
        this.data = data;
    }
}
