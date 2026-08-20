package com.bmos.wms.service.inventory.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class CargoInventoryBatchItemVO {

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
     * 原厂批号
     */
    @ApiModelProperty(value = "原厂批号", example = "WH030102231001")
    private String factoryBatchNo;

    /**
     * 有效日期
     */
    @ApiModelProperty(value = "有效日期", example = "2024-02-06")
    private LocalDate expiredDate;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期", example = "2024-03-29")
    private LocalDate produceDate;
}
