package com.bmos.lims2.server.stability.trend.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 稳定性趋势查询数据项DTO（平铺行）
 */
@Getter
@Setter
public class StabilityTrendDataItemDTO {

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("时间点数值")
    private Integer timeValue;

    @ApiModelProperty("时间单位（DAY/MONTH/YEAR）")
    private String timeUnit;

    @ApiModelProperty("数值")
    private BigDecimal valueNumber;

    @ApiModelProperty("文本值")
    private String valueText;
}
