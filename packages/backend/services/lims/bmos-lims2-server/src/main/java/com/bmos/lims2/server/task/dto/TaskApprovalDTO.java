package com.bmos.lims2.server.task.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 任务退回审批DTO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class TaskApprovalDTO {

    /**
     * 任务ID列表
     */
    @NotEmpty(message = "任务ID列表不能为空")
    private List<Long> taskIds;

    /**
     * 审批结果 true-通过 false-不通过
     */
    @NotNull(message = "审批结果不能为空")
    private Boolean approved;

    /**
     * 审批理由
     */
    private String approvalReason;
}
