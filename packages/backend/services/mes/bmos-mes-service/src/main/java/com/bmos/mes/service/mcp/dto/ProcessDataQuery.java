package com.bmos.mes.service.mcp.dto;

import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 17:37
 */
@Data
public class ProcessDataQuery {

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 工艺版本
     */
    private String processVersion;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 关联生产bom
     */
    private String bomName;

    /**
     * 生产bom版本
     */
    private String bomVersion;
}
