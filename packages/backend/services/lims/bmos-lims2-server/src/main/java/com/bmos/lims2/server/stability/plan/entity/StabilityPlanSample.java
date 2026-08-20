package com.bmos.lims2.server.stability.plan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 稳定性考察计划样品实体
 *
 * 计划创建时，针对每个批次 × 每个试验类型（方案检验计划）生成一条样品记录。
 * 样品接收完成后，稳定性考察正式开始，据此计算各时间点的计划检验日期。
 */
@Getter
@Setter
@TableName("lm_stability_plan_sample")
public class StabilityPlanSample extends BaseDO {

    /**
     * 考察计划ID
     */
    private Long planId;

    /**
     * 批次ID（lm_stability_inspect_plan_batch.id）
     */
    private Long batchId;

    /**
     * 批号（冗余）
     */
    private String batchNo;

    /**
     * 方案检验计划ID（lm_stability_scheme_plan.id — 对应一个试验类型配置）
     */
    private Long schemePlanId;

    /**
     * 试验类型（冗余）
     */
    private String experimentType;

    /**
     * 储存条件（冗余）
     */
    private String storageCondition;

    /**
     * 计划取样量（来自方案检验计划的整体取样量）
     */
    private String plannedSampleAmount;

    /**
     * 取样量单位
     */
    private String sampleUnit;

    /**
     * 样品状态（PENDING:待取样 SAMPLED:已取样 RECEIVED:已接收 PENDING_DESTROY:待销毁 DESTROYED:已销毁）
     */
    private StabilityPlanSampleStatusEnum status;

    /**
     * 实际取样量
     */
    private String actualSampleAmount;

    /**
     * 样品接收日期（接收完成后更新；作为时间点计划日期计算的起点）
     */
    private LocalDate receiveDate;

    /**
     * 关联的 lm_sample 记录ID（整体取样后创建的父样品）
     */
    private Long sampleId;

    /**
     * 样品编号（冗余，来自 lm_sample.sample_no）
     */
    private String sampleNo;

    /**
     * 取样人ID
     */
    private String samplerId;

    /**
     * 取样人姓名
     */
    private String samplerName;

    /**
     * 取样时间
     */
    private java.time.LocalDateTime samplingTime;

    /**
     * 接收人ID
     */
    private String receiverId;

    /**
     * 接收人姓名
     */
    private String receiverName;

    /**
     * 接收时间
     */
    private java.time.LocalDateTime receiveTime;

}
