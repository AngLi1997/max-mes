package com.bmos.lims2.server.stability.plan.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 稳定性时间点取样列表行DTO（WAITING_SAMPLE 状态的时间点任务）
 */
@Data
public class StabilityTimepointSampleDTO {

    /** 时间点任务ID */
    private Long timepointTaskId;

    /** 整体样品编号（来自 lm_stability_plan_sample.sample_no） */
    private String sampleNo;

    /** 批号 */
    private String batchNo;

    /** 稳定性考察计划编号 */
    private String planCode;

    /** 计划ID */
    private Long planId;

    /** 检验单号 */
    private String orderNo;

    /** 请验时间（检验单创建时间） */
    private LocalDateTime requestTime;

    /** 检品名称 */
    private String materialName;

    /** 检品编码 */
    private String materialCode;

    /** 检品规格 */
    private String materialSpec;

    /** 试验类型 */
    private String experimentType;

    /** 试验类型名称 */
    private String experimentTypeName;

    /** 试验储存条件 */
    private String storageCondition;

    /** 计划取样量 */
    private String plannedSampleAmount;

    /** 取样量单位 */
    private String sampleUnit;

    /** 取样量单位名称 */
    private String sampleUnitName;

    /** 物料标准单位ID */
    private Long materialUnitId;

    /** 物料标准单位名称 */
    private String materialUnitName;

    /** 关联整体样品ID（StabilityPlanSample.id，即取样对象默认值） */
    private Long planSampleId;

    /** 批次ID */
    private Long batchId;

    /** 方案检验计划ID */
    private Long schemePlanId;
}
