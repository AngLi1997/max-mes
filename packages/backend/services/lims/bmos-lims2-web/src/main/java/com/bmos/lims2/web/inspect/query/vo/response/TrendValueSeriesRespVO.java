package com.bmos.lims2.web.inspect.query.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 趋势查询结果VO
 * @Author: yigaohui
 * @Date: 2025/09/05 12:30
 */
@Getter
@Setter
@ApiModel("趋势查询结果VO")
public class TrendValueSeriesRespVO {

    @ApiModelProperty("x轴：检验批号列表")
    private List<String> xAxisBatchNos;

    @ApiModelProperty("趋势点列表")
    private List<Point> points;

    @ApiModelProperty("趋势线配置")
    private List<com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointTrendDTO> trendLines;

    @Getter
    @Setter
    @ApiModel("趋势点")
    public static class Point {
        private Long inspectionOrderId;
        private String inspectionOrderNo;
        private String batchNo;
        private LocalDateTime requestTime;
        private BigDecimal valueNumber;
    }
}


