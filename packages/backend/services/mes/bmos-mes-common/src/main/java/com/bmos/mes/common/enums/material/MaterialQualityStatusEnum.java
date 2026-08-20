package com.bmos.mes.common.enums.material;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 物料批次质量状态
 */
@Getter
@AllArgsConstructor
public enum MaterialQualityStatusEnum implements CommonEnum<String> {
    QUARANTINE("待验", "QUARANTINE"),
    QUALIFIED("合格", "QUALIFIED"),
    UNQUALIFIED("不合格", "UNQUALIFIED"),
    SAMPLED("已取样", "SAMPLED"),
    RESTRICTED_RELEASE("限制性放行", "RESTRICTED_RELEASE"),
    ;

    private final String name;
    @EnumValue
    private final String value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static MaterialQualityStatusEnum getEnumByValue(String value) {
        return Arrays.stream(MaterialQualityStatusEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(QUARANTINE);
    }
}
