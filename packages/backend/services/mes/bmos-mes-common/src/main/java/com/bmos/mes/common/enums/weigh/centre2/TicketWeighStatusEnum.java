package com.bmos.mes.common.enums.weigh.centre2;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicketWeighStatusEnum implements CommonEnum<Integer> {

    UN_WEIGHED(1, "未称量"),
    WEIGHING(2, "称量中"),
    WEIGHED(3, "已完成"),
    ;
    @EnumValue
    private final Integer value;
    private final String name;

}
