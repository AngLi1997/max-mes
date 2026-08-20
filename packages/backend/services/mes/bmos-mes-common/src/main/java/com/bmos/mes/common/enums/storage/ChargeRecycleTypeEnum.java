package com.bmos.mes.common.enums.storage;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 投料回收操作类型枚举
 */
@AllArgsConstructor
@Getter
public enum ChargeRecycleTypeEnum implements CommonEnum<String> {

    /**
     * 投料
     */
    CHARGE("CHARGE", "投料"),

    /**
     * 回收
     */
    RECYCLE("RECYCLE", "回收"),
    ;

    @EnumValue
    private final String value;

    @JsonValue
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

}
