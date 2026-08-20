package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CompareOperatorEnum implements KeyValueEnum<String> {

    LESS_THAN("LESS_THAN", "<"),
    LESS_THAN_OR_EQUAL("LESS_THAN_OR_EQUAL", "<="),
    EQUAL("EQUAL", "="),
    GREATER_THAN_OR_EQUAL("GREATER_THAN_OR_EQUAL", ">="),
    GREATER_THAN("GREATER_THAN", ">");

    @EnumValue
    private final String value;

    private final String name;
} 