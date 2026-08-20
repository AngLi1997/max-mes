package com.bmos.lims2.server.inspect.scheme.dto.response;

import com.bmos.lims2.common.enums.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验方案版本完整配置响应DTO（用于复制和编辑）
 *
 * @author makejava
 * @since 2025-01-28 10:00:00
 */
@Data
public class InspectionSchemeVersionFullConfigDTO {

    // === 版本基础信息 ===
    /**
     * 版本ID
     */
    private Long id;

    /**
     * 关联的检验方案ID
     */
    private Long schemeId;

    /**
     * 方案编码
     */
    private String schemeCode;

    /**
     * 方案名称
     */
    private String schemeName;

    /**
     * 方案描述
     */
    private String description;

    /**
     * 生效日期
     */
    private LocalDate effectiveDate;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 版本状态：EDITING-编辑中, APPROVING-审批中, ACTIVE-生效, INACTIVE-失效
     */
    private InspectionSchemeVersionStatusEnum status;

    /**
     * 父版本ID
     */
    private Long parentVersionId;

    /**
     * 父版本号
     */
    private String parentVersionNo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建人姓名
     */
    private String createByName;

    // === 物料和实验包信息 ===
    /**
     * 物料信息
     */
    private MaterialInfoDTO material;

    /**
     * 实验包信息
     */
    private PackageInfoDTO packageInfo;

    // === 检验项目配置 ===
    /**
     * 检验项目配置列表
     */
    private List<InspectionItemConfigDTO> inspectionItems;

    // === 取样配置 ===
    /**
     * 取样配置列表
     */
    private List<SamplingConfigDTO> samplingConfigs;

    /**
     * 物料信息DTO
     */
    @Data
    public static class MaterialInfoDTO {
        private Long materialId;
        private String materialName;
        private String materialCode;
        /**
         * 物料单位ID
         */
        private Long unitId;
        /**
         * 物料单位名称
         */
        private String unitName;
    }

    /**
     * 实验包信息DTO
     */
    @Data
    public static class PackageInfoDTO {
        private Long packageId;
        private String packageName;
        private String packageCode;
    }

    /**
     * 检验项目配置DTO
     */
    @Data
    public static class InspectionItemConfigDTO {
        private Long itemConfigId;
        /**
         * 关联的方案ID
         */
        private Long schemeId;

        /**
         * 关联的版本ID
         */
        private Long versionId;

        /**
         * 实验包id
         */
        private Long packageId;

        /**
         * 检验项目ID
         */
        private Long inspectItemId;

        @ApiModelProperty("检验项目名称")
        private String inspectItemName;

        @ApiModelProperty("检验项目编码")
        private String inspectItemCode;

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


        private Integer duration;

        private List<Long> teams;

        private ItemDurationUnitEnum timeUnit;
        private List<InspectParameterConfigDTO> inspectionParameters;
    }

    /**
     * 分析项配置DTO
     */
    @Data
    public static class InspectParameterConfigDTO {
        private Long parameterConfigId;
        /**
         * 关联的方案ID
         */
        private Long schemeId;

        /**
         * 关联的版本ID
         */
        private Long versionId;

        private Long packageId;

        private Long inspectItemId;

        /**
         * 关联的检验项目配置ID
         */
        private Long itemConfigId;

        /**
         * 分析项ID
         */
        private Long parameterId;

        /**
         * 分析项名称
         */
        private String parameterName;

        /**
         * 分析项编码
         */
        private String parameterCode;
        /**
         * 标准规定
         */
        private String standardRule;

        /**
         * 分析方法ID
         */
        private Long recordId;
        private String recordCode;
        private Long recordVersionId;
        private String recordVersion;
        /**
         * 记录项id（bm_batch_record_item.id）
         */
        private Long recordItemId;

        /**
         * 分析方法名称（优先standard，其次version，再次code）
         */
        private String recordName;

        /**
         * 是否报告项
         */
        private Boolean isReportable;

        /**
         * 是否可执行
         */
        private Boolean isExecutable;

        /**
         * 最终表达式
         */
        private String finalExpression;
        /**
         * 执行方式：LIMS/ELN
         */
        private ExecuteMethodEnum executeMethod;
        public ExecuteMethodEnum getExecuteMethod() { return this.executeMethod; }
        public void setExecuteMethod(ExecuteMethodEnum executeMethod) { this.executeMethod = executeMethod; }
        private List<DataPointConfigDTO> dataPoints;

