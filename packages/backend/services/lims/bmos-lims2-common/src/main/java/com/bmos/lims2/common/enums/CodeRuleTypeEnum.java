package com.bmos.lims2.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CodeRuleTypeEnum {

    INSPECT_ORDER_NO("检验单编号规则", "INSPECT_ORDER_NO"),
    SAMPLE_NO("样品编号规则", "SAMPLE_NO"),
    INSPECTION_REPORT_NO("报告编号规则", "INSPECTION_REPORT_NO"),
    STABILITY_INSPECT_PLAN_NO("稳定性考察计划编号规则", "STABILITY_INSPECT_PLAN_NO");

    private final String name;

    private final String value;


}
