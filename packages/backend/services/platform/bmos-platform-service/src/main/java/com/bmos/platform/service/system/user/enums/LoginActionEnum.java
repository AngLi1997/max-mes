package com.bmos.platform.service.system.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum LoginActionEnum implements CommonEnum<Integer> {

    lOG_OUT(0, "登出"),
    LOG_IN(1, "登录");

    @EnumValue
    private Integer code;

    @JsonValue
    private String name;

    public static LoginActionEnum convertByCode(Integer code) {
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElse(null);
    }

    public static LoginActionEnum convertByName(String name) {
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
