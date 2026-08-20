package com.bmos.mes.common.enums.process;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProcessStateEnum implements CommonEnum<Integer> {

    INACTIVE("未激活",0),
    IS_ACTIVE("已激活",2),
    ACTIVE("进行中",1),
    COMPLETE("已完成",4),
    IS_END("已结束",3);

    private final String name;
    @EnumValue
    private final Integer value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue(){
        return value;
    }


    public static ProcessStateEnum getEnumByValue(Integer state) {
        return Arrays.stream(ProcessStateEnum.values())
                .filter(stateEnum -> stateEnum.getValue().equals(state))
                .findFirst()
                .orElse(null);
    }
}
