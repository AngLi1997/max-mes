package com.bmos.mes.service.preparation.plan.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LiquidPreparationMaterialBatchDetailInfo {

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料批次id
     */
    private Long materialBatchId;

    /**
     * 物料批次号
     */
    private String materialBatchNo;

    /**
     * 配液量
     */
    private BigDecimal preparationQuantity;

    /**
     * 单位id
     */
    private Long unitId;

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
     * 原始编码
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

    /**
     * 有效期至
     */
    private String expiredDate;

    /**
     * 配液时间
     */
    private LocalDateTime createTime;

    /**
     * 配液物料排序
     */
    private int order;

    /**
     * 批次自定义字段
     */

    /**
     * 物料自定义字段
     */


}
