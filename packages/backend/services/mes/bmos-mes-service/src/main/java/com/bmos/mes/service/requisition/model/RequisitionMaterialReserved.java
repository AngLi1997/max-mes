package com.bmos.mes.service.requisition.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 领料单物料预定
 */
@Getter
@Setter
@TableName("bm_requisition_plan_reserved")
public class RequisitionMaterialReserved extends BaseDO {

    /**
     * 领料单id
     */
    private Long requisitionPlanId;

    /**
     * 物料批次id
     */
    private Long materialBatchId;

    /**
     * 物料批号
     */
    private String materialBatchNo;

    /**
     * 计划量/领料量
     */
    private BigDecimal plannedQuantity;

    /**
     * 理论量
     */
    private BigDecimal theoreticalQuantity;

    /**
     * 计划人id
     */
    private String userId;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 有效日期
     */
    private LocalDate expiredDate;

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 物料规格
     */
    private String specification;

    /**
     * 物料编码
     */
    private String mergeCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * wms货品id
     */
    private Long wmsMaterialId;

    /**
     * 水分
     */
    private BigDecimal hydration;

    /**
     * 含量
     */
    private BigDecimal noHydrationContent;

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
    private String originBatchNo;

    /**
     * 原厂编码
     */
    private String originCode;

}
