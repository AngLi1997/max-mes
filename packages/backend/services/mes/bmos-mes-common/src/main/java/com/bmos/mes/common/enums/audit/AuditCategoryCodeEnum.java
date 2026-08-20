package com.bmos.mes.common.enums.audit;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AuditCategoryCodeEnum implements CommonEnum<String> {

    RECODE("记录审批", "12002000101", "120020002"),
    PROCESS("工艺审批", "12002000201", "120020007"),
    PRODUCT_FORMULA("配方审批", "12002000301", "120020005"),
    PRODUCT_PLAN("生产计划审批", "12003000101", "120030002"),
    /**
     * 批签发审核中的code为categoryCode 若根据categoryCode与工艺id无法查找到 则根据此code直接查询内置的批签发审核流程
     */
    BATCH_SIGNATURE("批签发审核", "12004000101", "120040005"),
    OPERATE_RULE_BLOCK("操作规程停用审核","12002000501","120020013"),
    OPERATE_RULE_START("操作规程启用审核","12002000401","120020013"),
    /**
     * 批记录审核中的code为categoryCode 若根据categoryCode与工艺id无法查找到绑定关系 则根据此code直接查询内置的批记录审核流程
     */
    BATCH_RECORD_ARCHIVE("批记录审批", "12005000101", "120080003");

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
