package com.bmos.lims2.common.enums;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowAuditCodeEnum implements CommonEnum<String> {

    COMPLETE_TYPE("规则", "completeType"),
    COUNTERSIGN("会签", "countersign"),
    OR_VISE("或签", "or_vise"),
    STRATEGY("策略", "strategy"),
    ALL_USER("所有待审人", "all_user"),
    ALL_ROLE("所有待审角色", "all_role"),
    MAKE("抄送人员类型","make");

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
