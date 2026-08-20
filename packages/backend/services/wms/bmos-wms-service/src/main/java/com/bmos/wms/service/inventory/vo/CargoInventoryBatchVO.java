package com.bmos.wms.service.inventory.vo;

import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
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
public class CargoInventoryBatchVO {

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
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-02-06")
    private LocalDate expiredDate;
}
