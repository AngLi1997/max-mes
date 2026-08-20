package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * 任务操作枚举
 *
 * @className: TaskOperationTypeEnum
 * @author: yigaohui
 * @date: 2025/8/21 15:48
 * @Version: 1.0
 * @description:
 */
@Getter
@AllArgsConstructor
public enum TaskOperationTypeEnum implements KeyValueEnum<String> {


    CREATE("CREATE", "创建"),
    ASSIGNMENT("ASSIGNMENT", "分配"),
    CLAIM("CLAIM", "领取"),
    RETURN("RETURN", "退回"),
    APPROVAL_SUBMIT("APPROVAL_SUBMIT", "提交审批"),
    APPROVAL_PASS("APPROVAL_PASS", "审批通过"),
    APPROVAL_REJECT("APPROVAL_REJECT", "审批不通过"),
    REVIEW_PASS("REVIEW_PASS", "复核通过"),
    REVIEW_REJECT("REVIEW_REJECT", "复核不通过"),
    TERMINATED("TERMINATED", "终止"),
    COMPLETION("COMPLETION", "完成"),
    ENTRY_STATUS_CHANGE("ENTRY_STATUS_CHANGE", "录入状态变更"),
    ENTRY_VALUE_CREATE("ENTRY_VALUE_CREATE", "数据点录入"),
    ENTRY_VALUE_MODIFY("ENTRY_VALUE_MODIFY", "数据点修改"),
    ENTRY_START("ENTRY_START", "开始录入"),
    ENTRY_COMPLETE("ENTRY_COMPLETE", "完成"),
    SAMPLE_AUDIT_SUBMIT("SAMPLE_AUDIT_SUBMIT", "发起样品审核"),
    TEST_TIME_SET("TEST_TIME_SET", "设置检验时间"),
    JUDGMENT_SET("JUDGMENT_SET", "设置检验结论"),
    ELN_DATA_MODIFY("ELN_DATA_MODIFY", "ELN数据修改");

    @EnumValue
    private final String value;
    private final String name;
}
