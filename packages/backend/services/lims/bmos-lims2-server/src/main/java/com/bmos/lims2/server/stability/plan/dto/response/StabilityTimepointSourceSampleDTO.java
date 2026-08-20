package com.bmos.lims2.server.stability.plan.dto.response;

import lombok.Data;

/**
 * 时间点取样"取样对象"下拉选项DTO（可从中选取的整体样品）
 */
@Data
public class StabilityTimepointSourceSampleDTO {

    /** StabilityPlanSample.id */
    private Long planSampleId;

    /** 样品编号（lm_stability_plan_sample.sample_no） */
    private String sampleNo;

    /** 实际取样量（取样时登记的整体样品量） */
    private String actualSampleAmount;

    /** 取样量单位 */
    private String sampleUnit;

    /** 储存位置（来自 lm_sample.storage_location） */
    private String storageLocation;

    /** 当前剩余数量（来自 lm_sample.current_quantity） */
    private String currentQuantity;
}
