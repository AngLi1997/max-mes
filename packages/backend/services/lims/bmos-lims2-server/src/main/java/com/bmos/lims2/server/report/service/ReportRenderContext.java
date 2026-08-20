package com.bmos.lims2.server.report.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 报告渲染上下文：包含确认信息与审批节点信息，用于重渲染报告时填充动态占位符
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRenderContext {

    /** 确认人ID */
    private String confirmBy;

    /** 确认人名称（展示用） */
    private String confirmByName;

    /** 确认时间（即报告生成时间） */
    private LocalDateTime confirmTime;

    /** 检验结论 */
    private String inspectionConclusion;

    /** 报告编码 */
    private String reportNo;

    /** 检验依据（操作规程快照，格式：名称-编码-版本号，多个用";"分隔） */
    private String inspectBasis;

    /** 审批节点列表（按节点序号升序） */
    private List<ApprovalNodeContext> approvalNodes;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApprovalNodeContext {
        /** 节点名称 */
        private String nodeName;
        /** 操作人名称 */
        private String userName;
        /** 操作时间 */
        private LocalDateTime operationTime;
    }
}
