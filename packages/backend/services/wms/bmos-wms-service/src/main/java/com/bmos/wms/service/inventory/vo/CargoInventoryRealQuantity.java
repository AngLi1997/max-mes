package com.bmos.wms.service.inventory.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/5/24 14:01
 */
@Data
public class CargoInventoryRealQuantity {

    /**
     * 货品id
     */
    private Long cargoId;

    /**
     * 批次id
     */
    private Long inventoryBatchId;

    /**
     * 未被预定的可用量
     */
    private BigDecimal availableQuantity;

    /**
     * 预定量
     */
    private BigDecimal reserveQuantity;
}
