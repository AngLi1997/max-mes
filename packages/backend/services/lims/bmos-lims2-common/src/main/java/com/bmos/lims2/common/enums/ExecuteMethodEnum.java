package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;

/**
 * @Description: 执行方式枚举（用于方案分析项配置），取值：LIMS、ELN
 * @Author: yigaohui
 * @Date: 2025/10/28 00:00
 */
public enum ExecuteMethodEnum implements CommonEnum<String> {

    LIMS("LIMS", "LIMS"),
    ELN("ELN", "ELN"),
    ;

    @EnumValue
    private final String value;

    private final String name;

    ExecuteMethodEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}


