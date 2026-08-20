package com.bmos.lims2.server.stability.plan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.StabilityTimepointTaskStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 稳定性考察计划时间点任务实体
 *
 * 样品接收后，按方案时间点自动生成，每条对应详情页矩阵中的一个单元格：
 * （批次 × 试验类型 × 时间点）。
 */
@Getter
@Setter
@TableName("lm_stability_plan_timepoint_task")
public class StabilityPlanTimepointTask extends BaseDO {

    /**
     * 考察计划ID
     */
    private Long planId;

    /**
     * 批次ID
     */
    private Long batchId;

    /**
     * 样品ID（lm_stability_plan_sample.id）
     */
    private Long sampleId;

    /**
     * 方案检验计划ID（lm_stability_scheme_plan.id — 对应试验类型）
     */
    private Long schemePlanId;

    /**
     * 方案时间点ID（lm_stability_scheme_plan_timepoint.id）
     */
    private Long schemeTimepointId;

    /**
     * 试验类型（冗余）
     */
    private String experimentType;

    /**
     * 储存条件（冗余）
     */
    private String storageCondition;

    /**
     * 时间点数值（冗余）
     */
    private Integer timeValue;

    /**
     * 时间单位（DAY/MONTH/YEAR，冗余）
     */
    private String timeUnit;

    /**
     * 计划检验日期（= 样品接收日期 + 时间点偏移）
     */
    private LocalDate plannedDate;

    /**
     * 计划取样量（来自方案时间点配置，创建后不再更改）
     */
    private String sampleAmount;

    /**
     * 计划取样量单位
     */
    private String sampleUnit;

    /**
     * 实际取样量（取样提交时填写）
     */
    private String actualSampleAmount;

    /**
     * 实际取样量单位
     */
    private String actualSampleUnit;

    /**
     * 任务状态
     */
    private StabilityTimepointTaskStatusEnum status;

    /**
     * 关联检验单ID（发起检验后关联，用于详情页跳转）
     */
    private Long inspectionOrderId;

    /**
     * 实际检验完成日期
     */
    private LocalDate completedDate;

}
