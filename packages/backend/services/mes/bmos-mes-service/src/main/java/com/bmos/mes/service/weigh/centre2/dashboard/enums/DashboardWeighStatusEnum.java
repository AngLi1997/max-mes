package com.bmos.mes.service.weigh.centre2.dashboard.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 看板称量需求状态
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 18:24
 */
@Getter
@AllArgsConstructor
public enum DashboardWeighStatusEnum implements CommonEnum<Integer> {

    SEND(1, "已下发"),
    WEIGHING(2, "称量中"),
    WEIGHED(3, "已完成");

    @EnumValue
    private final Integer value;
    private final String name;
}
