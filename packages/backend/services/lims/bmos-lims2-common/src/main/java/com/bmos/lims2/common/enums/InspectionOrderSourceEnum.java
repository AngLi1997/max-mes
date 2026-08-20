package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 检验单来源枚举
 */
@AllArgsConstructor
@Getter
public enum InspectionOrderSourceEnum implements KeyValueEnum<String> {

    /**
     * 常规请验（默认）
     */
    REGULAR("REGULAR", "常规请验"),

    /**
     * 稳定性考察时间点任务自动创建
     */
    STABILITY("STABILITY", "稳定性考察");

    @EnumValue
    private final String value;

    private final String name;
}
