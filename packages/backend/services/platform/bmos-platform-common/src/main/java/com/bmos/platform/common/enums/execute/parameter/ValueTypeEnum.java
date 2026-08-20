package com.bmos.platform.common.enums.execute.parameter;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ValueTypeEnum implements CommonEnum<String> {
    STRING("STRING", "字符串"),
    NUMBER("NUMBER", "数字"),
    BOOLEAN("BOOLEAN", "布尔"),
    ENUM("ENUM", "枚举"),
    JSON("JSON", "json");

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
