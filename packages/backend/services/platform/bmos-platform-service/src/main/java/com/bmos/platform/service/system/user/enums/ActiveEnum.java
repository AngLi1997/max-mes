package com.bmos.platform.service.system.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum ActiveEnum implements CommonEnum<Integer> {

    /**
     * 激活状态
     */
    TO_BE_ACTIVATE(0,"待激活"),
    ACTIVATE(1,"激活"),
    PASSWORD_EXPIRED(2,"密码过期"),
    PASSWORD_LOCK(3,"密码锁定");

    @EnumValue
    private Integer code;

    @JsonValue
    private String name;

    public static ActiveEnum convertByCode(Integer code) {
        return Stream.of(values())
            .filter(bean -> bean.code.equals(code))
            .findAny()
            .orElse(null);
    }

    public static ActiveEnum convertByName(String name) {
        return Stream.of(values())
            .filter(bean -> bean.name.equals(name))
            .findAny()
            .orElse(null);
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
