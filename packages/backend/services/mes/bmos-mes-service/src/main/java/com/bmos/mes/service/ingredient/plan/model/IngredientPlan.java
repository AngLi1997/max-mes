package com.bmos.mes.service.ingredient.plan.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 配料计划
 */
@Getter
@Setter
@TableName("bm_ingredient_plan")
public class IngredientPlan extends BaseDO {

    /**
     * 配料单名称
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
     * 配料计划是否已完成
     */
    private Boolean completed;

    /**
     * 复制版本
     */
    private Long copyVersion;


}
