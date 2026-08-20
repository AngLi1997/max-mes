package com.bmos.platform.service.equipment.service.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 设备下的标签属性信息
 */
@Getter
@Setter
public class EquipmentTagData {

    /**
     * 设备id
     */
    private Long equipmentId;

    /**
     * 设别下的标签信息
     */
    private List<TagData> equipmentTagDataList;

    /**
     * 设备的各个设备状态
     */
    private List<EquipmentTagStatusData> statusPropertyList;

    /**
     * 设备的标签属性
     */
    private List<EquipmentPropertyData> infoPropertyList;

    /**
     * 设备的标签属性
     */
    private List<EquipmentPropertyData> dataPropertyList;
}
