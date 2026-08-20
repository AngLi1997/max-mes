package com.bmos.mes.service.mcp.dto;

import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 18:40
 */
@Data
public class MesStorageInventoryDataQuery {

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
    private Boolean includeExpired;
}
