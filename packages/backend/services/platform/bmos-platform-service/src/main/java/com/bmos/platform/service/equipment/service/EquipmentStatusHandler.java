package com.bmos.platform.service.equipment.service;

import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.service.data.EquipmentStatusData;
import com.bmos.platform.service.equipment.service.data.EquipmentTagStatusData;

import java.util.List;

/**
 * 计算最终设备状态
 */
public interface EquipmentStatusHandler {

    /**
     * 根据设备标签状态计算设备是否可用还是不可用以及可用时的设备最小效期
     * @param equipmentTagStatusDataList
     * @param equipmentId: 设备id
     * @return
     */
    EquipmentStatusData analyzeEffectiveEquipment(List<EquipmentTagStatusData> equipmentTagStatusDataList, Long equipmentId);

    /**
     * 计算设备的真实状态以及设备最小效期
     * 将结果回填到equipmentInfo中
     * @param equipmentTagStatusDataList
     * @param equipmentInfo
     * @return
     */
    void analyzeEffectiveReleaseEquipment(List<EquipmentTagStatusData> equipmentTagStatusDataList, EquipmentInfo equipmentInfo);

}
