package com.bmos.mes.common.enums.preparation;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 量取状态
 */
@Getter
@AllArgsConstructor
public enum MeasureStatusEnum implements CommonEnum<String> {
    UNMEASURED("未量取", "UNMEASURED"),
    MEASURING("量取中", "MEASURING"),
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
