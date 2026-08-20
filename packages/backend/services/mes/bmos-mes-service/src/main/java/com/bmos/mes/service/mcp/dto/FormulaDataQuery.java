package com.bmos.mes.service.mcp.dto;

import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 14:49
 */
@Data
public class FormulaDataQuery {

    /**
     * 关联生产bom
     */
    private String bomName;

    /**
     * 生产bom版本
     */
    private String bomVersion;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productCode;
}
