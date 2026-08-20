package com.bmos.platform.facade.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通信协议
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 10:02
 */
@Getter
@AllArgsConstructor
public enum EquipmentProto implements CommonEnum<String> {

    /**
     * ws
     */
    WEBSOCKET("WEBSOCKET");

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
