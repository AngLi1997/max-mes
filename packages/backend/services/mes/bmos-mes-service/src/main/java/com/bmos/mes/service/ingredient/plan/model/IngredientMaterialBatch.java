package com.bmos.mes.service.ingredient.plan.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 配料计划物料批次
 */
@Getter
@Setter
@TableName("bm_ingredient_plan_material_batch")
public class IngredientMaterialBatch extends BaseDO {

    /**
     * 配料单id
     */
    private Long ingredientPlanId;

    /**
     * 物料批次id
     */
    private Long materialBatchId;


    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 配料量
     */
    private BigDecimal ingredientQuantity;

    /**
     * 理论量
     */
    private BigDecimal theoreticalQuantity;

    /**
     * 配方物料单位id
     */
    private Long unitId;

    /**
     * 计划人id
     */
    private String userId;
}
