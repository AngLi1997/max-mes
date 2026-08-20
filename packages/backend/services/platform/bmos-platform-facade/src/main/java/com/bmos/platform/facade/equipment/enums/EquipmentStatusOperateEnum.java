package com.bmos.platform.facade.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备状态
 */
@Getter
@AllArgsConstructor
public enum EquipmentStatusOperateEnum implements CommonEnum<String> {
    /**
     * 可用
     */
    OPERATE("OPERATE", "使用"),
    /**
     * 消毒
     */
    DISINFECTION("DISINFECTION", "消毒"),
    /**
     * 清洁
     */
    CLEAN("CLEAN", "清洁"),
    /**
     * 校准
     */
    CALIBRATION("CALIBRATION", "校准"),
    ;

    @EnumValue
    private String code;

    private String desc;


    @Override
    public String getName() {
        return desc;
    }

    @Override
    public String getValue() {
        return code;
    }
}
