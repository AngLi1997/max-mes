package com.bmos.lims2.server.stability.plan.dto.request;

import lombok.Data;

/**
 * 整体取样-手动新增样品明细DTO
 */
@Data
public class StabilityPlanSampleAddItemDTO {

    /**
     * 试验类型（字典值）
     */
    private String experimentType;

    /**
     * 储存条件
     */
    private String storageCondition;

    /**
     * 计划取样量
     */
    private String plannedSampleAmount;

    /**
     * 实际取样量（可选）
     */
    private String actualSampleAmount;

    /**
     * 取样量单位ID
     */
    private String sampleUnit;
}
