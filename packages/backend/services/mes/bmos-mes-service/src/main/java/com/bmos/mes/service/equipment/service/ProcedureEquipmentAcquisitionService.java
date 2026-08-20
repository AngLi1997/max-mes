package com.bmos.mes.service.equipment.service;

import com.bmos.mes.service.equipment.service.dto.EquipmentAcquisitionComponentDTO;

/**
 * 设备点位数据服务
 *
 * @author yigaohui
 * @date 2024/4/23
 **/
public interface ProcedureEquipmentAcquisitionService {
    /**
     * 设备数采点位组件信息保存
     *
     * @param equipmentAcquisitionComponentDTO 数采组件信息
     */
    void saveEquipmentAcquisitionComponent(EquipmentAcquisitionComponentDTO equipmentAcquisitionComponentDTO);

    /**
     * 设备数采点位信息修改
     *
     * @param equipmentAcquisitionComponentDTO 数采组件信息
     */
    void modifyEquipmentAcquisitionComponent(EquipmentAcquisitionComponentDTO equipmentAcquisitionComponentDTO);
}
