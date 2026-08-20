package com.bmos.mes.service.mcp.vo;

import lombok.Data;

/**
 * 配方数据vo (mcp)
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 14:38
 */
@Data
public class ProcessDataVO {

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
