package com.bmos.wms.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeUnitEnum implements KeyValueEnum<Integer> {

    HOUR(0, "时"),
    DAY(1, "天"),
    MONTH(2, "月"),
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
}
