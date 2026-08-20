package com.bmos.platform.common.enums.system.code;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VersionStatusEnum implements CommonEnum<String> {
    EDIT("EDIT", "编辑"),
    CONFIRM("CONFIRM", "确认")
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
