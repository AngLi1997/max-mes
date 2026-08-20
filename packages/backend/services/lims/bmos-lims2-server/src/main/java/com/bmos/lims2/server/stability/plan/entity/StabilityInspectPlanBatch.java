package com.bmos.lims2.server.stability.plan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 稳定性考察计划批次实体类
 */
@Getter
@Setter
@TableName("lm_stability_inspect_plan_batch")
public class StabilityInspectPlanBatch extends BaseDO {

    /**
     * 考察计划ID
     */
    private Long planId;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 批次生产日期
     */
    private LocalDate productionDate;

    /**
     * 样品接收日期（样品接收完成后更新）
     */
    private LocalDate sampleReceiveDate;

    /**
     * 用户手动选择的0月检验单ID（当同批号存在多条常规检验单时由用户指定）
     */
    private Long zeroMonthOrderId;

}
