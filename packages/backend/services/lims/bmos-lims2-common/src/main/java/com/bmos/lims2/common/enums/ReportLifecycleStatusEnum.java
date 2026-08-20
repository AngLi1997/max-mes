package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报告生命周期状态
 * 待审批、审批中、生效、作废
 */
@Getter
@AllArgsConstructor
public enum ReportLifecycleStatusEnum implements KeyValueEnum<String> {

    PENDING_CONFIRM("PENDING_CONFIRM", "待确认"),
    PENDING_APPROVAL("PENDING_APPROVAL", "待审批"),
    APPROVING("APPROVING", "审批中"),
    EFFECTIVE("EFFECTIVE", "生效"),
    VOIDED("VOIDED", "作废");

    @EnumValue
    private final String value;
    private final String name;
}


