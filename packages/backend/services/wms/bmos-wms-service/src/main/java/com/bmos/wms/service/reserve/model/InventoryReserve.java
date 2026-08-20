package com.bmos.wms.service.reserve.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 货品预定信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/15 14:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("bw_inventory_reserve")
public class InventoryReserve {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 货品批次id
     */
    private Long inventoryBatchId;

    /**
     * 货品id
     */
    private Long cargoId;

    /**
     * 预定数量
     */
    private BigDecimal reserveQuantity;

    /**
     * 预定时间
     */
    private LocalDateTime reserveTime;

    /**
     * 领料计划单id
     */
    private Long requisitionPlanId;


}
