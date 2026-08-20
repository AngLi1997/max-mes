package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Description: 舍入规则枚举
 * @Author: yigaohui
 * @Date: 2025/10/31 10:20
 */
public enum RoundingRuleEnum {

    UP("UP", "向上"),
    DOWN("DOWN", "向下");

    @EnumValue
    private final String value;

    private final String name;

    RoundingRuleEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() { return value; }
    public String getName() { return name; }
}


