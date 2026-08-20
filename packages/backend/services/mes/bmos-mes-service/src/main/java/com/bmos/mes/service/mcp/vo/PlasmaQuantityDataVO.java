package com.bmos.mes.service.mcp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/6/12 10:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlasmaQuantityDataVO {

    /**
     * 年份
     */
    private String year;

    /**
     * 采浆量
     */
    private List<String> quantity;

    /**
     * 单位
     */
    private String unit;
}
