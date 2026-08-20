package com.bmos.lims2.server.report.service;


import com.bmos.lims2.server.report.dto.ReportApprovalPendingItemDTO;
import com.bmos.lims2.server.report.dto.ReportApprovalPageQueryDTO;
import com.bmos.mybatis.page.CommonPage;

/**
 * @Description: 报告审批服务
 * @Author: yigaohui
 * @Date: 2025/09/02 10:30
 */
public interface ReportApprovalService {

    /**
     * 分页查询当前用户待审批的报告
     */
    CommonPage<ReportApprovalPendingItemDTO> pagePending(ReportApprovalPageQueryDTO queryDTO);

    /**
     * 报告审批流程启动（以报告生成任务为维度）
     */
    String startReportApproval(Long generateTaskId);

    /**
     * 审批通过回调
     */
    void auditProcessSuccessCallBack(String processInstanceId, String comment,String remark, String userId);

    /**
     * 审批拒绝回调
     */
    void auditProcessRejectCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName);

    /**
     * 单个审批节点完成回调（EXECUTION_NODE_COMPLETE 事件）：重渲染报告，填充已完成的审批节点数据
     * @param processInstanceId 流程实例ID
     * @param businessKey 业务键（报告生成任务ID字符串）
     * @param nodeName 当前节点名称
     * @param userId 操作人ID
     */
    void auditNodeCompleteCallback(String processInstanceId, String businessKey, String nodeName, String userId);

    /**
     * 重渲染报告（填充确认人、检验结论及已完成的审批节点信息）
     * @param taskId 报告生成任务ID
     */
    void reRenderReport(Long taskId);
}


