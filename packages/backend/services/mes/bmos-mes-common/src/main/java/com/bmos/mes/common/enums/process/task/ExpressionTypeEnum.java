package com.bmos.mes.common.enums.process.task;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ExpressionTypeEnum implements CommonEnum<String> {
    
    EXECUTE_CONDITION("执行条件","execute_condition"),
    COMPLETE_CONDITION("完成条件", "complete_condition");

    private final String name;
    @EnumValue
    private final String value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static ExpressionTypeEnum getEnumByValue(String value){
        for (ExpressionTypeEnum e : Arrays.asList(ExpressionTypeEnum.values())) {
            if (e.getValue().equals(value)){
                return e;
            }
        }
        return null;
    }
}
