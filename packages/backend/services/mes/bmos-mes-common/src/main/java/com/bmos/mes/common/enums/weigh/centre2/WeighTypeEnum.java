package com.bmos.mes.common.enums.weigh.centre2;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeighTypeEnum implements CommonEnum<Integer> {
    ODDMENT(1, "余料称量"),
    NORMAL(2, "正常称量");

    @EnumValue
    private final Integer value;
    private final String name;
} 