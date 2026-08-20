package com.bmos.lims2.server.stability.scheme.dto.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性方案检验计划保存DTO
 */
@Data
public class StabilitySchemePlanSaveDTO {

    @NotNull(message = "版本ID不能为空")
    private Long versionId;

    @Valid
    private List<PlanDTO> plans;

    @Data
    public static class PlanDTO {

        /** 检验计划记录ID（有值则更新，无值则新增） */
        private Long planId;

        /** 试验类型 */
        private String experimentType;

        /** 试验储存条件 */
        private String storageCondition;

        /** 整体取样量 */
        private String totalSampleAmount;

        /** 整体取样量单位 */
        private Long totalSampleUnit;

        private Integer sort;

        @Valid
        private List<TimepointDTO> timepoints;
    }

    @Data
    public static class TimepointDTO {

        /** 时间点记录ID（有值则更新，无值则新增） */
        private Long timepointId;

        /** 检测时间点数值 */
        private Integer timeValue;

        /** 时间单位 */
        private String timeUnit;

        /** 取样量 */
        private String sampleAmount;

        /** 取样量单位 */
        private String sampleUnit;

        /**
         * 是否全选方案配置中的所有分析项
         */
        private Boolean selectAll;

        /**
         * 分析项引用列表（selectAll=false 时有效）
         */
        private List<ParamRefDTO> paramRefs;

        private Integer sort;
    }

    @Data
    public static class ParamRefDTO {

        /** 分析项配置ID */
        private Long parameterConfigId;

        /** 原始分析项ID */
        private Long parameterId;

        /** 分析项编码 */
        private String parameterCode;

        /** 检验项目配置ID */
        private Long itemConfigId;

        /** 检验项目ID */
        private Long inspectItemId;

        /** 检验项目编码 */
        private String inspectItemCode;
    }
}
