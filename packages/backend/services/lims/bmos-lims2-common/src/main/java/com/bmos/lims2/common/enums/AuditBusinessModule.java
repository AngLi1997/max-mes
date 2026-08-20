package com.bmos.lims2.common.enums;

/**
 * 审批业务模块枚举
 */
public enum AuditBusinessModule {
    // 流程配置
    AUDIT_CONFIG("AUDIT_CONFIG", "流程配置"),

    // 检验方案
    INSPECT_SCHEME("INSPECT_SCHEME", "检验方案"),
    SAMPLE_AUDIT("SAMPLE_AUDIT", "样品审核"),
    REPORT_AUDIT("REPORT_AUDIT", "报告审核"),
    METHOD_AUDIT("METHOD_AUDIT", "方法审核"),
    OPERATE_RULE("OPERATE_RULE", "操作规程审核"),

    // 稳定性方案
    STABILITY_SCHEME("STABILITY_SCHEME", "稳定性方案"),

    // 稳定性结果审核
    STABILITY_RESULT_AUDIT("STABILITY_RESULT_AUDIT", "稳定性结果审核"),

    // 留样管理
    RETENTION_SAMPLE_MANAGE("RETENTION_SAMPLE_MANAGE", "留样样品管理"),

    // 稳定性样品管理
    STABILITY_SAMPLE_MANAGE("STABILITY_SAMPLE_MANAGE", "稳定性样品管理");

    private final String code;
    private final String name;

    AuditBusinessModule(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
