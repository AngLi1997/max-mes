package com.bmos.mes.common.enums.audit;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowAuditStateEnum implements CommonEnum<String> {

    FLOW_AUDIT_FAIL_END("审核不通过流程结束", "FLOW_AUDIT_FAIL_END"),
    FLOW_AUDIT_PASS_END("审核通过流程结束", "FLOW_AUDIT_PASS_END");

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
