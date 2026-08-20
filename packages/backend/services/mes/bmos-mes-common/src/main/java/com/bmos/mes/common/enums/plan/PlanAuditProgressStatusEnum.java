package com.bmos.mes.common.enums.plan;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.CommonEnumVO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PlanAuditProgressStatusEnum implements CommonEnum<String> {

    PENDING_SUBMISSION("待提交","PENDING_SUBMISSION", "DISABLE"),
    SUBMITTED("已提交","SUBMITTED", "ENABLE"),
    UNDER_AUDIT("审核中","UNDER_AUDIT", "ACTIVATED"),
    AUDIT_COMPLETED("审核完成","AUDIT_COMPLETED", "COMPLETE");

    private final String name;
    @EnumValue
    private final String value;

    private final String mappingFlowStateValue;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static PlanAuditProgressStatusEnum getEnumByName(CommonEnumVO<String> commonEnumVO) {
        return Arrays.stream(PlanAuditProgressStatusEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(commonEnumVO.getValue()))
                .findFirst()
                .orElse(null);
    }

    public static PlanAuditProgressStatusEnum getEnumByMappingValue(String mappingValue) {
        return Arrays.stream(PlanAuditProgressStatusEnum.values())
                .filter(statusEnum -> statusEnum.getMappingFlowStateValue().equals(mappingValue))
                .findFirst()
                .orElse(null);
    }
}
