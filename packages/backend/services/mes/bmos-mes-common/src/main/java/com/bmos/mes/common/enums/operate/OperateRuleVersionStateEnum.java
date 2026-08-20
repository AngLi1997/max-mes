package com.bmos.mes.common.enums.operate;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OperateRuleVersionStateEnum implements CommonEnum<String> {

    EDIT("编辑", "edit"),
    CONFIRM("确认", "confirm"),
    AUDIT("审核", "audit"),
    VALID("生效", "valid"),
    INVALID("失效", "invalid"),
    WAIT_VALID("待生效", "wait_valid");

    private String name;

    @EnumValue
    private String code;


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.code;
    }

    public static String getNameByCode(String code) {
        return Arrays.stream(OperateRuleVersionStateEnum.values())
                .filter(auditCategoryCodeEnum -> auditCategoryCodeEnum.getCode().equals(code))
                .map(OperateRuleVersionStateEnum::getName)
                .findAny()
                .orElse(null);
    }

    public static OperateRuleVersionStateEnum getEnumByCode(String code) {
        return Arrays.stream(OperateRuleVersionStateEnum.values())
                .filter(operateRuleVersionStateEnum -> operateRuleVersionStateEnum.getCode().equals(code))
                .findFirst()
                .orElse(EDIT);
    }
}
