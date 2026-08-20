package com.bmos.expression.enums;

import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.RoundingMode;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum RoundingEnum implements KeyValueEnum<String> {

    /**
     * 四舍五入
     */
    ROUNDING_FIVE("四舍五入", "roundingFive", RoundingMode.HALF_UP),

    /**
     * 四舍六入五成双
     */
    ROUNDING_SIX("四舍六入五成双", "roundingSix", RoundingMode.HALF_EVEN),

    /**
     * 向上舍入
     */
    ROUNDING_UP("向上舍入", "roundingUp", RoundingMode.UP),

    /**
     * 向下舍入
     */
    ROUNDING_DOWN("向下舍入", "roundingDown", RoundingMode.DOWN);

    private String label;

    private String code;

    private RoundingMode mapping;

    @Override
    public String getName() {
        return this.label;
    }

    @Override
    public String getValue() {
        return this.code;
    }

    public static RoundingEnum getEnumByCode(String code) {
        return Arrays.stream(RoundingEnum.values())
            .filter(roundingEnum -> roundingEnum.getCode().equals(code))
            .findAny()
            .orElse(ROUNDING_SIX);
    }
}
