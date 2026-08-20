package com.bmos.lims2.web.inspect.pack.vo.resp;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: 实验包完整配置响应VO - 包含检项、分析项、数据点信息
 * @Author: yigaohui
 * @Date: 2025/01/21 15:35
 */
@Data
@ApiModel("实验包完整配置响应VO")
public class InspectPackageFullConfigRespVO {

    @ApiModelProperty("实验包ID")
    private Long packageId;

    @ApiModelProperty("实验包编码")
    private String packageCode;

    @ApiModelProperty("实验包名称")
    private String packageName;

    @ApiModelProperty("实验包描述")
    private String remark;

    @ApiModelProperty("检验项目列表")
    private List<InspectionItemVO> inspectionItems;

    /**
     * 检验项目VO
     */
    @Data
    @ApiModel("检验项目VO")
    public static class InspectionItemVO {
        @ApiModelProperty("检验项目ID")
        private Long inspectItemId;

        @ApiModelProperty("检验项目编码")
        private String inspectItemCode;

        @ApiModelProperty("检验项目名称")
        private String inspectItemName;

        @ApiModelProperty("分析项列表")
        private List<AnalysisItemVO> inspectionParameters;
    }

    /**
     * 分析项VO
     */
    @Data
    @ApiModel("分析项VO")
    public static class AnalysisItemVO {
        @ApiModelProperty("分析项ID")
        private Long parameterId;

        @ApiModelProperty("分析项编码")
        private String parameterCode;

        @ApiModelProperty("分析项名称")
        private String parameterName;

        @ApiModelProperty("标准规定")
        private String standard;


        @ApiModelProperty("数据点列表")
        private List<DataPointVO> dataPoints;
    }

    /**
     * 数据点VO
     */
    @Data
    @ApiModel("数据点VO")
    public static class DataPointVO {
        @ApiModelProperty("原始数据点ID")
        private Long dataPointId;

        @ApiModelProperty("数据点名称")
        private String name;

        @ApiModelProperty("数据点类型：NUMBER-数值, TEXT-文本, OPTION-选项, TIME-时间")
        private DataPointTypeEnum pointType;

        @ApiModelProperty("数据点报告显示")
        private String reportDisplay;

        @ApiModelProperty("时间类型显示格式（仅当pointType为TIME时有效）")
        private String timeFormat;

        private String dateStyle;


        /**
         * 选项配置列表
         */
        @ApiModelProperty(value = "选项配置列表")
        private List<OptionVO> options;

        /**
         * 趋势线配置列表
         */
        @ApiModelProperty(value = "趋势线配置列表")
        private List<TrendVO> trends;
    }

    /**
     * 判定配置VO
     */
    @Data
    @ApiModel("判定配置VO")
    public static class JudgmentVO {
        @ApiModelProperty("判定类型：RANGE-范围判定, EQUAL-相等判定")
        private JudgmentTypeEnum judgmentType;


        @ApiModelProperty("默认测试结果")
        private Boolean defaultResult;

        @ApiModelProperty("最小值")
        private BigDecimal minValue;

        @ApiModelProperty("最大值")
        private BigDecimal maxValue;

        @ApiModelProperty("标准值")
        private String standardValue;

        @ApiModelProperty("判定表达式")
        private String expression;
    }

    @Data
    @ApiModel("选项配置VO")
    public static class OptionVO{


        @ApiModelProperty(value = "数据点id", required = true)
        private Long dataPointId;

        /**
         * 分析项id
         */
        @ApiModelProperty(value = "分析项id", required = true)
        private Long parameterId;

        /**
         * 选项值
         */
        @ApiModelProperty(value = "选项值", required = true)
        private String optionValue;
    }

    @Data
    @ApiModel("趋势线配置VO")
    public static class TrendVO{


        @ApiModelProperty(value = "数据点id", required = true)
        private Long dataPointId;

        /**
         * 分析项id
         */
        @ApiModelProperty(value = "分析项id", required = true)
        @NotNull
        private Long parameterId;

        /**
         * 范围名称
         */
        @ApiModelProperty(value = "范围名称", required = true)
        @NotBlank
        private String rangeName;

        /**
         * 最小值
         */
        @ApiModelProperty(value = "最小值")
        private BigDecimal minValue;

        /**
         * 最小值运算符
         */
        @ApiModelEnumProperty(value = "最小值运算符", enumClass = CompareOperatorEnum.class)
        private CompareOperatorEnum minOperator;

        /**
         * 最大值
         */
        @ApiModelProperty(value = "最大值")
        private BigDecimal maxValue;

        /**
         * 最大值运算符
         */
        @ApiModelEnumProperty(value = "最大值运算符", enumClass = CompareOperatorEnum.class)
        private CompareOperatorEnum maxOperator;
    }
}

