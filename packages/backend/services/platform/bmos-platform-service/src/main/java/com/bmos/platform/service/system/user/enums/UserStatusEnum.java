package com.bmos.platform.service.system.user.enums;

import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatusEnum implements KeyValueEnum<Integer> {

    /**
     * 启停状态
     */
    ON("启用",1),
    OFF("停用",0);

    private final String name;
    private final Integer value;


    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }
}
