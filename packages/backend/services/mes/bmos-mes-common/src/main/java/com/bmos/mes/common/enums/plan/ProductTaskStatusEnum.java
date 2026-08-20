package com.bmos.mes.common.enums.plan;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.mes.common.enums.process.ProcessStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProductTaskStatusEnum implements CommonEnum<String> {

    DISABLE("不可用", "DISABLE"),
    ENABLE("可用", "ENABLE"),
    ACTIVATED("已激活", "ACTIVATED"),
    COMPLETE("完成", "COMPLETE");

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

    public static ProductTaskStatusEnum getEnumByValue(String value) {
        return Arrays.stream(ProductTaskStatusEnum.values())
                .filter(stateEnum -> stateEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
