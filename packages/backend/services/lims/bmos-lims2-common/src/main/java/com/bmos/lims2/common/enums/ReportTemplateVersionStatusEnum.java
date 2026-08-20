package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报告模板版本状态
 * 编辑中、确认、作废
 */
@Getter
@AllArgsConstructor
public enum ReportTemplateVersionStatusEnum implements KeyValueEnum<String> {

    EDITING("EDITING", "编辑中"),
    APPROVING("APPROVING", "审批中"),
    CONFIRMED("CONFIRMED", "确认"),
    REJECTED("REJECTED", "驳回"),
    VOIDED("VOIDED", "作废");

    @EnumValue
    private final String value;
    private final String name;
}


