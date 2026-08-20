package com.bmos.mes.service.plan.document.service;

import lombok.*;

/**
 * 流程节点结束 回调DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArchiveAuditCallBackDTO {

    /**
     * 归档id
     */
    private Long archiveId;

    /**
     * 审核结果
     */
    private Boolean auditResult;

    /**
     * 审核意见
     */
    private String auditOpinion;

    /**
     * 节点名称
     */
    private String elementName;

}
