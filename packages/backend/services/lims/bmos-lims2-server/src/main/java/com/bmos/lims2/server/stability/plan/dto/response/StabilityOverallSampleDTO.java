package com.bmos.lims2.server.stability.plan.dto.response;

import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性整体样品列表DTO（批次维度，一行一个批次）
 */
@Data
public class StabilityOverallSampleDTO {

    /** 批次ID（lm_stability_inspect_plan_batch.id） */
    private Long batchId;

    /** 批号 */
    private String batchNo;

    /** 批次生产日期 */
    private LocalDate productionDate;

    /** 稳定性考察计划ID */
    private Long planId;

    /** 稳定性考察计划编号 */
    private String planCode;

    /** 检品名称 */
    private String materialName;

    /** 检品编码 */
    private String materialCode;

    /** 检品规格 */
    private String materialSpec;

    /** 计划创建人ID */
    private String planCreateBy;

    /** 计划创建时间 */
    private LocalDateTime planCreateTime;

    /**
     * 批次样品状态（取所有样品中优先级最高的状态）：
     * PENDING=存在待取样；SAMPLED=已全部取样待接收；RECEIVED=已全部接收
     */
    private StabilityPlanSampleStatusEnum batchSampleStatus;
}
