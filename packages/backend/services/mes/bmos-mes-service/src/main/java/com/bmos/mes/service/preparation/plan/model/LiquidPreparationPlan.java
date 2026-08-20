package com.bmos.mes.service.preparation.plan.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 配液计划
 */
@Getter
@Setter
@TableName("bm_liquid_preparation_plan")
public class LiquidPreparationPlan extends BaseDO {

    /**
     * 配液单名称
     */
    private String name;

    /**
     * 生产计划id
     */
    private Long productPlanId;

    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 是否复用
     */
    private Boolean reuse;

    /**
     * 工序步骤模型id
     */
    private Long procedureStepModelId;

    /**
     * 流水号
     */
    private Integer serialNo;

    /**
     * 组件id
     */
    private Long componentId;

    /**
     * 配液计划是否已完成
     */
    private Boolean completed;

    /**
     * 复制版本
     */
    private Long copyVersion;

    /**
     * 组件配置信息
     */
    private String configJson;

    /**
     * 实际目标体积 根据配方批量与生产批量和配方物料计算得出
     */
    private BigDecimal actualTargetVolume;

    /**
     * 目标体积单位
     */
    private Long unitId;

}
