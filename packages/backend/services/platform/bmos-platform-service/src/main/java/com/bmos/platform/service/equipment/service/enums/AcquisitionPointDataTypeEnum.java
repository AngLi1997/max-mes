package com.bmos.platform.service.equipment.service.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.CommonEnumVO;
import com.bmos.common.base.enums.KeyValueEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yigaohui
 * @date 2024/4/20
 **/
@AllArgsConstructor
@Getter
public enum AcquisitionPointDataTypeEnum implements CommonEnum<String> {

    NUMBER("NUMBER", "数值类型"),

    STRING("STRING", "字符串类型"),

    DATETIME("DATETIME", "时间类型");

    @EnumValue
    private final String value;
    private final String name;

    public static AcquisitionPointDataTypeEnum findByName(String name) {
        for (AcquisitionPointDataTypeEnum typeEnum : AcquisitionPointDataTypeEnum.values()) {
            if (typeEnum.getName().equals(name)) {
                return typeEnum;
            }
        }
        return null;
    }
}
