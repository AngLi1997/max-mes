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
public enum ProductPlanStatusEnum implements CommonEnum<String> {

    EDIT("编辑", "EDIT"),
    AUDIT("审批中", "AUDIT"),
    CONFIRM("确认", "CONFIRM"),
    DISCARD("作废", "DISCARD"),
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
    public static ProductPlanStatusEnum getEnumByName(CommonEnumVO<String> commonEnumVO) {
        return Arrays.stream(ProductPlanStatusEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(commonEnumVO.getValue()))
                .findFirst()
                .orElse(null);
    }
}
