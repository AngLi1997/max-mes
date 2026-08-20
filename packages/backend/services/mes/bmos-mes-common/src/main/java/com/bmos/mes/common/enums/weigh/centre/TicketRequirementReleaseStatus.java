package com.bmos.mes.common.enums.weigh.centre;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工单需求发布状态枚举
 * @author liang
 * @version 1.0.0
 * @date 2025/5/21
 */
@Getter
@AllArgsConstructor
public enum TicketRequirementReleaseStatus implements CommonEnum<Integer> {

    /**
     * 编辑中
     */
    EDIT(0, "编辑中"),

    /**
     * 已确认
     */
    RELEASE(1, "已确认"),

    /**
     * 已完成
     */
    FINISHED(2, "已完成"),

    /**
     * 已取消
     */
    CANCELED(3, "已取消");

    @EnumValue
    private final Integer value;
    private final String name;
} 