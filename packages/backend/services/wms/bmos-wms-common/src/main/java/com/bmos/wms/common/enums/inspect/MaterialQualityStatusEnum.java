package com.bmos.wms.common.enums.inspect;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 货品批次质量状态（与 MES MaterialQualityStatusEnum 同语义）。
 * <p>新建批次默认 {@link #QUARANTINE}；由检验流程驱动状态流转，
 * 入库 / 出库流程不应反向修改本字段。
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
