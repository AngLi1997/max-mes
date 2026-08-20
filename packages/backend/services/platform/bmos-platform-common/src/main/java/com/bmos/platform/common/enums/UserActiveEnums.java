package com.bmos.platform.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserActiveEnums implements CommonEnum<Integer> {
    /**
     * 激活状态
     */
    TO_BE_ACTIVATE("待激活", 0),
    ACTIVATE("激活", 1),
    PASSWORD_EXPIRED("密码过期", 2),
    PASSWORD_LOCK("密码锁定", 3);

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
