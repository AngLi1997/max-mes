package com.bmos.wms.service.sendout.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.wms.common.enums.sendout.SendOrderType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 发料单列表项
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 18:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bw_send_out_order_item")
public class SendOutOrderItem extends BaseDO {

    /**
     * 发料工单id
     */
    private Long sendOrderId;

    /**
     * 发料工单类型
     */
    private SendOrderType sendOrderType;

    /**
     * 货品id
     */
    private Long cargoId;

    /**
     * 批次id
     */
    private Long inventoryBatchId;

    /**
     * 预订量
     */
    private BigDecimal reserveQuantity;

    /**
     * 单位id
     */
    private Long unitId;
}
