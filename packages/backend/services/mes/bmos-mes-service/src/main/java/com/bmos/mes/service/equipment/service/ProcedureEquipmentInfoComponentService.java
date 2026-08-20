package com.bmos.mes.service.equipment.service;

import com.bmos.mes.service.equipment.service.dto.EquipmentAcquisitionComponentDTO;
import com.bmos.mes.service.equipment.service.dto.EquipmentInfoComponentDTO;

import java.util.List;

/**
 * 设备信息数据服务
 *
 * @author yigaohui
 * @date 2024/4/23
 **/
public interface ProcedureEquipmentInfoComponentService {

    /**
     * 设备信息组件保存
     *
     * @param equipmentInfoComponentDTO 设备信息组件数据
     */
    void saveEquipmentInfoComponent(EquipmentInfoComponentDTO equipmentInfoComponentDTO);

    /**
     * 设备信息组件数据修改
     *
     * @param equipmentInfoComponentDTO 组件信息
     */
    void modifyEquipmentInfoComponent(EquipmentInfoComponentDTO equipmentInfoComponentDTO);
}
