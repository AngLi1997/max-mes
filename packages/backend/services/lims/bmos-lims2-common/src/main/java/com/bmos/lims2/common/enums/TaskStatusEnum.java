package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 任务状态枚举
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum implements KeyValueEnum<String> {

    PENDING_ASSIGNMENT("PENDING_ASSIGNMENT", "待分配"),
    RETURN_PENDING_APPROVAL("RETURN_PENDING_APPROVAL", "退回待审批"),
    PENDING_COMPLETION("PENDING_COMPLETION", "待完成"),
    IN_PROGRESS("IN_PROGRESS", "进行中"),
    TO_REVIEW("TO_REVIEW", "待复核"),
    REVIEW_PASSED("REVIEW_PASSED", "复核通过"),
    REVIEW_REJECTED("REVIEW_REJECTED", "复核不通过"),
    SAMPLE_AUDIT_PENDING("SAMPLE_AUDIT_PENDING", "待样品审核"),
    SAMPLE_AUDIT_REJECTED("SAMPLE_AUDIT_REJECTED", "样品审核不通过"),
    TERMINATED("TERMINATED", "已终止"),
    COMPLETED("COMPLETED", "已完成");

    @EnumValue
    private final String value;
    private final String name;
}