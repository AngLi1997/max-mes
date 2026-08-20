package com.bmos.lims2.server.inspect.pack.dto;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
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
 * @Description: 实验包完整配置DTO - 包含检项、分析项、数据点信息
 * @Author: yigaohui
 * @Date: 2025/01/21 15:30
 */
@Data
public class InspectPackageFullConfigDTO {

    /**
     * 实验包ID
     */
    private Long packageId;

    /**
     * 实验包编码
     */
    private String packageCode;

    /**
     * 实验包名称
     */
    private String packageName;

    /**
     * 实验包描述
     */
    private String remark;

    /**
     * 检验项目列表
     */
    private List<InspectionItemDTO> inspectionItems;

    /**
     * 检验项目DTO
     */
    @Data
    public static class InspectionItemDTO {
        /**
         * 检验项目ID
         */
        private Long inspectItemId;

        /**
         * 检验项目编码
         */
        private String inspectItemCode;

        /**
         * 检验项目名称
         */
        private String inspectItemName;

        /**
         * 是否必检
         */
        private Boolean isRequired;

        /**
         * 排序
         */
        private Integer sort;

        /**
         * 备注
         */
        private String remark;

        /**
         * 分析项列表
         */
        private List<AnalysisItemDTO> inspectionParameters;
    }

    /**
     * 分析项DTO
     */
    @Data
    public static class AnalysisItemDTO {
        /**
         * 分析项ID
         */
        private Long parameterId;

        /**
         * 分析项编码
         */
        private String parameterCode;

        /**
         * 分析项名称
         */
        private String parameterName;

        /**
         * 标准规定
         */
        private String standard;

        /**
         * 是否报告项
         */
        private Boolean isReportable;

        /**
         * 是否可执行
         */
        private Boolean isExecutable;

        /**
         * 数据点列表
         */
        private List<DataPointDTO> dataPoints;
    }

    /**
     * 数据点DTO
     */
    @Data
    public static class DataPointDTO {
        /**
         * 原始数据点ID
         */
        private Long dataPointId;

        /**
         * 数据点名称
         */
        private String name;

        /**
         * 数据点类型：NUMBER-数值, TEXT-文本, OPTION-选项, TIME-时间
         */
        private DataPointTypeEnum pointType;

        /**
         * 选项配置列表
         */
        @ApiModelProperty(value = "选项配置列表")
        private List<OptionDTO> options;

        /**
         * 时间类型显示格式（仅当pointType为TIME时有效）
         */
        private String timeFormat;
        private String dateStyle;

        /**
         * 时间类型时长舍入方式：true-向上；false-向下
         */
        private Boolean roundingUp;


        /**
         * 趋势线配置列表
         */
        @ApiModelProperty(value = "趋势线配置列表")
        private List<TrendDTO> trends;

        /**
         * 是否报告显示
         */
        private Boolean reportDisplay;

        /**
         * 最终判定表达式
         */
        private String finalExpression;
    }

    @Data
    @ApiModel("选项配置VO")
    public static class OptionDTO{

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
    public static class TrendDTO{


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

