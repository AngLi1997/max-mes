package com.bmos.lims2.web.inspect.scheme.vo.response;

import com.bmos.lims2.common.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验方案版本完整配置响应VO（用于复制和编辑）
 *
 * @author makejava
 * @since 2025-01-28 10:00:00
 */
@Data
@ApiModel("检验方案版本完整配置响应")
public class InspectionSchemeVersionFullConfigRespVO {

    // === 版本基础信息 ===
    @ApiModelProperty("版本ID")
    private Long id;

    @ApiModelProperty("关联的检验方案ID")
    private Long schemeId;

    @ApiModelProperty("方案编码")
    private String schemeCode;

    @ApiModelProperty("方案名称")
    private String schemeName;

    /**
     * 方案描述
     */
    @ApiModelProperty("方案描述")
    private String description;

    /**
     * 生效日期
     */
    @ApiModelProperty("生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;


    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("版本状态")
    private InspectionSchemeVersionStatusEnum status;

    @ApiModelProperty("父版本ID")
    private Long parentVersionId;

    @ApiModelProperty("父版本号")
    private String parentVersionNo;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人ID")
    private Long createBy;

    @ApiModelProperty("创建人姓名")
    private String createByName;

    // === 物料和实验包信息 ===
    @ApiModelProperty("物料信息")
    private MaterialInfoRespVO material;

    @ApiModelProperty("实验包信息")
    private PackageInfoRespVO packageInfo;

    // === 检验项目配置 ===
    @ApiModelProperty("检验项目配置列表")
    private List<InspectionItemConfigRespVO> inspectionItems;

    // === 取样配置 ===
    @ApiModelProperty("取样配置列表")
    private List<SamplingConfigRespVO> samplingConfigs;

    /**
     * 物料信息响应VO
     */
    @Data
    @ApiModel("物料信息")
    public static class MaterialInfoRespVO {
        @ApiModelProperty("物料ID")
        private Long materialId;

        @ApiModelProperty("物料名称")
        private String materialName;

        @ApiModelProperty("物料编码")
        private String materialCode;

        @ApiModelProperty("物料单位ID")
        private Long unitId;

        @ApiModelProperty("物料单位名称")
        private String unitName;
    }

    /**
     * 实验包信息响应VO
     */
    @Data
    @ApiModel("实验包信息")
    public static class PackageInfoRespVO {
        @ApiModelProperty("实验包ID")
        private Long packageId;

        @ApiModelProperty("实验包名称")
        private String packageName;

        @ApiModelProperty("实验包编码")
        private String packageCode;
    }

    /**
     * 检验项目配置响应VO
     */
    @Data
    @ApiModel("检验项目配置")
    public static class InspectionItemConfigRespVO {

        @ApiModelProperty("检验项目配置ID")
        private Long itemConfigId;

        /**
         * 关联的方案ID
         */
        @ApiModelProperty("关联的方案ID")
        private Long schemeId;

        /**
         * 关联的版本ID
         */
        @ApiModelProperty("关联的版本ID")
        private Long versionId;

        /**
         * 实验包id
         */
        @ApiModelProperty("实验包ID")
        private Long packageId;

        /**
         * 检验项目ID
         */
        @ApiModelProperty("检验项目ID")
        private Long inspectItemId;

        @ApiModelProperty("检验项目名称")
        private String inspectItemName;

        @ApiModelProperty("检验项目编码")
        private String inspectItemCode;

        /**
         * 是否必检
         */
        @ApiModelProperty("是否必检")
        private Boolean isRequired;

        /**
         * 排序
         */
        @ApiModelProperty("排序")
        private Integer sort;

        /**
         * 备注
         */
        @ApiModelProperty("备注")
        private String remark;


        @ApiModelProperty("时长")
        private Integer duration;

        @ApiModelProperty("时长单位")
        private ItemDurationUnitEnum timeUnit;

        @ApiModelProperty("班组id")
        private List<Long> teams;

        @ApiModelProperty("分析项配置列表")
        private List<AnalyzeItemConfigRespVO> inspectionParameters;
    }

    /**
     * 分析项配置响应VO
     */
    @Data
    @ApiModel("分析项配置")
    public static class AnalyzeItemConfigRespVO {
        @ApiModelProperty("分析项配置ID（修改时需要）")
        private Long parameterConfigId;

        /**
         * 关联的方案ID
         */
        @ApiModelProperty("关联的方案ID")
        private Long schemeId;

        /**
         * 关联的版本ID
         */
        @ApiModelProperty("关联的版本ID")
        private Long versionId;

        @ApiModelProperty("关联的实验包ID")
        private Long packageId;


        @ApiModelProperty("关联的检验项目ID")
        private Long inspectItemId;

        /**
         * 关联的检验项目配置ID
         */
        @ApiModelProperty("关联的检验项目配置ID")
        private Long itemConfigId;

        /**
         * 分析项ID
         */
        @ApiModelProperty("分析项ID")
        private Long parameterId;

        @ApiModelProperty("分析项名称")
        private String parameterName;

        @ApiModelProperty("分析项编码")
        private String parameterCode;
        /**
         * 标准规定
         */
        @ApiModelProperty("标准规定")
        private String standardRule;

        /**
         * 分析方法ID
         */
        @ApiModelProperty("分析方法ID")
        private Long recordId;

        @ApiModelProperty("分析方法版本ID")
        private Long recordVersionId;

        @ApiModelProperty("分析方法名称")
        private String recordName;

        @ApiModelProperty("分析方法编码")
        private String recordCode;

        @ApiModelProperty("版本号")
        private String recordVersion;

        @ApiModelProperty("记录项ID（bm_batch_record_item.id）")
        private Long recordItemId;

        /**
         * 是否报告项
         */
        @ApiModelProperty("是否报告项")
        private Boolean isReportable;

        /**
         * 是否可执行
         */
        @ApiModelProperty("是否可执行")
        private Boolean isExecutable;

        /**
         * 最终表达式
         */
        @ApiModelProperty("最终表达式")
        private String finalExpression;

        @ApiModelProperty("执行方式：LIMS/ELN")
        private ExecuteMethodEnum executeMethod;

        @ApiModelProperty("数据点配置列表")
        private List<DataPointConfigRespVO> dataPoints;

        @ApiModelProperty("判定配置列表")
        private List<JudgmentConfigRespVO> judgments;

        @ApiModelProperty("判定条件引用的数据点配置是否存在删除（true=存在问题）")
        private Boolean judgmentConfigError;

        @ApiModelProperty("判定引用的数据点是否存在删除（分析项聚合标识）")
        private Boolean judgmentDataPointDeleted;

        @ApiModelProperty("判定引用的数据点与记录组件绑定是否缺失（分析项聚合标识）")
        private Boolean judgmentDataPointBindingMissing;

        @ApiModelProperty("判定引用的数据点类型变更（分析项聚合标识）")
        private Boolean judgmentDataPointTypeChanged;

        @ApiModelProperty("判定引用的选项未在组件中配置（分析项聚合标识）")
        private Boolean judgmentDataPointOptionInvalid;
    }

    /**
     * 数据点配置响应VO
     */
    @Data
    @ApiModel("数据点配置")
    public static class DataPointConfigRespVO {
        @ApiModelProperty("数据点配置ID（修改时需要）")
        private Long dataPointConfigId;

        @ApiModelProperty("原始数据点ID")
        private Long dataPointId;

        @ApiModelProperty("分析项配置ID")
        private Long parameterConfigId;

        @ApiModelProperty("分析项id")
        private Long parameterId;

        /**
         * 关联的方案ID
         */
        @ApiModelProperty("关联的方案ID")
        private Long schemeId;

        /**
         * 关联的版本ID
         */
        @ApiModelProperty("关联的版本ID")
        private Long versionId;

        @ApiModelProperty("关联的实验包ID")
        private Long packageId;


        @ApiModelProperty("检验项目ID")
        private Long inspectItemId;

        @ApiModelProperty("数据点名称")
        private String name;

        @ApiModelProperty("数据点类型")
        private DataPointTypeEnum pointType;

        @ApiModelProperty("趋势线配置JSON")
        private String trendLineConfig;

        @ApiModelProperty("选项配置JSON")
        private String options;

        @ApiModelProperty("是否报告显示")
        private Boolean reportDisplay;

        @ApiModelProperty("时间类型显示格式（仅当pointType为TIME时有效）")
        private String timeFormat;


        private String dateStyle;

        @ApiModelProperty("时间类型舍入：true向上，false向下")
        private Boolean roundingUp;

        @ApiModelProperty("是否被结论判定引用")
        private Boolean referencedByJudgment;

        @ApiModelProperty("数据点关联的记录ID")
        private Long recordId;

        @ApiModelProperty("数据点关联的记录版本ID")
        private Long recordVersionId;

        @ApiModelProperty("数据点关联的字段ID")
        private Long fieldId;

        @ApiModelProperty("数据点关联的记录项ID")
        private Long recordItemId;

        @ApiModelProperty("数据点关联的组件ID")
        private Long componentId;
    }

    /**
     * 判定配置响应VO
     */
    @Data
    @ApiModel("判定配置")
    public static class JudgmentConfigRespVO {

        @ApiModelProperty("判定配置ID（修改时需要）")
        private Long judgmentConfigId;

        @ApiModelProperty("判定配置名称")
        private String judgementConfigName;

        /**
         * 关联的方案ID
         */
        @ApiModelProperty("关联的方案ID")
        private Long schemeId;

        /**
         * 关联的版本ID
         */
        @ApiModelProperty("关联的版本ID")
        private Long versionId;


        @ApiModelProperty("关联的实验包ID")
        private Long packageId;

        @ApiModelProperty("关联的检验项目ID")
        private Long inspectItemId;

        @ApiModelProperty("分析项ID")
        private Long parameterId;
        /**
         * 关联的分析项配置ID
         */
        @ApiModelProperty("关联的分析项配置ID")
        private Long parameterConfigId;

        @ApiModelProperty("关联的判定配置ID")
        private Long dataPointConfigId;

        /**
         * 关联的数据点ID
         */
        @ApiModelProperty("关联的数据点ID")
        private Long dataPointId;

        @ApiModelProperty("判定引用的选项是否未在组件中配置")
        private Boolean dataPointOptionInvalid;

        /**
         * 判定类型：RANGE-范围判定, EQUAL-相等判定
         */
        @ApiModelProperty("判定类型")
        private JudgmentTypeEnum judgmentType;

        @ApiModelProperty("判定引用的数据点是否已删除")
        private Boolean dataPointDeleted;

        @ApiModelProperty("判定引用的数据点与记录组件绑定是否缺失")
        private Boolean dataPointBindingMissing;

        @ApiModelProperty("判定引用的数据点类型是否已变更")
        private Boolean dataPointTypeChanged;

        @ApiModelProperty("默认测试结果")
        private Boolean defaultResult;

        /**
         * 最小值
         */
        @ApiModelProperty("最小值")
        private BigDecimal minValue;

        /**
         * 最大值
         */
        @ApiModelProperty("最大值")
        private BigDecimal maxValue;

        /**
         * 标准值
         */
        @ApiModelProperty("标准值")
        private String standardValue;

        /**
         * 判定表达式
         */
        @ApiModelProperty("判定表达式")
        private String expression;

        /**
         * 最小值比较运算符
         */
        private CompareOperatorEnum minOperator;


        /**
         * 最大值比较运算符
         */
        private CompareOperatorEnum maxOperator;



        private String minTime;


        private String maxTime;
    }

    /**
     * 取样配置响应VO
     */
    @Data
    @ApiModel("取样配置")
    public static class SamplingConfigRespVO {

        @ApiModelProperty("取样配置ID（修改时需要）")
        private Long samplingConfigId;

        @ApiModelProperty("检验项目ID")
        private Long inspectItemId;

        @ApiModelProperty("检验项目名称")
        private String inspectItemName;

        @ApiModelProperty("检验项目编码")
        private String inspectItemCode;

        @ApiModelProperty("取样量")
        private String samplingAmount;

        @ApiModelProperty("取样单位")
        private String samplingUnit;

        @ApiModelProperty("取样份数")
        private Integer samplingCount;
    }
} 