package com.bmos.mes.common.enums.formula;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 折干折纯类型
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DryAndPureTypeEnum implements CommonEnum<Integer> {

    NO_TYPE(0, "无"),
    PURE(1,"折纯"),
    DRY_PURE(2,"折干折纯"),
    DRY_PURE_WITH_PARAM(3,"折干折纯带参数"),
    ;

    @EnumValue
    private final Integer value;

    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static DryAndPureTypeEnum getEnumByValue(Integer value) {
        return Arrays.stream(DryAndPureTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
