package com.bmos.mes.service.audit.service;

import com.bmos.audit.engine.core.query.cmd.PageHistoryQueryCmd;
import com.bmos.audit.engine.core.query.cmd.TaskHistoryCmd;
import com.bmos.audit.engine.core.query.resp.PageHistoryInstanceResp;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskHistoryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.mes.service.audit.dto.*;
import com.bmos.mes.service.audit.model.FlowAuditProcess;
import com.bmos.mes.service.audit.vo.*;
import com.bmos.mybatis.page.CommonPage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FlowAuditService {

    CommonPage<FlowAuditVO> flowAuditPage(AuditPageDTO dto);

    Boolean checkoutDeployment(FlowCheckoutDTO dto);

    void saveFlowAudit(SaveAuditDTO dto);

    FlowAuditDetailVO detailFlowAudit(Long versionId);

    /**
     * 发起流程
     * @param dto 发起流程参数
     */
    String flowAuditStart(FlowStartDTO dto);

    /**
     * 处理任务
     * @param dto 任务处理参数
     */
    Boolean flowAuditComplete(CompleteDTO dto);

    /**
     * 审核不通过
     * @param dto 任务处理参数
     */
    Boolean flowAuditCompleteNotApprove(CompleteDTO dto);

    /**
     * 查询代办
     * @param dto 查询代办参数
     */
    PageQueryResp<List<TaskListResp>> queryToDoListByCategory(FlowAuditTaskDTO dto);

    /**
     * 查询已办任务
     * @param dto
     */
    PageQueryResp<List<TaskListResp>> queryDoneListByCategory(FlowAuditTaskDTO dto);

    /**
     * 查询单个任务
     * @param
     */
    AuditTaskVO findByTaskId(String taskId);

    /**
     * 查询查询登录人的多个任务
     */
    List<AuditTaskVO> findBatchTaskByProcessInstanceId(String processInstanceId);

    /**
     * 查询当前人的历史任务
     */
    PageQueryResp<List<PageHistoryInstanceResp>> findHistoryByCategoryCodeAndAssignee(PageHistoryQueryCmd cmd);

    /**
     * 查询当前人员历史节点任务
     */
    List<TaskHistoryResp> findHistoryByProcessInstanceIdAndAssignee(TaskHistoryCmd cmd);

    FlowAuditHistoryVO listFlowAuditHistory(FlowAuditHistoryDTO dto);

    CommonPage<AuditHistoryVO> listAuditHistory(AuditHistoryDTO dto);

    List<TaskHistoryVO> listTaskHistory(String processInstanceId);

    void exportAuditHistory(AuditHistoryExportDTO dto, HttpServletResponse response);

    void exportTaskHistory(ExportTaskHistoryDTO dto, HttpServletResponse response);

    Boolean flowAuditBackToPrev(AuditBackToPrevDTO dto);

    List<AuditCategoryCountVO> getAuditCategoryToDoCount(String userId);

    void saveAuditBackHistory(String businessId,String comment,String remark,String nodeName,String modelName);


    void bindFlowAuditProcess(FlowProcessBindDTO dto);

    /**
     * 查询某个流程categoryCode下processId绑定的流程code
     *
     * @param categoryCode
     * @param processId
     * @return
     */
    FlowAuditProcess selectBindProcessFlowAudit(String categoryCode, Long processId);

    /**
     * 查询流程code绑定的工艺id
     * @param code
     * @return
     */
    List<Long> flowAuditProcessList(String code);

    /**
     * 启停流程版本
     * @param dto
     */
    void changeFlowAuditState(ChangeAuditVersionStateDTO dto);
}
