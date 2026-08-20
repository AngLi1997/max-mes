package com.bmos.lims2.server.stability.sample.dto.response;

import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性样品管理平铺行DTO（Mapper查询结果，按计划分组前的原始行）
 */
@Data
public class StabilitySampleFlatRowDTO {

    // ─── lm_stability_plan_sample ───────────────────────────────────
    /** lm_stability_plan_sample.id */
    private Long overallSampleId;

    private Long planId;

    private Long batchId;
    private String batchNo;

    /** 关联 lm_sample.id */
    private Long sampleId;
    private String sampleNo;

    private String actualSampleAmount;
    private String sampleUnit;

    private LocalDate receiveDate;
    private String receiverName;

    private String experimentType;
    private String storageCondition;

    // ─── lm_stability_inspect_plan ──────────────────────────────────
    private String planCode;
    private Long materialId;
    private String materialName;
    private String materialCode;
    private String materialSpec;
    private String planCreateBy;
    private LocalDateTime planCreateTime;
    private LocalDate startDate;
    private LocalDate planEndDate;

    // ─── lm_sample ─────────────────────────────────────────────────
    private String storageLocation;
    private Boolean destroyed;
    private String currentQuantity;
    /** 销毁时间（lm_sample.process_time，destroyed=true时有效） */
    private LocalDateTime destroyTime;
    /** 销毁操作人（lm_sample.process_by） */
    private String destroyBy;
    /** 销毁原因（lm_sample.process_remark） */
    private String destroyReason;

    // ─── 派生状态 ───────────────────────────────────────────────────
    private StabilityPlanSampleStatusEnum sampleStatus;
}
