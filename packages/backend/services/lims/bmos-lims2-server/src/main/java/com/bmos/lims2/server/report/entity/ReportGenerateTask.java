package com.bmos.lims2.server.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.ReportGenerateStatusEnum;
import com.bmos.lims2.common.enums.ReportLifecycleStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 报告生成任务（异步）
 */
@Getter
@Setter
@TableName("lm_report_generate_task")
public class ReportGenerateTask extends BaseDO {

    private Long materialId;
    private Long templateVersionId;
    private Long inspectionOrderId;
    private Long schemeVersionId;
    private String reportNo;
    private ReportGenerateStatusEnum status;
    private String message;

    /**
     * 报告生命周期状态：待审批、审批中、生效、作废
     */
    private ReportLifecycleStatusEnum lifecycleStatus;

    private String path;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * 报告审批流程实例ID（以任务为维度）
     */
    private String reportApprovalProcessInstanceId;

    /**
     * 报告审批是否通过
     */
    private Boolean reportApproved;

    /**
     * 报告审批通过时间
     */
    private LocalDateTime reportApprovalTime;

    /**
     * 确认人ID
     */
    private String confirmBy;

    /**
     * 确认时间（即报告生成时间）
     */
    private LocalDateTime confirmTime;

    /**
     * 检验结论
     */
    private String inspectionConclusion;

    /**
     * 确认备注
     */
    private String confirmRemark;

    /**
     * 选择的操作规程版本ID列表（多个用";"分隔），报告渲染时通过ID查询规程信息
     */
    private String operateVersionIds;

    /**
     * 报告DOCX快照路径（首次生成时保存，后续重渲染从此加载，避免数据变更导致不一致）
     */
    private String docxSnapshotPath;
}


