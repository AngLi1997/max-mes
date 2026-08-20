package com.bmos.lims2.server.stability.trend.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 稳定性趋势查询导出VO
 */
@Data
@ApiModel("稳定性趋势查询导出VO")
public class StabilityTrendExportVO {

    @ExcelProperty("批号")
    @ApiModelProperty("批号")
    private String batchNo;

    @ExcelProperty("时间点")
    @ApiModelProperty("时间点（如 3月、6月）")
    private String timePoint;

    @ExcelProperty("数值")
    @ApiModelProperty("数值")
    private BigDecimal valueNumber;

    @ExcelProperty("文本值")
    @ApiModelProperty("文本值")
    private String valueText;
}
