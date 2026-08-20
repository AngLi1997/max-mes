package com.bmos.mes.common.enums.plan;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeRuleTypeEnum implements CommonEnum<String> {

    PRODUCT_PLAN_BATCH_NO("生产批号规则", "PRODUCT_PLAN_BATCH_NO"),
    PRODUCT_PLAN_NO("生产计划批号规则", "PRODUCT_PLAN_NO");

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
}
