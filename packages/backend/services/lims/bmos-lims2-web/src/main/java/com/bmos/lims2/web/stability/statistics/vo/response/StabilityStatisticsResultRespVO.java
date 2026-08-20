package com.bmos.lims2.web.stability.statistics.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ApiModel("稳定性统计查询结果")
public class StabilityStatisticsResultRespVO {

    @ApiModelProperty("表头信息")
    private HeaderInfo header;

    @ApiModelProperty("数据行")
    private List<DataRow> data;

    @Getter
    @Setter
    public static class HeaderInfo {
        @ApiModelProperty("稳定性方案ID")
        private Long schemeId;
        @ApiModelProperty("物料名称")
        private String materialName;
        @ApiModelProperty("物料编码")
        private String materialCode;
        @ApiModelProperty("规格")
        private String materialSpec;
        @ApiModelProperty("试验类型")
        private String experimentType;
        @ApiModelProperty("试验类型名称")
        private String experimentTypeName;
        @ApiModelProperty("存储条件")
        private String storageCondition;
        @ApiModelProperty("方案名称")
        private String schemeName;
        @ApiModelProperty("分析项列表")
        private List<ParameterHeader> parameters;
    }

    @Getter
    @Setter
    public static class ParameterHeader {
        @ApiModelProperty("分析项ID")
        private Long parameterId;
        @ApiModelProperty("方案版本ID")
        private Long schemeVersionId;
        @ApiModelProperty("分析项编码")
        private String parameterCode;
        @ApiModelProperty("分析项名称")
        private String parameterName;
        @ApiModelProperty("数据点列表")
        private List<DataPointHeader> dataPoints;
    }

    @Getter
    @Setter
    public static class DataPointHeader {
        @ApiModelProperty("数据点ID")
        private Long dataPointId;
        @ApiModelProperty("数据点名称")
        private String dataPointName;
        @ApiModelProperty("数据点类型")
        private String pointType;
    }

    @Getter
    @Setter
    public static class DataRow {
        @ApiModelProperty("批号")
        private String batchNo;
        @ApiModelProperty("时间点行列表")
        private List<TimepointRow> rows;
    }

    @Getter
    @Setter
    public static class TimepointRow {
        @ApiModelProperty("贮存时间数值（如 0、3、6）")
        private Integer timeValue;
        @ApiModelProperty("贮存时间单位（如 MONTH、DAY、YEAR）")
        private String timeUnit;
        @ApiModelProperty("数据点值映射(dataPointId -> value)")
        private java.util.Map<Long, String> dataPointValues;
    }
}
