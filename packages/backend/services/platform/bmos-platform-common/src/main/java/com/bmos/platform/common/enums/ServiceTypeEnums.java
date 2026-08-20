package com.bmos.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ServiceTypeEnums implements CommonEnum<String> {
    MES("MES", "MES"),;

    private final String name;

    @EnumValue
    private final String value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
}
