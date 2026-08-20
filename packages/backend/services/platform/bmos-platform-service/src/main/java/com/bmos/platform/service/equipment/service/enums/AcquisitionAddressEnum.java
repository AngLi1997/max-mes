package com.bmos.platform.service.equipment.service.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据采集地址枚举
 * @author ren jin guang
 */
@Getter
@AllArgsConstructor
public enum AcquisitionAddressEnum implements CommonEnum<String> {
    HUB("hub", "hub数采");

    @EnumValue
    private final String value;
    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
