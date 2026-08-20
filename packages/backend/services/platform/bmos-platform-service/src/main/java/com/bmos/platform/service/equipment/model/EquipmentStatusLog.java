package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

/**
 * 设备状态变更日志表，记录设备状态的变更信息(BpEquipmentStatusLog)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:39:56
 */
@Getter
@Setter
@TableName("bp_equipment_status_log")
public class EquipmentStatusLog extends BaseDO implements Serializable {
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
     * 变更前状态名称
     * {@link com.bmos.platform.facade.equipment.enums.EquipmentStatusLogEnum}
     */
    private String preStatusName;
    /**
     * 变更后状态名称
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
    /**
     * 操作人id
     */
    private String operator;

    /**
     * 操作人姓名
     */
    private String operatorName;

}

