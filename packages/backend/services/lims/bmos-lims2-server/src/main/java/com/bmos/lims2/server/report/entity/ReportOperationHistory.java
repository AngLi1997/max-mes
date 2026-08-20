package com.bmos.lims2.server.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.ReportOperationTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 报告操作历史（针对生成的报告）
 */
@Getter
@Setter
@TableName("lm_report_operation_history")
public class ReportOperationHistory extends BaseDO {

    private Long taskId;
    private ReportOperationTypeEnum operationType;
    private String operatorId;
    private String operatorName;
    private LocalDateTime operateTime;

    private String path;
    private String remark;
}


