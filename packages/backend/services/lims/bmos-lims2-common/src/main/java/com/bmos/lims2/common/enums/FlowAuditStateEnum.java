package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowAuditStateEnum implements CommonEnum<Integer> {
    DESIGN("编辑", 1),
    STATE("生效", 2),
    HISTORY("失效", 3);


    private String name;
    @EnumValue
    private Integer value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static FlowAuditStateEnum getEnumByCode(Integer value){
        for (FlowAuditStateEnum stateEnum : FlowAuditStateEnum.values()) {
            if (stateEnum.getValue().equals(value)){
                return stateEnum;
            }
        }
        return null;
    }
}
