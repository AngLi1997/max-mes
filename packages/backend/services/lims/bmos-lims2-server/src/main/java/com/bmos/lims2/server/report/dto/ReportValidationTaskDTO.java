package com.bmos.lims2.server.report.dto;

import com.bmos.lims2.common.enums.ReportGenerateStatusEnum;
import com.bmos.lims2.common.enums.ReportLifecycleStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("报告验证任务DTO")
public class ReportValidationTaskDTO {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("模板版本ID")
    private Long templateVersionId;
    @ApiModelProperty("检品ID")
    private Long materialId;
    @ApiModelProperty("方案版本ID")
    private Long schemeVersionId;
    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;
    @ApiModelProperty("状态")
    private ReportGenerateStatusEnum status;
    @ApiModelProperty("消息")
    private String message;
    @ApiModelProperty("输出桶")
    private String path;
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
    @ApiModelProperty("生命周期状态")
    private ReportLifecycleStatusEnum lifecycleStatus;
    @ApiModelProperty("DOCX快照路径（待确认阶段预览用）")
    private String docxSnapshotPath;
}


