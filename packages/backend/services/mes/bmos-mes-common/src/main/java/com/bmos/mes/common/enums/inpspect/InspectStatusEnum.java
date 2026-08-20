package com.bmos.mes.common.enums.inpspect;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InspectStatusEnum implements KeyValueEnum<Integer> {

    PENDING("检验中", 1),
    FINISHED("已完成", 2),
    REJECTED("已退回", 3),

    ;

    private final String name;

    @EnumValue
    private final Integer value;

}
