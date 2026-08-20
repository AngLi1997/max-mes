package com.bmos.lims2.server.task.dto;

import com.bmos.lims2.common.enums.TaskStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验单任务DTO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class InspectionOrderTaskDTO {

    /**
     * 检验单ID
     */
    private Long inspectionOrderId;

    /**
     * 检验单编号
     */
    private String orderNo;

    /**
     * 样品数量
     */
    private Integer sampleCount;

    /**
     * 任务总数
     */
    private Integer totalTaskCount;

    /**
     * 已完成任务数
     */
    private Integer completedTaskCount;

    /**
     * 待分配任务数
     */
    private Integer pendingAssignmentCount;

    /**
     * 待完成任务数
     */
    private Integer pendingCompletionCount;

    /**
     * 退回待审批任务数
     */
    private Integer returnPendingApprovalCount;

    /**
     * 已终止任务数
     */
    private Integer terminatedCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 任务列表
     */
    private List<TaskDTO> tasks;
}
