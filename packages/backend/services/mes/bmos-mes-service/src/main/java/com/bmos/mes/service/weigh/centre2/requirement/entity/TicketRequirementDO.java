package com.bmos.mes.service.weigh.centre2.requirement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.common.enums.weigh.centre.RequirementWeighStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工单需求物料实体类
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:16
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_weigh_ticket_requirement")
@Data
public class TicketRequirementDO extends BaseDO {

    /**
     * 称量需求组id
     */
    private Long requirementGroupId;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

    /**
     * 配方物料称量需求key
     */
    private String requirementKey;

    /**
     * 物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 配方量
     */
    private BigDecimal formulaQuantity;

    /**
     * 理论量
     */
    private BigDecimal theoreticalQuantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 工单id
     */
    private Long ticketId;

    /**
     * 规划时间
     */
    private LocalDateTime programTime;

    /**
     * 称量中心id
     */
    private Long weighCentreId;

    /**
     * 计划生产日期
     */
    private LocalDate planDate;

    /**
     * 需求状态
     */
    private RequirementStatusEnum requirementStatus;

    /**
     * 称量状态
     */
    private RequirementWeighStatusEnum weighStatus;

    /**
     * 需求用途
     */
    private String requirementUsage;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 完成人
     */
    private String complete_user;
}
