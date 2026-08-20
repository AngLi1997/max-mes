package com.bmos.wms.service.inventory.vo;

import com.bmos.unit.annotation.PrecisionUnitId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 货品批次信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 14:54
 */
@Data
@ApiModel("货品批次信息")
public class CargoInventoryBatchDetailVO {

    /**
     * 货品批次id
     */
    @ApiModelProperty(value = "货品批次id", example = "1")
    private Long id;

    /**
     * 货品批号
     */
    @ApiModelProperty(value = "货品批号", example = "WH030102")
    private String inventoryBatchNo;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    @PrecisionUnitId
    private Long unitId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "袋")
    private String unit;

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id", example = "1")
    private Long cargoId;

    /**
     * 原厂批号
     */
    @ApiModelProperty(value = "原厂批号", example = "WH030102")
    private String factoryBatchNo;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期", example = "2023-04-07")
    private LocalDate produceDate;

    /**
     * 有效日期
     */
    @ApiModelProperty(value = "有效日期", example = "2023-04-07")
    private LocalDate expiredDate;

    /**
     * 水分(%)
     */
    @ApiModelProperty(value = "水分(%)", example = "1.000")
    private BigDecimal hydration;

    /**
     * 无水含量(%)
     */
    @ApiModelProperty(value = "水分(%)", example = "1.000")
    private BigDecimal noHydrationContent;

    /**
     * 报告单编号
     */
    @ApiModelProperty(value = "报告单编号", example = "WH030102")
    private String reportNo;

    /**
     * 放行单编号
     */
    @ApiModelProperty(value = "放行单编号", example = "WH030102")
    private String licenceNo;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "可用性", example = "true")
    private Boolean available;

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
     * 供应商
     */
    @ApiModelProperty(value = "供应商", example = "供应商")
    private String supplier;

    /**
     * 生产商
     */
    @ApiModelProperty(value = "生产商", example = "生产商")
    private String producer;

    /**
     * 可用量
     */
    @ApiModelProperty(value = "可用量", example = "1.000")
    private BigDecimal availableQuantity;

    /**
     * 预订量
     */
    @ApiModelProperty(value = "预定量", example = "1.000")
    private BigDecimal reserveQuantity;

    /**
     * 货品量
     */
    @ApiModelProperty(value = "货品量", example = "1.000")
    private BigDecimal quantity;

    /**
     * 结存件数
     */
    @ApiModelProperty(value = "结存件数", example = "1")
    private Integer availableSize;
}
