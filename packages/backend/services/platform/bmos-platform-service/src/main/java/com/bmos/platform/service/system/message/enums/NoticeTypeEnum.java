package com.bmos.platform.service.system.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知类型枚举
 */
@AllArgsConstructor
@Getter
public enum NoticeTypeEnum {

    AUDIT_INFORMATION(0, "审核信息"),
    WARNING_INFORMATION(1, "预警信息");

    private final Integer code;
    private final String name;
}