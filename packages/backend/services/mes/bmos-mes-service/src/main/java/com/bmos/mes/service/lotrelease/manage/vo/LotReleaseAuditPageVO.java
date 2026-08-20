package com.bmos.mes.service.lotrelease.manage.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 批签发审核分页数据vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/28 14:27
 */
@ApiModel("批签发审核分页数据vo")
@Data
public class LotReleaseAuditPageVO {

    private Long id;

    private String no;

    private String productName;

    private String productMergeCode;

    private String specification;

    private String processName;

    private String batchNo;

    private String name;

    private String templateVersion;

    private String submitterName;

    private String submitterId;

    private LocalDateTime submitterTime;

    private String fileUrl;

    private String taskId;

    private String processInstanceId;

    private String deploymentId;

    private String executionId;

    private String nodeId;

    private Map<String,Object> payload;

    private LocalDateTime processStartTime;
}
