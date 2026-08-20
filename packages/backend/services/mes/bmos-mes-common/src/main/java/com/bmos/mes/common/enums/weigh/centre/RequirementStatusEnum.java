package com.bmos.mes.common.enums.weigh.centre;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 称量需求状态
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 18:24
 */
@Getter
@AllArgsConstructor
public enum RequirementStatusEnum implements CommonEnum<Integer> {

    UN_PLANNED(0, "未规划"),
    UN_WEIGHED(1, "未称量"),
    WEIGHING(2, "称量中"),
    WEIGHED(3, "已完成"),
    EXPIRED(4, "已失效");

    @EnumValue
    private final Integer value;
    private final String name;
}
