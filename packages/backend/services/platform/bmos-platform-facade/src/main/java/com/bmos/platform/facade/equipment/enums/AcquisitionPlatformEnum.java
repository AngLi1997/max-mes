package com.bmos.platform.facade.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
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
public enum AcquisitionPlatformEnum implements CommonEnum<String> {
    hub("hub", "指令集hub"),

    supCon("supCon", "中控");

    @EnumValue
    private final String value;
    private final String name;

}
