package com.bmos.mes.common.enums.preparation;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 量取类型
 */
@Getter
@AllArgsConstructor
public enum MeasureModeEnum implements CommonEnum<String> {
    EQUIPMENT_MEASURE("设备量取", "EQUIPMENT_MEASURE"),
    MANUAL_MEASURE("手动量取", "MANUAL_MEASURE")
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

    public static MeasureModeEnum getEnumByValue(String value) {
        return Arrays.stream(MeasureModeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
