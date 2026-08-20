package com.bmos.platform.facade.equipment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 根据参数查询该参数下的所有设备
 * 若不传递，则返回空设备列表
 */
@Getter
@Setter
public class EquipmentQueryDTO {

    /**
     * 工位id
     */
    private List<Long> stationIdList;

    /**
     * 设备标签编码
     */
    private String tagCode;

}
