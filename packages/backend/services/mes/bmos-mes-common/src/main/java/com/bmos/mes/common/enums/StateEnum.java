package com.bmos.mes.common.enums;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StateEnum implements CommonEnum<Boolean> {
    ON(true, "启用"),
    OFF(false, "停用");

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
