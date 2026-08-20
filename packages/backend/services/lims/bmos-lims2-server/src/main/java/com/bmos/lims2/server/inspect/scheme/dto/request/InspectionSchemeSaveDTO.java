package com.bmos.lims2.server.inspect.scheme.dto.request;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 检验方案保存请求DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeSaveDTO {



    /**
     * 方案名称
     */
    private String name;

    /**
     * 版本描述
     */
    private String description;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 父版本ID（基于现有版本创建时必填）
     */
    private Long parentVersionId;

    /**
     * 物料信息
     */
    private MaterialInfoDTO material;

    /**
     * 实验包信息
     */
    private PackageInfoDTO packageInfo;

    /**
     * 检验项目配置列表
     */
    private List<InspectionItemConfigDTO> inspectionItems;

    /**
     * 取样配置列表
     */
    private List<SamplingConfigDTO> samplingConfigs;

    /**
     * 物料信息DTO
     */
    @Data
    public static class MaterialInfoDTO {
        /**
         * 物料ID
         */
        private Long materialId;

        /**
         * 物料名称
         */
        private String materialName;

        /**
         * 物料编码
         */
        private String materialCode;
    }

    /**
     * 实验包信息DTO
     */
    @Data
    public static class PackageInfoDTO {
        /**
         * 实验包ID
         */
        private Long packageId;

        /**
         * 实验包名称
         */
        private String packageName;

        /**
         * 实验包编码
         */
        private String packageCode;
    }

    /**
     * 检验项目配置DTO
     */
    @Data
    public static class InspectionItemConfigDTO {
        /**
         * 检验项目ID
         */
        private Long inspectItemId;

        /**
         * 是否必需
         */
        private Boolean isRequired;

        /**
         * 排序
         */
        private Integer sort;


        private Integer duration;

        private ItemDurationUnitEnum timeUnit;


        private List<Long> teams;

        /**
         * 分析项配置列表
         */
        private List<InspectParameterConfigDTO> inspectionParameters;
    }

    /**
     * 分析项配置DTO
     */
    @Data
    public static class InspectParameterConfigDTO {
        /**
         * 分析项ID
         */
        private Long parameterId;

        /**
         * 标准规定
         */
        private String standardRule;


        /**
         * 分析方法ID
         */
        private Long recordId;

        /**
         * 是否报告项
         */
        private Boolean isReportable;

        /**
         * 是否可执行
         */
        private Boolean isExecutable;

        /**
         * 数据点配置列表
         */
        private List<DataPointConfigDTO> dataPoints;

        /**
         * 最终表达式
         */
        private String finalExpression;

        /**
         * 判定配置列表
         */
        private List<JudgmentConfigDTO> judgments;

        /**
         * 执行方式：LIMS/ELN
         */
        private ExecuteMethodEnum executeMethod;
        public ExecuteMethodEnum getExecuteMethod() { return this.executeMethod; }
        public void setExecuteMethod(ExecuteMethodEnum executeMethod) { this.executeMethod = executeMethod; }
    }

    /**
     * 数据点配置DTO
     */
    @Data
    public static class DataPointConfigDTO {
        /**
         * 原始数据点ID
         */
        private Long dataPointId;

        /**
         * 数据点名称
         */
        private String name;

        /**
         * 数据点类型
         */
        private DataPointTypeEnum pointType;

        /**
         * 趋势线配置JSON
         */
        private String trendLineConfig;

        /**
         * 选项配置JSON
         */
        private String options;

        /**
         * 是否报告显示
         */
        private Boolean reportDisplay;

        /**
         * 时间类型显示格式（仅当pointType为TIME时有效）
         */
        private String timeFormat;
        private String dateStyle;

    }

    /**
     * 判定配置DTO
     */
    @Data
    public static class JudgmentConfigDTO {
        /**
         * 判定类型
         */
        private JudgmentTypeEnum judgmentType;


        @ApiModelProperty("默认测试结果")
        private Boolean defaultResult;

        /**
         * 原始数据点ID
         */
        private Long dataPointId;

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
    }

    /**
     * 取样配置DTO
     */
    @Data
    public static class SamplingConfigDTO {
        /**
         * 检验项目ID
         */
        private Long inspectItemId;

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