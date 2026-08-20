package com.bmos.mes.service.plan.info.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 当前生产批次下所有设备使用日志的分页信息
 */
@ApiModel("当前生产批次下所有设备使用日志的分页信息")
@Data
public class PlanRetraceEquipmentPageVO {

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备编码
     */
    private String equipmentCode;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 使用开始时间
     */
    private LocalDateTime useStartTime;

    /**
     * 使用结束时间
     */
    private LocalDateTime useEndTime;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 复核人
     */
    private String verifier;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;
}
