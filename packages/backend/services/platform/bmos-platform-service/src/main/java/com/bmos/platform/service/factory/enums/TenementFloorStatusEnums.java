package com.bmos.platform.service.factory.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @className: TenementFloorStatusEnums
 * @author: yigaohui
 * @date: 2024/12/30 14:51
 * @Version: 1.0
 * @description:
 */

@Getter
@AllArgsConstructor
public enum TenementFloorStatusEnums implements KeyValueEnum<String> {
    ENABLE("启用", "ENABLE"),

    DISABLE("停用", "DISABLE");

    private String name;
    @EnumValue
    private String value;

}
