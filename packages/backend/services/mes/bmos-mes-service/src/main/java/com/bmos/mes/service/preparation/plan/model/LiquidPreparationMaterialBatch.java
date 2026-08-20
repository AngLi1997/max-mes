package com.bmos.mes.service.preparation.plan.model;

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
@TableName("bm_liquid_preparation_plan_material_batch")
public class LiquidPreparationMaterialBatch extends BaseDO {

    /**
     * 配液单id
     */
    private Long liquidPreparationPlanId;

    /**
     * 物料批次id
     */
    private Long materialBatchId;

    /**
     * 物料批次编码
     */
    private String materialBatchNo;


    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 配液量
     */
    private BigDecimal preparationQuantity;

    /**
     * 配方物料单位id
     */
    private Long unitId;

    /**
     * 在配液计划物料中的排序
     */
    private int materialOrder;
}
