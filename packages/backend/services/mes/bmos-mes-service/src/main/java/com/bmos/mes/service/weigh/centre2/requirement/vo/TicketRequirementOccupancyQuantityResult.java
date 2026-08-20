package com.bmos.mes.service.weigh.centre2.requirement.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/5/20 16:44
 */
@Data
public class TicketRequirementOccupancyQuantityResult {

    /**
     * 物料批次
     */
    private Long storageMaterialBatchId;

    /**
     * 占用量
     */
    private BigDecimal occupancyQuantity;

    /**
     * 单位id
     */
    private Long unitId;
}
