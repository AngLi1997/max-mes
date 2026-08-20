package com.bmos.platform.facade.equipment.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 工位信息
 */
@Getter
@Setter
public class EquipmentFeignStationVO {

    /**
     * 工位id
     */
    private Long id;

    /**
     * 工位编码
     */
    private String code;

    /**
     * 工位名称
     */
    private String stationName;
}
