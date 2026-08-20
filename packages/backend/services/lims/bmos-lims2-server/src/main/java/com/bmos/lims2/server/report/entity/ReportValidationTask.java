package com.bmos.lims2.server.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.ReportGenerateStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 报告验证任务（异步）
 */
@Getter
@Setter
@TableName("lm_report_validation_task")
public class ReportValidationTask extends BaseDO {

    private Long templateVersionId;
    private Long materialId;
    private Long schemeVersionId;
    private Long inspectionOrderId;
    private ReportGenerateStatusEnum status;
    private String message;

    private String path;

    /** 选中的操作规程版本ID列表，分号分隔，用于渲染检验依据 */
    private String operateVersionIds;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}


