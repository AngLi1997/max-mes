package com.bmos.mes.common.enums.formula;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 物料数量类型
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum QuantityTypeEnum implements CommonEnum<Integer> {

    STANDARD_QUANTITY(0,"标准量"),
    FIXED_QUANTITY(1,"固定量"),
    APPROPRIATE_QUANTITY(2,"适量"),
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

    public static QuantityTypeEnum getEnumByValue(Integer value) {
        return Arrays.stream(QuantityTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }


}
