package com.bmos.lims2.server.stability.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 稳定性结果审核-审核不通过DTO
 */
@Getter
@Setter
public class StabilityAuditRejectDTO {

    /** 检验单ID */
    private Long inspectionOrderId;

    /** 工作流流程实例ID */
    private String processInstanceId;

    /** 工作流任务ID */
    private String taskId;

    /** 需要退回的录入任务ID集合（lm_task.id） */
    private List<Long> taskIds;

    /** 审核意见 */
    private String comment;

    /** 备注 */
    private String remark;
}
