package com.bmos.mes.common.enums.audit;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum FlowStateEnum implements CommonEnum<String> {

    ACTIVE("1", "审批中", "ACTIVE"),
    BACK_TO_PREV("2", "退回", "BACK_TO_PREV"),
    COMPLETE("4", "通过", "COMPLETE"),
    APPROVE_REJECT("5", "不通过", "APPROVE_REJECT");

    @EnumValue
    private String state;

    private String value;

    @EnumValue
    private String code;

    @Override
    public String getName() {
        return this.value;
    }

    @Override
    public String getValue() {
        return this.state;
    }

    public static FlowStateEnum getEnumByState(String state){
        return Arrays.asList(FlowStateEnum.values())
                .stream()
                .filter(item->item.getState().equals(state))
                .findFirst().get();
    }
}