        /**
         * 判定配置列表
         */
        private List<JudgmentConfigDTO> judgments;

        /**
         * 判定条件引用的数据点配置是否存在删除（true=存在问题）
         */
        private Boolean judgmentConfigError;

        /**
         * 判定引用的数据点异常聚合标识（分析项层级）
         */
        private Boolean judgmentDataPointDeleted;
        private Boolean judgmentDataPointBindingMissing;
        private Boolean judgmentDataPointTypeChanged;
        private Boolean judgmentDataPointOptionInvalid;
    }

    /**
     * 数据点配置DTO
     */
    @Data
    public static class DataPointConfigDTO {
        private Long dataPointConfigId;
        /**
         * 关联的方案ID
         */
        private Long schemeId;

        /**
         * 关联的版本ID
         */
        private Long versionId;

        private Long packageId;

        private Long inspectItemId;

        private Long parameterId;

        /**
         * 关联的分析项配置ID
         */
        private Long parameterConfigId;

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

        private String timeFormat;
        private String dateStyle;
        private Boolean roundingUp;


        /**
         * 趋势线配置(JSON)
         */
        private String trendLineConfig;

        /**
         * 选项配置(JSON)
         */
        private String options;

        /**
         * 是否报告显示
         */
        private Boolean reportDisplay;

        /**
         * 最终判定表达式（由多个判定条件组合而成）
         */
        private String finalExpression;
        private List<JudgmentConfigDTO> judgments;

        /**
         * 记录id（绑定记录组件用）
         */
        private Long recordId;
        /**
         * 记录版本id（绑定记录组件用）
         */
        private Long recordVersionId;
        /**
         * 记录组件id（bm_batch_record_component.id）
         */
        private Long componentId;
        /**
         * 记录项id（bm_batch_record_item.id）
         */
        private Long recordItemId;
        /**
         * 字段id（fieldId，对应记录组件字段）
         */
        private Long fieldId;

        /**
         * 是否被结论判定引用
         */
        private Boolean referencedByJudgment;

        /**
         * 判定所引用的数据点是否存在异常（分析项维度聚合标识）
         */
        private Boolean judgmentDataPointDeleted;
        private Boolean judgmentDataPointTypeChanged;
        private Boolean judgmentDataPointBindingMissing;
    }

    /**
     * 判定配置DTO
     */
    @Data
    public static class JudgmentConfigDTO {
        private Long judgmentConfigId;



        @ApiModelProperty("判定配置名称")
        private String judgementConfigName;
        /**
         * 关联的方案ID
         */
        private Long schemeId;

        /**
         * 关联的版本ID
         */
        private Long versionId;


        private Long packageId;

        private Long inspectItemId;

        private Long parameterId;
        /**
         * 关联的分析项配置ID
         */
        private Long parameterConfigId;

        private Long dataPointConfigId;

        /**
         * 关联的数据点ID
         */
        private Long dataPointId;

        /**
         * 数据点类型：NUMBER-数值, TEXT-文本, OPTION-选项, TIME-时间
         */
        private DataPointTypeEnum pointType;

        /**
         * 数据点是否已被删除（判定引用失效标识）
         */
        private Boolean dataPointDeleted;

        /**
         * 判定绑定的数据点与记录组件的关联关系是否缺失
         */
        private Boolean dataPointBindingMissing;
        /**
         * 判定绑定的选项是否已在组件选项中配置
         */
        private Boolean dataPointOptionInvalid;

        /**
         * 判定类型：RANGE-范围判定, EQUAL-相等判定
         */
        private JudgmentTypeEnum judgmentType;


        @ApiModelProperty("默认测试结果")
        private Boolean defaultResult;
        /**
         * 最小值
         */
        private BigDecimal minValue;

        /**
         * 最大值
         */
        private BigDecimal maxValue;

        /**
         * 标准值
         */
        private String standardValue;

        /**
         * 判定表达式
         */
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
     * 取样配置DTO
     */
    @Data
    public static class SamplingConfigDTO {
        private Long samplingConfigId;
        /**
         * 关联的方案ID
         */
        private Long schemeId;

        /**
         * 关联的版本ID
         */
        private Long versionId;

        /**
         * 检验项目ID，为NULL时表示整体取样
         */
        private Long inspectItemId;

        private String inspectItemName;

        private String inspectItemCode;
        /**
         * 取样量
         */
        private String samplingAmount;

        /**
         * 取样单位
         */
        private String samplingUnit;

        /**
         * 取样份数
         */
        private Integer samplingCount;
    }
} 