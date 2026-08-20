package com.bmos.platform.service.equipment.service.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yigaohui
 * @date 2024/4/20
 **/
@Getter
@AllArgsConstructor
public enum AcquisitionPointStatusEnum implements CommonEnum<String> {

    ENABLE("ENABLE", "启用"),

    DISABLE("DISABLE", "停用");

    @EnumValue
    private final String value;
    private final String name;
}
