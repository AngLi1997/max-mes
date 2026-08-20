package com.bmos.mes.service.preparation.measure.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.preparation.MeasureTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配液量取日志
 */
@Data
@TableName("bm_liquid_preparation_measure_log")
public class LiquidPreparationMeasureLog {

    private Long id;

    /**
     * 操作类型
     */
    private MeasureTypeEnum measureType;

    /**
     * 量取量
     */
    private BigDecimal measureQuantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 量取人id
     */
    private String measurerId;

    /**
     * 量取人登录账号
     */
    private String measurerLoginName;

    /**
     * 量取人名称
     */
    private String measurerName;

    /**
     * 复核人id
     */
    private String reCheckerId;

    /**
     * 复核人登录账号
     */
    private String reCheckerLoginName;

    /**
     * 复核人名称
     */
    private String reCheckerName;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 物料件号
     */
    private String materialNo;

    /**
     * 量取时间
     */
    private LocalDateTime measureTime;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialMergeCode;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料类型
     */
    private Integer materialType;

    /**
     * 物料批号
     */
    private String materialBatchNo;

    /**
     * 物料批次id
     */
    private Long materialBatchId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productMergeCode;

    /**
     * 生产批号
     */
    private String productBatchNo;

    /**
     * 生产批次id
     */
    private Long productPlanId;
}
