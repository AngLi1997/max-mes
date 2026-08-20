package com.bmos.platform.common.enums.expression;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ExpressionStatusEnum implements CommonEnum<Integer> {
    /**
     * 编辑->验证通过
     */
    EDIT(0, "编辑"),
    CONFIRMED(1, "确认"),

    /**
     * 编辑->验证通过->确认/编辑
     */
    VERIFIED(2, "验证通过"),
    ;
    @EnumValue
    private final Integer value;
    private final String name;

    public static ExpressionStatusEnum getByValue(Integer value) {
        return Arrays.stream(ExpressionStatusEnum.values())
                .filter(item -> item.value.equals(value))
                .findFirst()
                .orElse(null);
    }

}
