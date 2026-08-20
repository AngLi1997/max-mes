package com.bmos.lims2.server.operate.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OperateRuleVersionStateEnum implements CommonEnum<String> {

    EDIT("编辑", "edit"),
    AUDIT("审核", "audit"),
    VALID("生效", "valid"),
    INVALID("失效", "invalid");

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
