package com.bmos.lims2.common.enums;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowAuditMessageEnum implements CommonEnum<String> {

    AUDIT_TASK_MESSAGE("审核节点消息通知", "AUDIT_TASK_MESSAGE"),
    AUDIT_TASK_COPY_TO_MESSAGE("任务节点抄送通知", "AUDIT_TASK_COPY_TO_MESSAGE");

    private String name;

    private String value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
