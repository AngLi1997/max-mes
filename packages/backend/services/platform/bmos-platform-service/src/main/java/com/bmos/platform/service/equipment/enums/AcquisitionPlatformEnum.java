package com.bmos.platform.service.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnumVO;
import com.bmos.common.base.enums.KeyValueEnum;
import com.bmos.platform.common.enums.signature.SignatureActionEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @className: AcquisitionPlatformEnum
 * @author: yigaohui
 * @date: 2024/11/29 13:52
 * @Version: 1.0
 * @description:
 */

@Getter
@AllArgsConstructor
public enum AcquisitionPlatformEnum implements KeyValueEnum<String> {
    hub("hub", "指令集hub"),

    supCon("supCon", "中控");

    @EnumValue
    private final String value;
    private final String name;

    public static AcquisitionPlatformEnum findByName(String name) {
        for (AcquisitionPlatformEnum typeEnum : AcquisitionPlatformEnum.values()) {
            if (typeEnum.getName().equals(name)) {
                return typeEnum;
            }
        }
        return null;
    }
}
