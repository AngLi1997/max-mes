package com.bmos.lims2.common.enums;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum NumberLimitTypeEnum implements CommonEnum<String> {

    SCOPE_LIMIT("范围限制", "SCOPE_LIMIT"),
    NUMBER_EQUALS("数值相等", "NUMBER_EQUALS");

    private final String name;
    private final String value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static NumberLimitTypeEnum getEnumByValue(String value) {
        return Arrays.stream(NumberLimitTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
