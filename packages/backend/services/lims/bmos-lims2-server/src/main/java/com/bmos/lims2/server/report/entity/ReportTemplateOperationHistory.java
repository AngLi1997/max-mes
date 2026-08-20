package com.bmos.lims2.server.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.ReportOperationTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 报告模板版本操作历史
 */
@Getter
@Setter
@TableName("lm_report_template_operation_history")
public class ReportTemplateOperationHistory extends BaseDO {

    private Long templateVersionId;
    private ReportOperationTypeEnum operationType;
    private String operatorId;
    private String operatorName;
    private LocalDateTime operateTime;

    private String path;
    private String remark;
}


