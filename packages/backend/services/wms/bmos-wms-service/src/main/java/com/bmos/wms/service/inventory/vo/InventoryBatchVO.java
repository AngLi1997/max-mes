package com.bmos.wms.service.inventory.vo;

import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/6 11:58
 */
@Data
@ApiModel("货品批次信息")
public class InventoryBatchVO {

    /**
     * 货品批次id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "氯化钠")
    private String cargoName;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "WH03")
    private String mergeCode;

    /**
     * 货品规格
     */
    @ApiModelProperty(value = "物料规格", example = "25kg/袋")
    private String specification;

    /**
     * 货品批号
     */
    @ApiModelProperty(value = "货品批号", example = "WH030102231001")
    private String inventoryBatchNo;

    /**
     * 件数
     */
    @ApiModelProperty(value = "件数", example = "1")
    private Integer size;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    @PrecisionUnitId
    private Long unitId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    /**
     * 单位转换率
     */
    @ApiModelProperty(value = "单位转换率", example = "1000")
    private BigDecimal rate;

    /**
     * 预订量
     */
    @ApiModelProperty(value = "预订量", example = "1.000")
    @PrecisionValue
    private BigDecimal reserveQuantity;

    /**
     * 可用量
     */
    @ApiModelProperty(value = "可用量", example = "1.000")
    @PrecisionValue
    private BigDecimal availableQuantity;

    /**
     * 物料量（可用+预定）
     */
    @ApiModelProperty(value = "物料量(可用+预定)", example = "1.000")
    @PrecisionValue
    private BigDecimal quantity;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-02-06")
    private LocalDate expiredDate;

    /**
     * 原厂批号
     */
    @ApiModelProperty(value = "原厂批号", example = "WH030102231001")
    private String factoryBatchNo;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期", example = "2024-02-06")
    private LocalDate produceDate;

    /**
     * 水分(%)
     */
    @ApiModelProperty(value = "水分(%)", example = "0")
    private BigDecimal hydration;

    /**
     * 无水含量(%)
     */
    @ApiModelProperty(value = "无水含量(%)", example = "100")
    private BigDecimal noHydrationContent;

    /**
     * 报告单编号
     */
    @ApiModelProperty(value = "报告单编号", example = "WH030102231001")
    private String reportNo;

    /**
     * 放行单编号
     */
    @ApiModelProperty(value = "放行单", example = "WH030102231001")
    private String licenceNo;
}
