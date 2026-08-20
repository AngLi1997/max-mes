package com.bmos.mes.common.enums.formula;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 允差类型
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ToleranceTypeEnum implements CommonEnum<Integer> {

    PERCENTAGE(0,"百分比"),
    FIXED_VALUE(1,"固定值"),
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

    public static ToleranceTypeEnum getEnumByValue(Integer value) {
        return Arrays.stream(ToleranceTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
