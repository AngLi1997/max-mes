package com.bmos.lims2.web.inspect.scheme.vo.request;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;

import com.bmos.lims2.server.inspect.scheme.validate.InspectionSchemeNameUnique;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

/**
 * 检验方案保存请求VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案保存请求")
public class InspectionSchemeSaveReqVO {



    @ApiModelProperty(value = "方案名称", required = true)
    @NotBlank(message = "方案名称不能为空")
    @InspectionSchemeNameUnique
    private String name;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty(value = "版本号", required = true)
    @NotBlank(message = "版本号不能为空")
    private String versionNo;

    @ApiModelProperty("父版本ID（基于现有版本创建时必填）")
    private Long parentVersionId;

    @ApiModelProperty("物料信息")
    @Valid
    @NotNull(message = "物料信息不能为空")
    private MaterialInfoReqVO material;

    @ApiModelProperty("实验包信息")
    @Valid
    @NotNull(message = "实验包信息不能为空")
    private PackageInfoReqVO packageInfo;

    @ApiModelProperty("检验项目配置列表")
    @Valid
    @NotEmpty(message = "检验项目配置不能为空")
    private List<InspectionItemConfigReqVO> inspectionItems;

    @ApiModelProperty("取样配置列表")
    @Valid
    private List<SamplingConfigReqVO> samplingConfigs;

    /**
     * 物料信息请求VO
     */
    @Data
    @ApiModel("物料信息")
    public static class MaterialInfoReqVO {

        @ApiModelProperty(value = "物料ID", required = true)
        @NotNull(message = "物料ID不能为空")
        private Long materialId;

        @ApiModelProperty("物料名称")
        private String materialName;

        @ApiModelProperty("物料编码")
        private String materialCode;
    }

    /**
     * 实验包信息请求VO
     */
    @Data
    @ApiModel("实验包信息")
    public static class PackageInfoReqVO {

        @ApiModelProperty(value = "实验包ID", required = true)
        @NotNull(message = "实验包ID不能为空")
        private Long packageId;

        @ApiModelProperty("实验包名称")
        private String packageName;

        @ApiModelProperty("实验包编码")
        private String packageCode;
    }

    /**
     * 检验项目配置请求VO
     */
    @Data
    @ApiModel("检验项目配置")
    public static class InspectionItemConfigReqVO {

        @ApiModelProperty(value = "检验项目ID", required = true)
        @NotNull(message = "检验项目ID不能为空")
        private Long inspectItemId;

        @ApiModelProperty("是否必需")
        private Boolean isRequired;

        @ApiModelProperty("排序")
        private Integer sort;

        @ApiModelProperty("执行时长")
        private Integer duration;

        @ApiModelProperty("执行时长单位")
        private ItemDurationUnitEnum timeUnit;

        @ApiModelProperty("执行班组")
        private List<Long> teams;

        @ApiModelProperty("分析项配置列表")
        @Valid
        private List<AnalyzeItemConfigReqVO> inspectionParameters;
    }

    /**
     * 分析项配置请求VO
     */
    @Data
    @ApiModel("分析项配置")
    public static class AnalyzeItemConfigReqVO {

        @ApiModelProperty(value = "分析项ID", required = true)
        @NotNull(message = "分析项ID不能为空")
        private Long parameterId;

        @ApiModelProperty("分析方法ID")
        private Long recordId;

        @ApiModelProperty("标准规定")
        private String standardRule;

        @ApiModelProperty("是否报告项")
        private Boolean isReportable;

        @ApiModelProperty("是否可执行")
        private Boolean isExecutable;

        @ApiModelProperty("数据点配置列表")
        @Valid
        private List<DataPointConfigReqVO> dataPoints;

        /**
         * 最终判定表达式（由前端传入）
         */
        @ApiModelProperty("最终判定表达式")
        private String finalExpression;

        @ApiModelProperty("判定配置列表")
        @Valid
        private List<JudgmentConfigReqVO> judgments;

        @ApiModelProperty("执行方式：LIMS/ELN")
        private ExecuteMethodEnum executeMethod;
    }

    /**
     * 数据点配置请求VO
     */
    @Data
    @ApiModel("数据点配置")
    public static class DataPointConfigReqVO {

        @ApiModelProperty("原始数据点ID")
        private Long dataPointId;

        @ApiModelProperty(value = "数据点名称", required = true)
        @NotBlank(message = "数据点名称不能为空")
        private String name;

        @ApiModelProperty(value = "数据点类型", required = true)
        @NotNull(message = "数据点类型不能为空")
        private DataPointTypeEnum pointType;

        @ApiModelProperty("趋势线配置JSON")
        private String trendLineConfig;

        @ApiModelProperty("选项配置JSON")
        private String options;

        @ApiModelProperty("时间类型显示格式（仅当pointType为TIME时有效）")
        private String timeFormat;

        private String dateStyle;

        @ApiModelProperty("是否报告显示")
        private Boolean reportDisplay;
    }

    /**
     * 判定配置请求VO
     */
    @Data
    @ApiModel("判定配置")
    public static class JudgmentConfigReqVO {

        @ApiModelProperty("判定类型")
        private JudgmentTypeEnum judgmentType;


        @ApiModelProperty("默认测试结果")
        private Boolean defaultResult;

        @ApiModelProperty("数据点ID")
        private Long dataPointId;

        @ApiModelProperty("最小值")
        private BigDecimal minValue;

        @ApiModelProperty("最大值")
        private BigDecimal maxValue;

        @ApiModelProperty("标准值")
        private String standardValue;

        @ApiModelProperty("判定表达式")
        private String expression;
    }

    /**
     * 取样配置请求VO
     */
    @Data
    @ApiModel("取样配置")
    public static class SamplingConfigReqVO {

        @ApiModelProperty("检验项目ID，为NULL时表示整体取样")
        private Long inspectItemId;

        @ApiModelProperty(value = "取样量", required = true)
        @NotNull(message = "取样量不能为空")
        private String samplingAmount;

        @ApiModelProperty(value = "取样单位", required = true)
        @NotBlank(message = "取样单位不能为空")
        private String samplingUnit;

        @ApiModelProperty(value = "取样份数", required = true)
        @NotNull(message = "取样份数不能为空")
        @Positive(message = "取样份数必须大于0")
        private Integer samplingCount;
    }
} 