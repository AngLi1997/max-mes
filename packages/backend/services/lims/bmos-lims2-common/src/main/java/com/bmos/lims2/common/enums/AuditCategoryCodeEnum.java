package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AuditCategoryCodeEnum implements CommonEnum<String> {

    SCHEME_AUDIT("检验方案审批", "120020002", "120020002"),
    SAMPLE_AUDIT("样品审核", "120020010", "120020010"),
    REPORT_AUDIT("报告审批", "120020020", "120020020"),
    METHOD_AUDIT("方法审批", "120020030", "120020030"),

    OPERATE_RULE_BLOCK("操作规程停用审核","1200200050","120020013"),
    OPERATE_RULE_START("操作规程启用审核","1200200040","120020013"),

    STABILITY_SCHEME_AUDIT("稳定性方案审批", "120020003", "120020003"),

    STABILITY_RESULT_AUDIT("稳定性结果审核", "120020040", "120020040");

    private String name;

    @EnumValue
    private String code;

    private String menuCode;

    public static String getMenuCodeByCode(String code) {
        return Arrays.stream(AuditCategoryCodeEnum.values())
            .filter(auditCategoryCodeEnum -> auditCategoryCodeEnum.getCode().equals(code))
            .map(AuditCategoryCodeEnum::getMenuCode)
            .findAny()
            .orElse(null);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.code;
    }
}
