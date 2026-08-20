package com.bmos.wms.service.inventory.vo;

import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 货品信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 13:41
 */
@Data
@ApiModel("货品信息详情")
public class InventoryVO {

    /**
     * 货品件id
     */
    @ApiModelProperty(value = "货品件id", example = "1")
    private Long id;

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1")
    private Long positionId;

    /**
     * 货品批次id
     */
    @ApiModelProperty(value = "货品批次id")
    private Long inventoryBatchId;

    /**
     * 货位
     */
    @ApiModelProperty(value = "货位", example = "A01")
    private String position;

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id", example = "1")
    private Long cargoId;

    /**
     * 货品编码
     */
    @ApiModelProperty(value = "货品编码", example = "001")
    private String cargoCode;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "WH03")
    private String mergeCode;

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "可口可乐")
    private String cargoName;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格", example = "25kg/袋")
    private String specification;

    /**
     * 货品件号
     */
    @ApiModelProperty(value = "货品件号", example = "001")
    private String inventoryNo;

    /**
     * 货品批号
     */
    @ApiModelProperty(value = "货品批号", example = "WH030102231001")
    private String batchNo;

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
     * 初始量
     */
    @ApiModelProperty(value = "初始量", example = "1.000")
    @PrecisionValue
    private BigDecimal initQuantity;

    /**
     * 可用量
     */
    @ApiModelProperty(value = "可用量", example = "1.000")
    @PrecisionValue
    private BigDecimal availableQuantity;

    /**
     * 预定量
     */
    @ApiModelProperty(value = "预定量", example = "1.000")
    @PrecisionValue
    private BigDecimal reserveQuantity;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-02-06")
    private LocalDate expiredDate;
}
