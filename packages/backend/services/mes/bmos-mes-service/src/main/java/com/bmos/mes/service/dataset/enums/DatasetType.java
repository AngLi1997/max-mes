package com.bmos.mes.service.dataset.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据集类型
 * @author liang
 * @version 1.0.0
 * @date 2024/8/13 14:41
 */
@Getter
@AllArgsConstructor
public enum DatasetType implements CommonEnum<String> {

    /**
     * 批记录数据(数据点)
     */
    POINT("POINT", "批记录数据(数据点)"),

    /**
     * 批签发引用
     */
    LOT_RELEASE_LINK("LOT_RELEASE_LINK", "批签发引用"),

    /**
     * 动态数据填报
     */
    DYNAMIC_REPORT("DYNAMIC_REPORT", "动态数据填报");


    @EnumValue
    private final String value;

    private final String name;
}
