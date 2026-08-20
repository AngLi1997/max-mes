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
public enum NumberPatternEnum implements CommonEnum<Integer> {

    REVISION_NUMBER(0, "修约数"),
    PERCENTAGE(1, "百分比"),
    SCIENTIFIC_NOTATION(2, "科学计数法"),
    ;

    @EnumValue
    private final Integer value;

    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static NumberPatternEnum getEnumByValue(Integer value) {
        return Arrays.stream(NumberPatternEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
