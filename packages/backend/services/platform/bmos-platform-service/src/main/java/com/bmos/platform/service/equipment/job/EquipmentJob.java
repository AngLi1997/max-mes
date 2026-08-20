package com.bmos.platform.service.equipment.job;


/**
 * 设备相关定时器
 */
public interface EquipmentJob {

    void equipmentHeart();

    /**
     * 设备属性状态更新定时器
     */
    void equipmentPropertiesStatus();

}
