package com.bmos.mes.service.trace.material.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 物料追溯类型
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 14:51
 */
@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MaterialTraceType implements CommonEnum<Integer> {

    CONSUME(1, "消耗"),

    OUTPUT(2, "产出");

    @EnumValue
    private final Integer value;

    private final String name;

    public static MaterialTraceType getByValue(Integer value) {
        for (MaterialTraceType materialTraceType : MaterialTraceType.values()) {
            if (materialTraceType.getValue().equals(value)) {
                return materialTraceType;
            }
        }
        return null;
    }
}
