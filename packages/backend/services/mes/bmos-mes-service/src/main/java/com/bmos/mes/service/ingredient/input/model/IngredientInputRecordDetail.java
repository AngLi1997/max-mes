package com.bmos.mes.service.ingredient.input.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配料投入记录详情 含物料件信息及批次信息 用于批记录数据回填
 */
@Data
public class IngredientInputRecordDetail {

    /**
     * 配料单id
     */
    private Long ingredientPlanId;

    /**
     * 暂存物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 物料件编号
     */
    private String storageMaterialNo;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 投料人id
     */
    private String importerId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 投料时间
     */
    private LocalDateTime inputTime;

    /**
     * 暂存物料批次号
     */
    private String storageMaterialBatchNo;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料量
     */
    private BigDecimal quantity;

    /**
     * 物料名称
     */
    private String materialName;

    private String materialMergeCode;

    private String specification;

    private Long unitId;
}
