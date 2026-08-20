package com.bmos.lims2.server.stability.plan.dto.response;

import lombok.Data;

/**
 * 稳定性方案试验类型DTO（用于新增样品时下拉选项）
 */
@Data
public class StabilitySchemeExperimentTypeDTO {

    /** 试验类型（字典值） */
    private String experimentType;

    /** 试验类型名称（字典标签） */
    private String experimentTypeName;

    /** 储存条件 */
    private String storageCondition;

    /** 整体取样量（来自方案检验计划） */
    private String totalSampleAmount;

    /** 取样量单位ID */
    private String sampleUnit;

    /** 取样量单位名称 */
    private String unitName;
}
