package com.bmos.lims2.server.stability.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 稳定性样品移动记录实体
 */
@Getter
@Setter
@TableName("lm_stability_sample_move_log")
public class StabilitySampleMoveLog extends BaseDO {

    /** 稳定性计划样品ID（lm_stability_plan_sample.id） */
    private Long planSampleId;

    /** 原储存位置 */
    private String fromLocation;

    /** 目标储存位置 */
    private String toLocation;

    /** 移动原因 */
    private String moveReason;

    /** 操作人ID */
    private String operatorId;

    /** 操作人姓名 */
    private String operatorName;

    /** 移动时间 */
    private LocalDateTime moveTime;
}
