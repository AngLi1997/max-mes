package com.bmos.platform.service.equipment.service.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EquipmentOperateLogData {

    /**
     * 设备id，关联到bp_equipment_info表中的id
     */
    private Long equipmentId;
    /**
     * 设备编码
     */
    private String code;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 设备地点
     */
    private String position;
    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 变更类型
     */
    private String changeType;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 占用设备的工位id
     */
    private Long applyStationId;
    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 操作日志id
     */
    private Long operateLogId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 符合人
     */
    private String reviewer;


    private String operateContent;

    /**
     * 使用结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否是设备使用日志填报处手动填报
     */
    private boolean fillLog;

    /**
     * 操作内容模板id
     */
    private Long templateId;
}
