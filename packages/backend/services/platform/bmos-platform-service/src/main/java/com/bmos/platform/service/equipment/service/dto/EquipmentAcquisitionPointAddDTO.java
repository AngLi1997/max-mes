package com.bmos.platform.service.equipment.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yigaohui
 * @date 2024/4/22
 **/
@Data
@Accessors(chain = true)
public class EquipmentAcquisitionPointAddDTO {
    /**
     * 设备id
     */
    private Long equipmentId;
    /**
     * 设备编码
     */
    private String equipmentCode;
    /**
     * 采集点id
     */
    private Long acquisitionPointId;
    /**
     * 采集点编码
     */
    private String acquisitionPointCode;
}
