package com.bmos.platform.common.enums.equipment;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.Getter;

/**
 * 属性类型
 */
@Getter
public enum PropertyTypeEnum implements CommonEnum<Integer> {

    /**
     * 设备状态
     */
    EQUIPMENT_STATUS(1, "设备状态"),
    /**
     * 标签属性
     */
    TAG_PROPERTY(2, "类型信息属性"),
    TAG_DATA_PROPERTY(3, "类型数据属性"),
    ;
    @EnumValue
    private Integer code;

    private String name;

    PropertyTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getValue() {
        return code;
    }
}
