package com.bmos.mes.common.enums.process.task;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 表达式节点类型：工序/工步
 * @Author: RJG
 */
@Getter
@AllArgsConstructor
public enum NodeTypeEnum implements CommonEnum<String> {
    
    PROCEDURE("工序节点","procedure_type"),
    STEP_OR_TASK("工步节点", "step_or_task");

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

    public static NodeTypeEnum getEnumByValue(String value){
        for (NodeTypeEnum e : Arrays.asList(NodeTypeEnum.values())) {
            if (e.getValue().equals(value)){
                return e;
            }
        }
        return null;
    }
}
