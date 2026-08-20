package com.bmos.mes.service.weigh.centre2.execute.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class WeighRequirementRecordDTO {
    /**
     * 称量需求ID
     */
    private Long weighTicketRequirementId;

    /**
     * 工单id 当为余料称量的时候，传递工单id
     */
    private Long ticketId;
    /**
     * 净重（kg）
     */
    private BigDecimal netWeight;
    /**
     * 皮重（kg）
     */
    private BigDecimal tareWeight;
    /**
     * 毛重（kg）
     */
    private BigDecimal grossWeight;
    /**
     * 称量功能类型（如普通、补料等）
     */
    private Integer weighFunc;
    /**
     * 称量类型（如余料、正常等）
     */
    private Integer weighType;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 称量的设备id
     */
    private Long equipmentId;
    /**
     * 容器ID
     */
    private Long deviceId;
    /**
     * 容器名称
     */
    private String deviceName;
    /**
     * 容器编码
     */
    private String deviceCode;
    /**
     * 仓库/库位ID
     */
    private Long storageId;

    /**
     * 超出目标量需要签名
     */
    private String finishSignUser;

} 