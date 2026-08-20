package com.bmos.mes.service.weigh.centre2.ticket.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.weigh.centre.TaskProgramTypeEnum;
import com.bmos.mes.common.enums.weigh.centre2.TicketStatusEnum;
import com.bmos.mes.common.enums.weigh.centre2.TicketWeighStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工单实体类
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:16
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_weigh_ticket")
@Data
public class TicketDO extends BaseDO {

    /**
     * 工单编号
     */
    private String ticketNo;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 合并编码
     */
    private String materialMergeCode;

    /**
     * 物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 物料规格
     */
    private String materialSpecification;

    /**
     * 称量中心id
     */
    private Long weighCentreId;

    /**
     * 需求量
     */
    private BigDecimal requirementQuantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 计划日期
     */
    private LocalDate planDate;

    /**
     * 工单状态
     */
    private TicketStatusEnum status;

    /**
     * 称量状态
     * 1-未称量 2-称量中 3-已称量
     */
    private TicketWeighStatusEnum ticketWeighStatus;

    /**
     * 下发时间
     */
    private LocalDateTime sendTime;

    /**
     * 规划类型
     */
    private TaskProgramTypeEnum taskProgramType;

    /**
     * 称量完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 是否满足完成称量
     */
    private Boolean enoughCompleteCondition;

}
