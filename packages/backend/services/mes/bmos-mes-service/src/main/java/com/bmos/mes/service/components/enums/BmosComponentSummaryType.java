package com.bmos.mes.service.components.enums;

/**
 * 组件分组统计类型
 * @author liang
 * @version 1.0.0
 * @date 2024/7/30 16:33
 */
public enum BmosComponentSummaryType {


    /**
     * 求和(列表中的属性求和)
     */
    SUM,

    /**
     * 计数(列表中的元素的个数)
     */
    SIZE,

    /**
     * 静态(回显列表中第一个属性值)
     */
    STATIC
}
