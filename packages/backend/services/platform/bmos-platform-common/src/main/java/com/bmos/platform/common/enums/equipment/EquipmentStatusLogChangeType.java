package com.bmos.platform.common.enums.equipment;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.Getter;

@Getter
public enum EquipmentStatusLogChangeType implements CommonEnum<String> {
    /**
     * 手动变更
     */
    MANUAL("MANUAL", "手动变更"),
    /**
     * 业务
     */
    BUSINESS("BUSINESS", "业务流转"),

    /**
     * 效期到期
     */
    EXPIRE("EXPIRE", "效期到期")
    ;
    @EnumValue
    private String value;

    private String name;

    private EquipmentStatusLogChangeType(String value, String name) {
        this.value = value;
        this.name = name;
    }

}
