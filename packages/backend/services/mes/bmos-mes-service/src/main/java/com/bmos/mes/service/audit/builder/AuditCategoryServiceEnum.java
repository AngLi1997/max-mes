package com.bmos.mes.service.audit.builder;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.mes.service.audit.condition.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author renjinguang
 */

@Getter
@AllArgsConstructor
public enum AuditCategoryServiceEnum implements CommonEnum<String> {

    RECODE("记录审批", "12002000101", RecordAuditCondition.class),
    PROCESS("工艺审批", "12002000201", ProcessAuditConditon.class),
    PRODUCT_FORMULA("生产BOM审核", "12002000301", ProductAuditCondition.class),
    PRODUCT_PLAN("指令单审核", "12003000101", PlanAuditCondition.class),
    OPERATE_RULE_AUDIT("操作规程审批","12002000401",OperateRuleAuditCondition.class),
    BATCH_RECORD_ARCHIVE("批记录审批","12005000101",BatchRecordCondition.class),
    BATCH_SIGNATURE("批签发审核","12004000101",LotReleaseCondition.class);

    private final String name;

    private final String code;

    private final Class<? extends AbstractAuditDataCondition> service;

    public static List<String> codes() {
        return Arrays.stream(AuditCategoryServiceEnum.values()).map(AuditCategoryServiceEnum::getCode).collect(Collectors.toList());
    }

    public static Class<? extends AbstractAuditDataCondition> getService(String code) {
        return Arrays.stream(AuditCategoryServiceEnum.values())
                .filter(auditCategoryCodeEnum -> auditCategoryCodeEnum.getCode().equals(code))
                .map(AuditCategoryServiceEnum::getService)
                .findAny()
                .orElse(null);
    }

    public static AuditCategoryServiceEnum getEnumByCode(String code){
        for (AuditCategoryServiceEnum item : Arrays.asList(AuditCategoryServiceEnum.values())) {
            if (StrUtil.equals(item.getCode(),code)){
                return item;
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return code;
    }
}
