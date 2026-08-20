package com.bmos.lims2.server.stability.review.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性结果审核-审核通过DTO
 */
@Getter
@Setter
public class StabilityAuditApproveDTO {

    /** 检验单ID */
    private Long inspectionOrderId;

    /** 工作流流程实例ID */
    private String processInstanceId;

    /** 工作流任务ID */
    private String taskId;

    /** 审核意见 */
    private String comment;

    /** 备注 */
    private String remark;

    /** 登录名（用于密码验证） */
    private String loginName;

    /** 密码（电子签名确认） */
    private String password;
}
