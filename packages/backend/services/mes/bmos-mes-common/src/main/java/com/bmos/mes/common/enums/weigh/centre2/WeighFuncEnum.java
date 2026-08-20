package com.bmos.mes.common.enums.weigh.centre2;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeighFuncEnum implements CommonEnum<Integer> {
    MANUAL(1, "手动称量"),
    MATERIAL(2, "物料称量");

    @EnumValue
    private final Integer value;
    private final String name;
}