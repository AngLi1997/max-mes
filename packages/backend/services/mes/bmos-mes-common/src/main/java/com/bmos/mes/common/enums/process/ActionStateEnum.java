package com.bmos.mes.common.enums.process;

import com.bmos.common.base.enums.CommonEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionStateEnum implements CommonEnum<String> {

    EDIT("编辑", "edit"),
    FRESH_EDIT("重新编辑","fresh_edit"),
    APPROVAL("审批", "approval"),
    CONFIRM("确认", "confirm"),
    VALID("生效", "valid"),
    INVALID("失效", "invalid"),
    WAIT_VALID("待生效", "wait_valid");

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
