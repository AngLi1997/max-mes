package com.bmos.mes.common.enums.preparation;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 量取类型
 */
@Getter
@AllArgsConstructor
public enum MeasureTypeEnum implements CommonEnum<String> {
    LIQUID_MEASURE("配液量取", "LIQUID_MEASURE"),
    ODD_LIQUID_MEASURE("余液量取", "ODD_LIQUID_MEASURE")
    ;

    private final String name;
    @EnumValue
    private final String value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
