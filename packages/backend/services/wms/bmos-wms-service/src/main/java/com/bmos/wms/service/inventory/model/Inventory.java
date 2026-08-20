package com.bmos.wms.service.inventory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 货品件
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/28 14:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bw_inventory")
public class Inventory extends BaseDO {

    /**
     * 货品id
     */
    private Long cargoId;

    /**
     * 货品批次id
     */
    private Long inventoryBatchId;

    /**
     * 货位id
     */
    private Long positionId;

    /**
     * 物料件号
     */
    private String no;

    /**
     * 初始量
     */
    private BigDecimal initQuantity;

    /**
     * 可用量
     */
    private BigDecimal availableQuantity;

    /**
     * 消耗量
     */
    private BigDecimal consumeQuantity;

    /**
     * 预订量
     */
    private BigDecimal reserveQuantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 货品量(可用量 + 预订量)
     *
     * @return 货品量
     */
    @JsonIgnore
    public BigDecimal getQuantity() {
        return availableQuantity.add(reserveQuantity);
    }

    /**
     * 可用性
     *
     * @return 可用性
     */
    public Boolean isAvailable() {
        return getQuantity().compareTo(BigDecimal.ZERO) > 0;
    }
}
