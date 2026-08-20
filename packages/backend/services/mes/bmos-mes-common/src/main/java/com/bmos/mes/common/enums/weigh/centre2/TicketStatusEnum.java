package com.bmos.mes.common.enums.weigh.centre2;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 称量工单任务状态
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/4 18:24
 */
@Getter
@AllArgsConstructor
public enum TicketStatusEnum implements CommonEnum<Integer> {

    EDIT(0, "编辑中"),
    SEND(1, "已下发"),
    EXECUTED(2, "已完成"),
    CANCELED(3, "已取消");

    @EnumValue
    private final Integer value;
    private final String name;
}
