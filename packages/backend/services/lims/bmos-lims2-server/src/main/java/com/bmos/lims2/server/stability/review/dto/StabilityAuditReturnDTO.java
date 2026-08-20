package com.bmos.lims2.server.stability.review.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性结果审核-审核退回DTO
 */
@Getter
@Setter
public class StabilityAuditReturnDTO {

    /** 检验单ID */
    private Long inspectionOrderId;

    /** 工作流执行实例ID（用于退回） */
    private String executionId;

    /** 审核意见 */
    private String comment;

    /** 备注 */
    private String remark;
}
