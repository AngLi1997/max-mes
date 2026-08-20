package com.bmos.mes.common.enums.audit;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName 待办类型枚举
 * @Description
 * @Author Ren Jin Guang
 * @Date 2024/11/19 14:07
 */
@AllArgsConstructor
@Getter
public enum FlowToDoTypeEnum implements CommonEnum<String> {

    PRESENT_TODO("当前待办", "present_todo"),
    FUTURE_TODO("计划待办", "future_todo");

    private String name;

    private String value;
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
