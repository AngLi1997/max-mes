package com.bmos.mes.common.enums.audit;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BatchReleaseAuditStatusEnum implements CommonEnum<Integer> {

    AUDIT_REFUSED(0, "审核不通过"),
    NOT_AUDIT(1, "未审核"),
    AUDIT_ING(2, "审核中"),
    AUDIT_SUCCESS(3, "审核通过");

    @EnumValue
    private final Integer value;

    private final String name;

    public static String getDescByValue(Integer value) {
        return Arrays.stream(BatchReleaseAuditStatusEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .map(BatchReleaseAuditStatusEnum::getName)
                .findFirst()
                .orElse("");
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BatchReleaseAuditStatusEnum getEnumByValue(Integer value) {
        return Arrays.stream(BatchReleaseAuditStatusEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
