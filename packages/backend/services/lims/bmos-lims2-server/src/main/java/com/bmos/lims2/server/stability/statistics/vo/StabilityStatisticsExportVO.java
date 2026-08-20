package com.bmos.lims2.server.stability.statistics.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性统计查询导出VO
 */
@Data
@ApiModel("稳定性统计查询导出VO")
public class StabilityStatisticsExportVO {

    @ExcelProperty("稳定性考察编号")
    @ApiModelProperty("稳定性考察编号")
    private String planCode;

    @ExcelProperty("批号")
    @ApiModelProperty("批号")
    private String batchNo;

    @ExcelProperty("检验单号")
    @ApiModelProperty("检验单号")
    private String orderNo;

    @ExcelProperty("请验时间")
    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ExcelProperty("试验类型")
    @ApiModelProperty("试验类型")
    private String experimentType;

    @ExcelProperty("储存条件")
    @ApiModelProperty("储存条件")
    private String storageCondition;

    @ExcelProperty("时间点")
    @ApiModelProperty("时间点（如 3M、6M）")
    private String timePoint;

    @ExcelProperty("检验项目编码")
    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ExcelProperty("检验项目名称")
    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ExcelProperty("分析项编码")
    @ApiModelProperty("分析项编码")
    private String parameterCode;

    @ExcelProperty("分析项名称")
    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ExcelProperty("数据点名称")
    @ApiModelProperty("数据点名称")
    private String dataPointName;

    @ExcelProperty("文本值")
    @ApiModelProperty("文本值")
    private String valueText;

    @ExcelProperty("数值")
    @ApiModelProperty("数值")
    private BigDecimal valueNumber;

    @ExcelProperty("检验时间")
    @ApiModelProperty("检验时间")
    private LocalDate testTime;
}
