package com.bmos.lims2.server.report.service.impl;

import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.FlowAuditService;
import com.bmos.lims2.server.audit.dto.FlowAuditTaskDTO;
import com.bmos.lims2.server.audit.dto.FlowStartDTO;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.audit.vo.TaskHistoryVO;
import com.bmos.lims2.server.report.dto.ReportApprovalPendingItemDTO;
import com.bmos.lims2.server.report.entity.ReportGenerateTask;
import com.bmos.lims2.server.report.mapper.ReportGenerateTaskMapper;
import com.bmos.lims2.server.report.mapper.ReportTemplateVersionMapper;
import com.bmos.lims2.server.report.dto.ReportApprovalPageQueryDTO;
import com.bmos.lims2.server.report.service.ReportApprovalService;
import com.bmos.lims2.server.report.service.ReportDocTemplateProcessor;
import com.bmos.lims2.server.report.service.ReportRenderContext;
import com.bmos.lims2.server.report.service.StorageService;
import com.bmos.lims2.server.config.minio.MinioProperties;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.mybatis.page.CommonPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description: 报告审批服务实现
 * @Author: yigaohui
 * @Date: 2025/09/02 10:30
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReportApprovalServiceImpl implements ReportApprovalService {

    private final ReportGenerateTaskMapper reportGenerateTaskMapper;
    private final FlowAuditService flowAuditService;
    private final ReportTemplateVersionMapper reportTemplateVersionMapper;
    private final ReportDocTemplateProcessor reportDocTemplateProcessor;
    private final StorageService storageService;
    private final MinioProperties minioProperties;
    @Autowired
    private AuditOperationLogService auditOperationLogService;

    @Override
    public CommonPage<ReportApprovalPendingItemDTO> pagePending(ReportApprovalPageQueryDTO queryDTO) {
        // 业务过滤出待审批任务（成功、已发起流程、未通过）
        java.util.List<ReportApprovalPendingItemDTO> candidates = reportGenerateTaskMapper.selectPendingApprovalTasks(queryDTO);
        if (candidates == null || candidates.isEmpty()) {
            return CommonPage.convertPage(Collections.emptyList());
        }
        Map<Long, ReportApprovalPendingItemDTO> taskMap = new HashMap<>();
        List<String> businessKeys = new ArrayList<>();
        for (ReportApprovalPendingItemDTO i : candidates) {
            taskMap.put(i.getTaskId(), i);
            businessKeys.add(String.valueOf(i.getTaskId()));
        }
        FlowAuditTaskDTO flowDto = new FlowAuditTaskDTO();
        flowDto.setCategory(AuditCategoryCodeEnum.REPORT_AUDIT.getCode());
        flowDto.setCurrent(queryDTO.getPageNum());
        flowDto.setSize(queryDTO.getPageSize());
        flowDto.setBusinessKeyList(businessKeys);
        PageQueryResp<List<TaskListResp>> todo = flowAuditService.queryToDoListByCategory(flowDto);
        if (todo.getTotal() == 0) {
            return CommonPage.convertPage(Collections.emptyList());
        }
        List<ReportApprovalPendingItemDTO> resultList = new ArrayList<>();
        for (TaskListResp task : todo.getData()) {
            Long taskId = Long.valueOf(task.getBusinessKey());
            ReportApprovalPendingItemDTO dto = taskMap.get(taskId);
            if (dto != null) {
                // 填充工作流字段，参照方案审批列表
                dto.setWorkflowTaskId(task.getTaskId());
                dto.setProcessInstanceId(task.getProcessInstanceId());
                dto.setCurrentNodeName(task.getElementName());
                dto.setInitiateTime(task.getProcessStartTime());
                dto.setInitiator(task.getProcessStartBy());
                dto.setDeploymentId(task.getDeploymentId());
                dto.setPayload(task.getPayload());
                dto.setExecutionId(task.getExecutionId());
                dto.setBusinessKey(task.getBusinessKey());
                resultList.add(dto);
            }
        }

        // 使用工作流分页总数构建返回
        CommonPage<ReportApprovalPendingItemDTO> page = new CommonPage<>();
        page.setPageNum(queryDTO.getPageNum());
        page.setPageSize(queryDTO.getPageSize());
        page.setTotal(todo.getTotal().intValue());
        page.setList(resultList);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String startReportApproval(Long generateTaskId) {
        ReportGenerateTask task = reportGenerateTaskMapper.selectById(generateTaskId);
        if (task == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }
        if (Boolean.TRUE.equals(task.getReportApproved())) {
            return task.getReportApprovalProcessInstanceId();
        }
        FlowStartDTO startDTO = new FlowStartDTO();
        startDTO.setBusinessKey(String.valueOf(task.getId()));
        startDTO.setCode(AuditCategoryCodeEnum.REPORT_AUDIT.getCode());
        startDTO.setCategoryCode(AuditCategoryCodeEnum.REPORT_AUDIT.getCode());
        startDTO.setName(task.getReportNo());
        startDTO.setExtField(String.valueOf(task.getInspectionOrderId()));
        String processInstanceId = flowAuditService.flowAuditStart(startDTO);

        ReportGenerateTask update = new ReportGenerateTask();
        update.setId(task.getId());
        update.setReportApprovalProcessInstanceId(processInstanceId);
        update.setLifecycleStatus(com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.APPROVING);
        reportGenerateTaskMapper.updateById(update);

        // 记录报告审批操作历史（参照方案版本历史实现）
        try {
            String userId = com.bmos.common.holder.SysUserHolder.getUser() != null ? 
                    com.bmos.common.holder.SysUserHolder.getUser().getUserId() : null;
            auditOperationLogService.save(AuditOperationLogEntity.builder()
                    .businessId(task.getId())
                    .module(AuditBusinessModule.REPORT_AUDIT.name())
                    .operationType(OperationType.SUBMIT_AUDIT.getValue())
                    .remark("发起报告审批")
                    .createBy(userId)
                    .build());
        } catch (Exception e) {
            log.warn("记录发起报告审批历史失败", e);
        }
        return processInstanceId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessSuccessCallBack(String processInstanceId, String comment,String remark, String userId) {
        ReportGenerateTask task = reportGenerateTaskMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ReportGenerateTask>()
                .eq(ReportGenerateTask::getReportApprovalProcessInstanceId, processInstanceId));
        if (task == null) {
            log.warn("未找到报告审批任务，流程实例ID:{}", processInstanceId);
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        // 审批通过：将同检验单、相同模板已生效的报告置为作废
        com.bmos.lims2.server.report.entity.ReportTemplateVersion version =
                reportTemplateVersionMapper.selectById(task.getTemplateVersionId());
        if (version != null) {
            Long templateId = version.getTemplateId();
            reportGenerateTaskMapper.voidEffectiveByOrderAndTemplate(task.getInspectionOrderId(), templateId, task.getId());
        }

        ReportGenerateTask update = new ReportGenerateTask();
        update.setId(task.getId());
        update.setReportApproved(true);
        update.setReportApprovalTime(now);
        update.setLifecycleStatus(com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.EFFECTIVE);
        reportGenerateTaskMapper.updateById(update);

        // 最终审批通过后重渲染报告（填充所有审批节点信息）
        reRenderReportWithHistory(task);

        // 记录报告审批通过操作历史（参照方案版本历史实现，包含审核意见）
        // 注意：节点名称在工作流监听器中已记录，这里记录流程结束的审批通过操作
        try {
            auditOperationLogService.save(AuditOperationLogEntity.builder()
                    .businessId(task.getId())
                    .module(AuditBusinessModule.REPORT_AUDIT.name())
                    .operationType(OperationType.APPROVE_AUDIT.getValue())
                    .remark(remark)
                    .comment(comment)
                    .createBy(userId)
                    .build());
        } catch (Exception e) {
            log.warn("记录报告审批通过历史失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessRejectCallBack(String processInstanceId, String comment, String remark, String userId, String nodeName) {
        ReportGenerateTask task = reportGenerateTaskMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ReportGenerateTask>()
                .eq(ReportGenerateTask::getReportApprovalProcessInstanceId, processInstanceId));
        if (task == null) {
            log.warn("未找到报告审批任务(拒绝)，流程实例ID:{}", processInstanceId);
            return;
        }
        // 业务：保持未通过（reportApproved=false），可按需记录备注到任务消息
        ReportGenerateTask update = new ReportGenerateTask();
        update.setId(task.getId());
        update.setReportApproved(false);
        update.setLifecycleStatus(com.bmos.lims2.common.enums.ReportLifecycleStatusEnum.PENDING_APPROVAL);
        reportGenerateTaskMapper.updateById(update);

        // 记录报告审批拒绝操作历史（参照方案版本历史实现，包含审核意见、节点名称）
        saveHistoryLog(comment, remark, userId, task.getId(), OperationType.REJECT_AUDIT, nodeName);
    }

    private void saveHistoryLog(String comment, String remark, String userId, Long id, OperationType operationType, String nodeName) {
        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.REPORT_AUDIT.name())
                .businessId(id)
                .operationType(operationType.getValue())
                .remark(remark)
                .nodeName(nodeName)
                .comment(comment)
                .createBy(userId)
                .build());
    }

    @Override
    public void auditNodeCompleteCallback(String processInstanceId, String businessKey, String nodeName, String userId) {
        try {
            Long taskId = Long.valueOf(businessKey);
            ReportGenerateTask task = reportGenerateTaskMapper.selectById(taskId);
            if (task == null) {
                log.warn("auditNodeCompleteCallback: 未找到报告生成任务，businessKey={}", businessKey);
                return;
            }
            reRenderReportWithHistory(task);
        } catch (Exception e) {
            log.warn("审批节点完成后重渲染报告失败，businessKey={}", businessKey, e);
        }
    }

    @Override
    public void reRenderReport(Long taskId) {
        ReportGenerateTask task = reportGenerateTaskMapper.selectById(taskId);
        if (task == null) {
            log.warn("reRenderReport: 未找到报告生成任务，taskId={}", taskId);
            return;
        }
        reRenderReportWithHistory(task);
    }

    /**
     * 根据任务的确认信息及当前审批历史，重渲染报告PDF并更新存储
     */
    private void reRenderReportWithHistory(ReportGenerateTask task) {
        if (task.getDocxSnapshotPath() == null && task.getPath() == null) {
            log.warn("reRenderReportWithHistory 跳过：快照路径和PDF路径均未就绪，taskId={}", task.getId());
            return;
        }
        try {
            // 构造渲染上下文：确认信息
            ReportRenderContext ctx = new ReportRenderContext();
            ctx.setConfirmBy(task.getConfirmBy());
            ctx.setConfirmTime(task.getConfirmTime());
            ctx.setInspectionConclusion(task.getInspectionConclusion());
            ctx.setReportNo(task.getReportNo());

            // 取确认人名称
            if (task.getConfirmBy() != null) {
                BaseUserDO user = UserUtils.getUser(task.getConfirmBy());
                ctx.setConfirmByName(user != null ? user.getUserName() : task.getConfirmBy());
            }

            // 查询当前审批流历史节点
            if (task.getReportApprovalProcessInstanceId() != null) {
                List<TaskHistoryVO> history = flowAuditService.listTaskHistory(task.getReportApprovalProcessInstanceId());
                if (history != null && !history.isEmpty()) {
                    List<ReportRenderContext.ApprovalNodeContext> nodes = new ArrayList<>();
                    for (TaskHistoryVO h : history) {
                        if (h.getEndTime() != null) {
                            String userName = h.getCompleteName();
                            if (userName == null) userName = h.getAssigneeName();
                            nodes.add(new ReportRenderContext.ApprovalNodeContext(h.getElementName(), userName, h.getEndTime()));
                        }
                    }
                    ctx.setApprovalNodes(nodes);
                }
            }

            // 重渲染
            byte[] bytes;
            String bucket = minioProperties.getBuckets().getReportVersion();
            if (task.getDocxSnapshotPath() != null) {
                // 从冻结快照重渲染，不重新查DB，避免数据变更导致不一致
                try (java.io.InputStream snapshotStream = storageService.getObject(bucket, task.getDocxSnapshotPath());
                     java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream()) {
                    byte[] buf = new byte[8192];
                    int n;
                    while ((n = snapshotStream.read(buf)) != -1) baos.write(buf, 0, n);
                    bytes = reportDocTemplateProcessor.renderPdfFromDocxSnapshot(baos.toByteArray(), ctx);
                }
            } else {
                // 兜底：老数据无快照，回退到重新查DB渲染
                bytes = reportDocTemplateProcessor.renderPdfWithContext(
                        task.getTemplateVersionId(), task.getInspectionOrderId(), ctx);
            }

            // 计算PDF目标路径：如果当前path仍指向快照(.docx)，则生成正式PDF路径
            String pdfPath = task.getPath();
            if (pdfPath == null || pdfPath.endsWith(".docx")) {
                String reportNo = task.getReportNo();
                String fileName = (reportNo != null && !reportNo.isEmpty() ? reportNo : "report-" + task.getId()) + ".pdf";
                pdfPath = "report-version/" + task.getId() + "/" + fileName;
            }
            storageService.upload(bucket, pdfPath,
                    new java.io.ByteArrayInputStream(bytes), bytes.length, "application/pdf");
            log.info("报告重渲染成功，taskId={}, path={}", task.getId(), pdfPath);
            // 如果路径发生变化（从快照路径升级为PDF路径），更新数据库
            if (!pdfPath.equals(task.getPath())) {
                ReportGenerateTask pathUpdate = new ReportGenerateTask();
                pathUpdate.setId(task.getId());
                pathUpdate.setPath(pdfPath);
                reportGenerateTaskMapper.updateById(pathUpdate);
            }
        } catch (Exception e) {
            log.error("报告重渲染失败，taskId={}", task.getId(), e);
        }
    }
}


