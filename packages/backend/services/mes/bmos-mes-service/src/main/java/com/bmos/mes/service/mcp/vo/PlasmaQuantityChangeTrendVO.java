package com.bmos.mes.service.mcp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 投浆量变换趋势
 * @author liang
 * @version 1.0.0
 * @date 2025/6/12 10:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlasmaQuantityChangeTrendVO {

    /**
     * 月份
     */
    private String month;

    /**
     * 投浆量
     */
    private String quantity;

    /**
     * 单位
     */
    private String unit;
}
