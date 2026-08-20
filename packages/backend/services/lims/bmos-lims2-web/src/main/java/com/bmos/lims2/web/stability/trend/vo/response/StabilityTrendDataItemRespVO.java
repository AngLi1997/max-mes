package com.bmos.lims2.web.stability.trend.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 稳定性趋势查询数据项响应VO
 */
@Getter
@Setter
@ApiModel("稳定性趋势查询数据项")
public class StabilityTrendDataItemRespVO {

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
