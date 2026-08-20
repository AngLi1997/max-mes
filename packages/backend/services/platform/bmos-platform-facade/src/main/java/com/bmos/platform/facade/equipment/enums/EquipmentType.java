package com.bmos.platform.facade.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 09:45
 */
@Getter
@AllArgsConstructor
public enum EquipmentType implements CommonEnum<String> {

    /**
     * 电子天平
     */
    BALANCE("BALANCE");

    @EnumValue
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return name;
    }
}
