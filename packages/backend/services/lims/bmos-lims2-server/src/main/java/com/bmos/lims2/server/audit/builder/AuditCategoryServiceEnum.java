package com.bmos.lims2.server.audit.builder;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import com.bmos.lims2.server.audit.message.strategy.*;
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
public enum AuditCategoryServiceEnum implements KeyValueEnum<String> {

    CATEGORY_SCHEME_APPROVAL("检验方案审批", "120020002", InspectSchemeStrategy.class),
    CATEGORY_SAMPLE_AUDIT("样品审核", "120020010", SampleAuditStrategy.class),
    CATEGORY_REPORT_AUDIT("报告审批", "120020020", ReportAuditStrategy.class),
    METHOD_AUDIT("方法审批", "120020030", RecordAuditStrategy.class),
    OPERATE_RULE_AUDIT("操作规程审批","1200200040",OperateRuleAuditStrategy.class),
    STABILITY_SCHEME_AUDIT("稳定性方案审批", "120020003", StabilitySchemeAuditStrategy.class),
    STABILITY_RESULT_AUDIT("稳定性结果审核", "120020040", StabilityResultAuditStrategy.class);



    private final String name;

    private final String code;

    private final Class<? extends AbstractAuditMessageStrategy> service;

    public static List<String> codes() {
        return Arrays.stream(AuditCategoryServiceEnum.values()).map(AuditCategoryServiceEnum::getCode).collect(Collectors.toList());
    }

    public static Class<? extends AbstractAuditMessageStrategy> getService(String code) {
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
