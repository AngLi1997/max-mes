package com.bmos.mes.common.enums.preparation;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 配液量取阶段
 */
@Getter
@AllArgsConstructor
public enum MeasureStageEnum implements CommonEnum<String> {
    LIQUID_MEASURE("配液量取", "UNMEASURED"),
    LIQUID_ODD("余液量取", "MEASURING"),
    COMPLETED("已完成", "COMPLETED")
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
