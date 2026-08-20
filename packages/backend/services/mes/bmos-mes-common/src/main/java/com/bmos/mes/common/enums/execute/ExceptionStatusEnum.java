package com.bmos.mes.common.enums.execute;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 异常状态
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExceptionStatusEnum implements CommonEnum<String> {

    INVESTIGATING("0", "调查中"),
    HANDLED("1","已关闭"),
    CANCELED("2","已作废"),
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

    public static ExceptionStatusEnum getEnumByValue(String value) {
        return Arrays.stream(ExceptionStatusEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
