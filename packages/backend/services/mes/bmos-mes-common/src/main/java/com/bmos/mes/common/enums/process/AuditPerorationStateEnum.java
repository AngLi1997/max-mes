package com.bmos.mes.common.enums.process;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditPerorationStateEnum implements CommonEnum<String> {

    ELIGIBLE("合格", "ELIGIBLE"),
    NOT_ELIGIBLE("不合格", "NOT_ELIGIBLE"),
    RESTS("其他", "RESTS");

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
