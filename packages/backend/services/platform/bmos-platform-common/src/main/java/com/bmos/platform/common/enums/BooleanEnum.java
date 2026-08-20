package com.bmos.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BooleanEnum implements CommonEnum<String> {
    TRUE("TRUE", "真"),
    FALSE("FALSE", "假");

    @EnumValue
    private final String value;
    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
