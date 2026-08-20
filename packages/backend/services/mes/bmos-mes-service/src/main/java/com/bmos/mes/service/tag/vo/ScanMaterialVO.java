package com.bmos.mes.service.tag.vo;

import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 扫描物料件结果信息
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 13:36
 */
@Data
@ApiModel("扫描物料件结果信息")
public class ScanMaterialVO {

    /**
     * 物料件id
     */
    @ApiModelProperty(value = "物料件id", example = "1")
    private Long id;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "01")
    private String no;

    /**
     * 批次编号
     */
    @ApiModelProperty(value = "批次编号", example = "B1")
    private String materialBatchNo;

    /**
     * 批次id
     */
    @ApiModelProperty(value = "批次id", example = "1")
    private Long materialBatchId;

    /**
     * 物料预定量（经配方物料单位和精度换算）
     */
    @PrecisionValue
    @ApiModelProperty(value = "物料预定量（经配方物料单位和精度换算）", example = "1.000")
    private BigDecimal reserveQuantity;

    /**
     * 单位id
     */
    @PrecisionUnitId
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称", example = "kg")
    private String unit;

    /**
     * 水分(%)
     */
    @ApiModelProperty(value = "水分(%)", example = "1.000")
    private BigDecimal hydration;

    /**
     * 无水含量(%)
     */
    @ApiModelProperty(value = "无水含量(%)", example = "1.000")
    private BigDecimal noHydrationContent;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-04-01")
    private LocalDate expiredDate;

    /**
     * 原厂批号
     */
    @ApiModelProperty(value = "原厂批号", example = "2310001")
    private String factoryBatchNo;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商", example = "供应商")
    private String supplier;
}
