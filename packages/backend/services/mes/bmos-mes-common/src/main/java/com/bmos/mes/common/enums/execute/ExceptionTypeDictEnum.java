package com.bmos.mes.common.enums.execute;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 异常类型字典
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExceptionTypeDictEnum implements CommonEnum<String> {

    OverLimitException("OverLimitException", "超限异常", "120090001001001"),
    ProductReviseException("ProductReviseException","生产修订异常", "120090001001002"),
    ;

    @EnumValue
    private final String value;

    private final String name;

    private final String codeValue;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static ExceptionTypeDictEnum getEnumByValue(String value) {
        return Arrays.stream(ExceptionTypeDictEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
