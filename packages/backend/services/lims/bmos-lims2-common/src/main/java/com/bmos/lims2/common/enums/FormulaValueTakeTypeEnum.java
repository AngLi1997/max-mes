package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 公式参数取值类型
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FormulaValueTakeTypeEnum implements CommonEnum<String> {

    LATEST_EFFECTIVE("LATEST_EFFECTIVE", "最新有效"),
    ALL_EFFECTIVE("ALL_EFFECTIVE", "所有有效"),
    ;

    @EnumValue
    private final String value;

    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FormulaValueTakeTypeEnum getEnumByValue(String value) {
        return Arrays.stream(FormulaValueTakeTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
