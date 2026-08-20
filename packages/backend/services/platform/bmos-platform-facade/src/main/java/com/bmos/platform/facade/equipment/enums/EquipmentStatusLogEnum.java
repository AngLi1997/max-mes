package com.bmos.platform.facade.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.Getter;

/**
 * bp_equipment_status_log中pre_status_name以及status_name的值枚举
 */
@Getter
public enum EquipmentStatusLogEnum implements CommonEnum<String> {


    /**
     * 设备属性状态
     */
    ALREADY_CLEAN("ALREADY_CLEAN", "已清洁"),
    ALREADY_DISINFECT("ALREADY_DISINFECT", "已消毒"),
    ALREADY_CALIBRATION("ALREADY_CALIBRATION", "已校准"),
    // 未清洁
    NOT_CLEAN("NOT_CLEAN", "未清洁"),
    // 未消毒
    NOT_DISINFECT("NOT_DISINFECT", "未消毒"),
    // 未校准
    NOT_CALIBRATION("NOT_CALIBRATION", "未校准"),

    /**
     * 可用
     */
    AVAILABLE("AVAILABLE", "可用"),
    /**
     * 不可用
     */
    UNAVAILABLE("UNAVAILABLE", "不可用"),
    /**
     * 占用
     */
    OCCUPY("OCCUPY", "占用"),
    /**
     * 故障
     */
    FAULT("FAULT", "故障"),
    ;


    EquipmentStatusLogEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @EnumValue
    private String code;

    private String name;

    @Override
    public String getValue() {
        return code;
    }
}
