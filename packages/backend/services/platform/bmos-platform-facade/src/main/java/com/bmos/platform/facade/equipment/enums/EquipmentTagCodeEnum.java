package com.bmos.platform.facade.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备标签code
 */
@Getter
@AllArgsConstructor
public enum EquipmentTagCodeEnum implements CommonEnum<String> {

    /**
     * CIP系统单元
     */
    CIP_12001("CIP_12001", "CIP系统单元"),

    /**
     * SIP系统单元
     */
    SIP_12002("SIP_12002", "SIP系统单元"),
    /**
     * 温控系统
     */
    TEMPERATURE_CONTROL_12003("TEMPERATURE_CONTROL_12003", "温控系统"),
    /**
     * 空调系统
     */
    AIR_CONDITIONING_12004("AIR_CONDITIONING_12004", "空调系统"),
    /**
     * 清洗设备
     */
    CLEANING_DEVICE_12005("CLEANING_DEVICE_12005", "清洗设备"),
    /**
     * 灭菌设备
     */
    MICROBIOLOGICAL_DEVICE_12006("MICROBIOLOGICAL_DEVICE_12006", "灭菌设备"),
    /**
     * 检测设备
     */
    CHECK_DEVICE_12007("CHECK_DEVICE_12007", "检测设备"),
    /**
     * 破袋设备
     */
    BROKEN_BAG_DEVICE_12008("BROKEN_BAG_DEVICE_12008", "破袋设备"),
    /**
     * 血液制品生产设备
     */
    BLOOD_PRODUCTS_PRODUCTION_DEVICE_12009("BLOOD_PRODUCTS_PRODUCTION_DEVICE_12009", "血液制品生产设备"),
    /**
     * 离心机
     */
    CENTRIFUGE_12010("CENTRIFUGE_12010", "离心机"),
    /**
     * 灭活设备
     */
    MICROBIOLOGICAL_DEVICE_12011("MICROBIOLOGICAL_DEVICE_12011", "灭活设备"),
    /**
     * 层析系统
     */
    LAYER_EXTRACTION_SYSTEM_12012("LAYER_EXTRACTION_SYSTEM_12012", "层析系统"),
    /**
     * 超滤系统
     */
    OVERFILTER_SYSTEM_12013("OVERFILTER_SYSTEM_12013", "超滤系统"),
    /**
     * 灌装设备
     */
    INJECTION_DEVICE_12014("INJECTION_DEVICE_12014", "灌装设备"),
    /**
     * 冻干设备
     */
    FREEZE_DRY_DEVICE_12015("FREEZE_DRY_DEVICE_12015", "冻干设备"),
    /**
     * 轧盖设备
     */
    ROLL_COVER_DEVICE_12016("ROLL_COVER_DEVICE_12016", "轧盖设备"),
    /**
     * 干热灭活设备
     */
    DRY_MICROBIOLOGICAL_DEVICE_12017("DRY_MICROBIOLOGICAL_DEVICE_12017", "干热灭活设备"),
    /**
     * 全自动灯检设备
     */
    AUTO_LIGHT_CHECK_DEVICE_12018("AUTO_LIGHT_CHECK_DEVICE_12018", "全自动灯检设备"),
    /**
     * 制品包装设备
     */
    PRODUCT_PACKAGING_DEVICE_12019("PRODUCT_PACKAGING_DEVICE_12019", "制品包装设备"),
    /**
     * 称具
     */
    WEIGHING_DEVICE_12020("WEIGHING_DEVICE_12020", "称具"),
    /**
     * 容器
     */
    CONTAINER_12021("CONTAINER_12021", "容器"),
    /**
     * 打印机
     */
    PRINTER_12022("PRINTER_12022", "打印机"),
    /**
     * PAD
     */
    PAD_12023("PAD_12023", "PAD"),
    ;
    @EnumValue
    private String code;

    private String name;


    @Override
    public String getValue() {
        return code;
    }
}
