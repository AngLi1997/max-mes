package com.bmos.mes.service.ingredient.plan.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class IngredientMaterialBatchDetailInfo extends IngredientMaterialBatch{

    /**
     * 物料批次号
     */
    private String materialBatchNo;

    /**
     * 水分
     */
    private BigDecimal hydration;

    /**
     * 含量
     */
    private BigDecimal noHydrationContent;

    /**
     * 有效日期
     */
    private LocalDate expiredDate;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 生产商
     */
    private String producer;

    /**
     * 原厂批号
     */
    private String factoryBatchNo;

    /**
     * 原厂编码
     */
    private String originalBatchNo;

    /**
     * 报告单编号
     */
    private String reportNo;

    /**
     * 放行单编号
     */
    private String licenceNo;


}
