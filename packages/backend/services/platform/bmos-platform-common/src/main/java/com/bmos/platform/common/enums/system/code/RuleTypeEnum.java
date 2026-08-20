package com.bmos.platform.common.enums.system.code;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleTypeEnum implements CommonEnum<String> {
    CONSTANT("CONSTANT", "常量"),
    PARAMETER("PARAMETER", "参数"),
    DATE("DATE", "日期"),
    SEQUENCE("SEQUENCE", "流水号")
    ;

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
