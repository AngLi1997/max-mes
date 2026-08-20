package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 稳定性考察样品状态枚举
 */
@AllArgsConstructor
@Getter
public enum StabilityPlanSampleStatusEnum implements KeyValueEnum<String> {

    /**
     * 待取样 — 计划已建，等待实际批次生产后取样
     */
    PENDING("PENDING", "待取样"),

    /**
     * 已取样 — 样品已取，等待接收
     */
    SAMPLED("SAMPLED", "已取样"),

    /**
     * 已接收 — 样品已接收，稳定性考察开始
     */
    RECEIVED("RECEIVED", "已接收"),

    /**
     * 待销毁 — 考察计划已完成，等待销毁
     */
    PENDING_DESTROY("PENDING_DESTROY", "待销毁"),

    /**
     * 已销毁
     */
    DESTROYED("DESTROYED", "已销毁");

    @EnumValue
    private final String value;

    private final String name;
}
