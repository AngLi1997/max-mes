package com.bmos.mes.service.mcp.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 16:05
 */
@Data
public class WmsStorageInventoryFeignVO {

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料批次
     */
    private String materialBatchNo;

    /**
     * 有效期至
     */
    private String expiredDate;

    /**
     * 可用量
     */
    private BigDecimal availableQuantity;


    /**
     * 单位id
     */
    private Long unitId;
}
