package com.bmos.lims2.server.stability.plan.dto.response;

import com.bmos.lims2.common.enums.StabilityTimepointTaskStatusEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性考察检验单样品DTO（含稳定性上下文信息）
 */
@Data
public class StabilityOrderSampleDTO {

    // ── 样品信息（来自 lm_sample）──────────────────────────

    private Long sampleId;

    private String sampleNo;

    /** 是否已取样 */
    private Boolean sampled;

    /** 是否已接收 */
    private Boolean received;

    private String samplerName;

    private LocalDateTime samplingTime;

    private String receiverName;

    private LocalDateTime receiveTime;

    /** 计划取样量 */
    private String planQuantity;

    /** 取样量单位ID */
    private Long unitId;

    // ── 稳定性上下文（来自 lm_stability_plan_timepoint_task）─

    private Long timepointTaskId;

    private Long inspectionOrderId;

    /** 试验类型 */
    private String experimentType;

    /** 储存条件 */
    private String storageCondition;

    /** 时间点数值 */
    private Integer timeValue;

    /** 时间单位（DAY/MONTH/YEAR） */
    private String timeUnit;

    /** 计划检验日期 */
    private LocalDate plannedDate;

    /** 时间点任务状态 */
    private StabilityTimepointTaskStatusEnum taskStatus;

    // ── 批次信息（来自 lm_stability_inspect_plan_batch）─────

    private String batchNo;
}
