package com.bmos.lims2.web.stability.statistics.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性统计查询检项信息响应VO
 */
@Getter
@Setter
@ApiModel("稳定性统计查询检项信息")
public class StabilityStatisticsItemRespVO {

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

    @ApiModelProperty("数据点名称")
    private String dataPointName;

    @ApiModelProperty("数据点类型")
    private String pointType;

    @ApiModelProperty("文本值")
    private String valueText;

    @ApiModelProperty("数值")
    private BigDecimal valueNumber;

    @ApiModelProperty("检验时间")
    private LocalDate testTime;
}
