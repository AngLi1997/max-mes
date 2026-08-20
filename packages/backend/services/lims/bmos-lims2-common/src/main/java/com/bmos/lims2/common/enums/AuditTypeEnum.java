package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AuditTypeEnum implements CommonEnum<String> {

    OPERATE_RULE_BLOCK("停用审批", "1200200050", "block"),
    OPERATE_RULE_START("启用审批", "1200200040", "start");

    private String name;

    private String code;

    @EnumValue
    private String auditType;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.code;
    }

    public static String getNameByType(String auditType) {
        return Arrays.stream(AuditTypeEnum.values())
                .filter(auditTypeEnum -> auditTypeEnum.getAuditType().equals(auditType))
                .map(AuditTypeEnum::getName)
                .findAny()
                .orElse(null);
    }

    public static AuditTypeEnum getEnumByType(String auditType) {
        return Arrays.stream(AuditTypeEnum.values())
                .filter(auditTypeEnum -> auditTypeEnum.getAuditType().equals(auditType))
                .findAny()
                .orElse(null);
    }
}
