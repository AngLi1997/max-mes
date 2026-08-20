package com.bmos.mes.common.enums.record;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ScopeLimitTypeEnum implements CommonEnum<String> {

    SCOPE_LIMIT("范围限制", "0"),
    NUMBER_EQUALS("数值相等", "1");

    private final String name;

    @JsonValue
    @EnumValue
    private final String value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static ScopeLimitTypeEnum getEnumByValue(String value) {
        return Arrays.stream(ScopeLimitTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
