package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 稳定性考察计划状态枚举
 */
@AllArgsConstructor
@Getter
public enum StabilityInspectPlanStatusEnum implements KeyValueEnum<String> {

    PENDING("PENDING", "待开始"),

    IN_PROGRESS("IN_PROGRESS", "进行中"),

    COMPLETED("COMPLETED", "已完成"),

    PAUSED("PAUSED", "已暂停");

    @EnumValue
    private final String value;

    private final String name;
}
