package com.bmos.lims2.server.stability.statistics.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性统计查询结果项DTO（检项信息）
 */
@Getter
@Setter
public class StabilityStatisticsItemDTO {

    @ApiModelProperty("稳定性考察编号")
    private String planCode;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("试验类型")
    private String experimentType;

    @ApiModelProperty("储存条件")
    private String storageCondition;

    @ApiModelProperty("时间点数值")
    private Integer timeValue;

    @ApiModelProperty("时间单位（DAY/MONTH/YEAR）")
    private String timeUnit;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("分析项编码")
    private String parameterCode;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("分析项ID")
    private Long parameterId;

    @ApiModelProperty("方案版本ID")
    private Long schemeVersionId;

    @ApiModelProperty("数据点ID")
    private Long dataPointId;

    @ApiModelProperty("数据点名称")
    private String dataPointName;

    @ApiModelProperty("数据点类型")
    private String pointType;

    @ApiModelProperty("文本值")
    private String valueText;

    @ApiModelProperty("数值")
    private java.math.BigDecimal valueNumber;

    @ApiModelProperty("检验时间")
    private LocalDate testTime;
}
