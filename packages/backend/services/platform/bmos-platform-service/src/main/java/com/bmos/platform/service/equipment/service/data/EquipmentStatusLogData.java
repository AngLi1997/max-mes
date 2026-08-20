package com.bmos.platform.service.equipment.service.data;

import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备变更日志数据
 */
@Getter
@Setter
public class EquipmentStatusLogData {

    /**
     * 设备id，关联到bp_equipment_info表中的id
     */
    private Long equipmentId;
    /**
     * 设备code
     */
    private String equipmentCode;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 设备地址
     */
    private String position;
    /**
     * 变更类型
     */
    private EquipmentStatusLogChangeType changeType;
    /**
     * 操作名称
     * {@link com.bmos.platform.facade.equipment.enums.EquipmentStatusOperateEnum}
     */
    private String operateName;
    /**
     * 变更前状态
     * {@link com.bmos.platform.facade.equipment.enums.EquipmentStatusLogEnum}
     */
    private String preStatusName;
    /**
     * 变更后状态
     * {@link com.bmos.platform.facade.equipment.enums.EquipmentStatusLogEnum}
     */
    private String statusName;
    /**
     * 效期
     */
    private LocalDateTime expireDateTime;
    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

}
