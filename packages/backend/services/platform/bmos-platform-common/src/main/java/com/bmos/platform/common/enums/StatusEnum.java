package com.bmos.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum implements CommonEnum<Boolean> {
    ON(true, "启用"),
    OFF(false, "停用");

    @EnumValue
    private final Boolean value;
    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }
}
