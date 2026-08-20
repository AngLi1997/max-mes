package com.bmos.mes.common.enums.weigh.centre;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 称量任务状态
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 18:24
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum implements CommonEnum<Integer> {

    EDIT(0, "编辑"),
    WAIT_SEND(1, "待下发"),
    SEND(2, "已下发"),
    EXECUTED(3, "已执行");

    @EnumValue
    private final Integer value;
    private final String name;
}
