package com.bmos.platform.service.system.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum GenderEnum implements CommonEnum<Integer> {

    /**
     * 性别
     */
    MAN(0,"男"),
    WOMAN(1,"女");

    @EnumValue
    private Integer code;

    @JsonValue
    private String name;

    public static GenderEnum convertByCode(Integer code) {
        return Stream.of(values())
            .filter(bean -> bean.code.equals(code))
            .findAny()
            .orElse(null);
    }

    public static GenderEnum convertByName(String name) {
        return Stream.of(values())
            .filter(bean -> bean.name.equals(name))
            .findAny()
            .orElse(null);
    }

    public static List<String> getNameList(){
        return Arrays.asList(GenderEnum.values())
                .stream()
                .map(GenderEnum::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
