package com.bmos.platform.common.enums.tag;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/8 12:53
 */
@Getter
@AllArgsConstructor
public enum PrintCmdType implements CommonEnum<String> {

    ZPL("ZPL", "ZPL"),

    EPL("EPL", "EPL");

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
