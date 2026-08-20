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
 * @date 2024/4/17 11:16
 */
@Data
@ApiModel("货品信息")
public class CargoInventoryItemVO {

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id", example = "1")
    private Long id;

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "苹果")
    private String cargoName;

    /**
     * 货品批次id
     */
    @ApiModelProperty(value = "货品批次id", example = "1")
    private Long inventoryBatchId;

    /**
     * 货品批号
     */
    @ApiModelProperty(value = "货品批号", example = "WH030102")
    private String inventoryBatchNo;

    /**
     * 货品件号
     */
    @ApiModelProperty(value = "货品件号", example = "001")
    private String inventoryNo;

    /**
     * 物料量
     */
    @ApiModelProperty(value = "物料量", example = "1.000")
    @PrecisionValue
    private BigDecimal quantity;

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
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1")
    private Long positionId;

    /**
     * 货位
     */
    @ApiModelProperty(value = "货位", example = "A01")
    private String position;

    /**
     * 有效日期
     */
    @ApiModelProperty(value = "有效日期", example = "2024-02-06")
    private LocalDate expiredDate;
}
