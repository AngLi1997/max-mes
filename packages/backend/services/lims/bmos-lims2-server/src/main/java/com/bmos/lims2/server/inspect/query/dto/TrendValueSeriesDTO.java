package com.bmos.lims2.server.inspect.query.dto;

import com.bmos.lims2.server.inspect.entry.dto.TrendValuePointDTO;

import java.util.List;

/**
 * @Description: 趋势查询-结果DTO
 * @Author: yigaohui
 * @Date: 2025/09/05 11:35
 */
public class TrendValueSeriesDTO {

    private List<String> xAxisBatchNos;
    private List<TrendValuePointDTO> points;
    private List<com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointTrendDTO> trendLines;

    public List<String> getXAxisBatchNos() { return xAxisBatchNos; }
    public void setXAxisBatchNos(List<String> xAxisBatchNos) { this.xAxisBatchNos = xAxisBatchNos; }
    public List<TrendValuePointDTO> getPoints() { return points; }
    public void setPoints(List<TrendValuePointDTO> points) { this.points = points; }
    public List<com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointTrendDTO> getTrendLines() { return trendLines; }
    public void setTrendLines(List<com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointTrendDTO> trendLines) { this.trendLines = trendLines; }
}


