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
public enum ProductPlanStartEnum implements CommonEnum<String> {

    WAIT("等待", "WAIT"),
    STARTING ("开始", "STARTING"),
    END("已完成", "END"),
    TERMINATION("终止", "TERMINATION"),
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
    public static ProductPlanStartEnum getEnumByName(CommonEnumVO<String> commonEnumVO) {
        return Arrays.stream(ProductPlanStartEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(commonEnumVO.getValue()))
                .findFirst()
                .orElse(null);
    }
}
