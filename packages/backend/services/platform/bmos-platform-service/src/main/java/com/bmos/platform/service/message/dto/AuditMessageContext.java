package com.bmos.platform.service.message.dto;

import lombok.Data;

/**
 * 审批消息VO
 *
 * @className: AuditMessageVO
 * @author: yigaohui
 * @date: 2025/1/8 13:56
 * @Version: 1.0
 * @description:
 */

@Data
public class AuditMessageContext extends MessageContextDTO {

    private String auditUser;

    private String nodeName;

    private String auditContent;

    private String remark;

    private String businessText;

    private Boolean isStart;
}
