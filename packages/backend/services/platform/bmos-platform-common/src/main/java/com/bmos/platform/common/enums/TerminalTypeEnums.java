package com.bmos.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TerminalTypeEnums implements KeyValueEnum<Integer> {
    PC("pc", 0),
    PAD("pad", 1);

    private final String name;
    @EnumValue
    private final Integer value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
