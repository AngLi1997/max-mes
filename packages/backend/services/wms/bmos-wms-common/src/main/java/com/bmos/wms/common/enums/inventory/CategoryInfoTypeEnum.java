package com.bmos.wms.common.enums.inventory;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CategoryInfoTypeEnum implements KeyValueEnum<Integer> {

    /**
     * wms 货品信息
     */
    CARGO(0, "货品信息");

    @EnumValue
    private final Integer value;
    private final String name;

    public static String getNameByValue(Integer code) {
        CategoryInfoTypeEnum[] values = CategoryInfoTypeEnum.values();
        for (CategoryInfoTypeEnum type : values) {
            if (ObjectUtil.equal(code, type.value)) {
                return type.name;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static CategoryInfoTypeEnum getEnumByValue(Integer value) {
        return Arrays.stream(CategoryInfoTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
