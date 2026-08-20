package com.bmos.platform.service.equipment.service.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.CommonEnumVO;
import com.bmos.common.base.enums.KeyValueEnum;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yigaohui
 * @date 2024/4/20
 **/
@AllArgsConstructor
@Getter
public enum AcquisitionPointTypeEnum implements CommonEnum<String> {

    ATTR("ATTR", "属性"),

    SERVICE("SERVICE", "服务"),

    EVENT("EVENT", "事件");

    @EnumValue
    private final String value;
    private final String name;

    public static AcquisitionPointTypeEnum findByName(String name) {
        for (AcquisitionPointTypeEnum typeEnum : AcquisitionPointTypeEnum.values()) {
            if (typeEnum.getName().equals(name)) {
                return typeEnum;
            }
        }
        return null;
    }
}
