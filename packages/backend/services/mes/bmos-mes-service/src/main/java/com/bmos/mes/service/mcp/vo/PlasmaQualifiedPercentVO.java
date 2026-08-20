package com.bmos.mes.service.mcp.vo;

import lombok.Data;

/**
 * 在库血浆的检疫期合格和不合格的比例
 * @author liang
 * @version 1.0.0
 * @date 2025/6/12 10:52
 */
@Data
public class PlasmaQualifiedPercentVO {

    /**
     * 分类
     */
    private String label;

    /**
     * 在库血浆数量
     */
    private String quantity;

    /**
     * 单位
     */
    private String unit;
}
