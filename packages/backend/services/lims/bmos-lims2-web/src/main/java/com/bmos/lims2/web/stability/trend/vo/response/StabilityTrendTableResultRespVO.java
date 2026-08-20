package com.bmos.lims2.web.stability.trend.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("稳定性趋势查询表格结果")
public class StabilityTrendTableResultRespVO {

    @ApiModelProperty("基础信息")
    private BasicInfo basicInfo;

    @ApiModelProperty("时间点列（从方案配置获取）")
    private List<TimepointColumn> timepointColumns;

    @ApiModelProperty("数据行列表（每批次一行）")
    private List<DataRow> dataRows;

    @Getter
    @Setter
    public static class BasicInfo {
        @ApiModelProperty("检品名称")
        private String materialName;
        @ApiModelProperty("检品编码")
        private String materialCode;
        @ApiModelProperty("检品规格")
        private String materialSpec;
        @ApiModelProperty("试验类型编码")
        private String experimentType;
        @ApiModelProperty("试验类型名称")
        private String experimentTypeName;
        @ApiModelProperty("储存条件")
        private String storageCondition;
        @ApiModelProperty("分析项名称")
        private String parameterName;
        @ApiModelProperty("数据点ID")
        private Long dataPointId;
        @ApiModelProperty("数据点名称")
        private String dataPointName;
        @ApiModelProperty("数据点类型")
        private String pointType;
    }

    @Getter
    @Setter
    public static class TimepointColumn {
        @ApiModelProperty("时间点数值")
        private Integer timeValue;
        @ApiModelProperty("时间单位（DAY/MONTH/YEAR）")
        private String timeUnit;
        @ApiModelProperty("显示标签，如 0月、3月")
        private String label;
    }

    @Getter
    @Setter
    public static class DataRow {
        @ApiModelProperty("批号")
        private String batchNo;
        @ApiModelProperty("各时间点数据")
        private List<TimepointValue> timepointValues;
    }

    @Getter
    @Setter
    public static class TimepointValue {
        @ApiModelProperty("时间点数值")
        private Integer timeValue;
        @ApiModelProperty("时间单位（DAY/MONTH/YEAR）")
        private String timeUnit;
        @ApiModelProperty("检测值")
        private String value;
    }
}
