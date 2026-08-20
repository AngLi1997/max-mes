package com.bmos.mes.service.weigh.centre2.execute.controller.vo;

import com.bmos.mes.common.enums.weigh.centre2.TicketWeighStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class WeighTicketPageVO {
    /**
     * 工单ID
     */
    private Long id;
    /**
     * 物料合并编码
     */
    private String materialMergeCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 称量中心编码
     */
    private String centreCode;
    /**
     * 称量中心名称
     */
    private String centreName;
    /**
     * 工单号
     */
    private String ticketNo;
    /**
     * 计划生产日期
     */
    private LocalDate planDate;
    /**
     * 下发日期
     */
    private LocalDateTime sendTime;
    /**
     * 完成时间
     */
    private LocalDateTime completeTime;
    /**
     * 物料规格
     */
    private String materialSpecification;
    /**
     * 操作人姓名
     */
    private String operator;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 称量中心ID
     */
    private Long weighCentreId;
    /**
     * 工单称量状态
     */
    private TicketWeighStatusEnum ticketWeighStatus;
} 