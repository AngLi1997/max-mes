package com.bmos.mes.service.plan.document.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@ApiModel("生产计划审核分页VO")
public class ArchiveAuditPageVO {

    @ApiModelProperty("批记录ID")
    private Long archiveId;

    @ApiModelProperty("批记录编号")
    private String archiveNo;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("生产工艺名称")
    private String processName;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("批记录模板名称")
    private String templateName;

    @ApiModelProperty("批记录模板版本")
    private String templateVersion;

    @ApiModelProperty("归档生成时间")
    private LocalDateTime archiveTime;

    @ApiModelProperty("流程发起人对象")
    private String flowAuditStartByName;

    @ApiModelProperty("归档文件路径")
    private String path;

    @ApiModelProperty("流程发起时间")
    private LocalDateTime sendTime;

    @ApiModelProperty("流程实例")
    private String instanceId;

    @ApiModelProperty("流程任务")
    private String taskId;

    private String deploymentId;

    private String executionId;

    private String nodeId;

    private Map<String,Object> payload;

}
