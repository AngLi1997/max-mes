package com.bmos.platform.common.enums.factory;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author renjinguang
 */

@Getter
@AllArgsConstructor
public enum FactoryModuleEnum implements CommonEnum<String> {

    FIRM(0, "企业"),
    FACTORY(1, "工厂"),
    LINE(2, "产线"),
    ROOM(3, "房间"),
    STATION(4, "工位");

    @EnumValue
    private final Integer type;

    private final String value;

    @Override
    public String getName() {
        return this.value;
    }

    @Override
    public String getValue() {
        return String.valueOf(this.type);
    }
}
