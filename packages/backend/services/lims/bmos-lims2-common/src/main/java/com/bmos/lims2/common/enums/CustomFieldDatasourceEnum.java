package com.bmos.lims2.common.enums;

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
public enum CustomFieldDatasourceEnum implements CommonEnum<String> {

    MaterialCustomFields("MaterialCustomFields", "物料自定义字段"),
    MaterialPieceCustomFields("MaterialPieceCustomFields", "物料件自定义字段"),
    MaterialBatchCustomFields("MaterialBatchCustomFields", "物料批次自定义字段"),
    ;

    @EnumValue
    private final String value;

    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CustomFieldDatasourceEnum getEnumByValue(String value) {
        return Arrays.stream(CustomFieldDatasourceEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
