package com.bmos.platform.service.equipment.service.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备总状态
 */
@Getter
@Setter
public class EquipmentStatusData {

    /**
     * 当前设备的id
     */
    private Long equipmentId;

    /**
     * 设备是否可用
     */
    private Integer status;

    /**
     * 有效期
     */
    private LocalDateTime expireDateTime;

}
