package com.bmos.mes.common.enums.plan;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.CommonEnumVO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProductPlanTypeEnum implements CommonEnum<String> {

    PRODUCT("生产批次", "PRODUCT", "A"),
    EXPERIMENT("实验批次", "EXPERIMENT", "B"),
    VERIFY("验证批次", "VERIFY", "C");

    private final String name;
    @EnumValue
    private final String value;

    /**
     * 编号规则映射参数
     */
    private final String codeParamMapping;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static ProductPlanTypeEnum getEnumByName(CommonEnumVO<String> commonEnumVO) {
        return Arrays.stream(ProductPlanTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(commonEnumVO.getValue()))
                .findFirst()
                .orElse(null);
    }

    public static String getNameByValue(String value) {
        return Optional.ofNullable(Arrays.stream(ProductPlanTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst().get().getName())
                .orElse(null);
    }
}
