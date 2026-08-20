package com.bmos.wms.service.inventory.vo;

import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 货品信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 14:54
 */
@Data
@ApiModel("货品信息")
public class CargoInventoryVO {

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id", example = "1")
    private Long id;

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "可口可乐")
    private String cargoName;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "WH03")
    private String mergeCode;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格", example = "25kg/袋")
    private String specification;

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
     * 库存量
     */
    @ApiModelProperty(value = "库存量", example = "1.000")
    @PrecisionValue
    private BigDecimal quantity;

    /**
     * 件数
     */
    @ApiModelProperty(value = "件数", example = "1")
    private Integer size;
}
