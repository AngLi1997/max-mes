package com.bmos.mes.service.preparation.measure.dto;


import com.bmos.mes.common.enums.preparation.MeasureTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@ApiModel("量取日志保存DTO")
@Builder
public class LiquidPreparationMeasureLogSaveDTO {

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 物料件号
     */
    private String materialNo;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 量取日志
     */
    private MeasureTypeEnum measureType;

    /**
     * 量取人id
     */
    private String measurerId;

    /**
     * 复核人id
     */
    private String reCheckerId;


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


    /**
     * 物料批号
     */
    private String materialBatchNo;

    /**
     * 物料批次id
     */
    private Long materialBatchId;

    /**
     * 物料类型
     */
    private Integer materialType;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialMergeCode;

    /**
     * 量取量
     */
    private BigDecimal measureQuantity;


}
