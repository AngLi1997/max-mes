package com.bmos.lims2.server.stability.review.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.audit.engine.core.query.resp.PageQueryResp;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.enums.TaskOperationTypeEnum;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.FlowAuditService;
import com.bmos.lims2.server.audit.dto.AuditBackToPrevDTO;
import com.bmos.lims2.server.audit.dto.CompleteDTO;
import com.bmos.lims2.server.audit.dto.FlowAuditTaskDTO;
import com.bmos.lims2.server.audit.dto.FlowStartDTO;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.server.eln.signature.dto.SignatureValidateDTO;
import com.bmos.lims2.server.eln.signature.service.SignatureService;
import com.bmos.lims2.server.inspect.audit.service.SampleAuditService;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.order.util.OrderTransitionGuard;
import com.bmos.lims2.server.stability.plan.service.StabilityInspectPlanService;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditApproveDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditDetailDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditHeaderDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditRejectDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityAuditReturnDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityResultReviewDTO;
import com.bmos.lims2.server.stability.review.dto.StabilityResultReviewQueryDTO;
import com.bmos.lims2.server.stability.review.mapper.StabilityResultReviewMapper;
import com.bmos.lims2.server.stability.review.service.StabilityResultReviewService;
import com.bmos.lims2.server.task.entity.Task;
import com.bmos.lims2.server.task.entity.TaskStatusHistory;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.lims2.server.task.mapper.TaskStatusHistoryMapper;
import com.bmos.lims2.server.task.util.TaskTransitionGuard;
import com.bmos.mybatis.page.CommonPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 稳定性结果审核服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StabilityResultReviewServiceImpl implements StabilityResultReviewService {

    private final StabilityResultReviewMapper stabilityResultReviewMapper;
    private final SampleAuditService sampleAuditService;
    private final InspectionOrderMapper inspectionOrderMapper;
    private final FlowAuditService flowAuditService;
    private final TaskMapper taskMapper;
    private final TaskStatusHistoryMapper taskStatusHistoryMapper;
    private final StabilityInspectPlanService stabilityInspectPlanService;
    private final AuditOperationLogService auditOperationLogService;

    @Autowired
    private SignatureService signatureService;

    @Override
    public CommonPage<StabilityResultReviewDTO> pageReviewList(StabilityResultReviewQueryDTO queryDTO) {
        // 1. 按业务条件查询候选检验单（SAMPLE_AUDIT_PENDING + 已发起稳定性审核流程）
        List<StabilityResultReviewDTO> candidates = stabilityResultReviewMapper.selectCandidatesForReview(queryDTO);
        if (candidates == null || candidates.isEmpty()) {
            CommonPage<StabilityResultReviewDTO> empty = new CommonPage<>();
            empty.setPageNum(queryDTO.getPageNum());
            empty.setPageSize(queryDTO.getPageSize());
            empty.setTotal(0);
            empty.setList(Collections.emptyList());
            return empty;
        }

        // 2. 构建 businessKey → DTO 的映射
        Map<String, StabilityResultReviewDTO> candidateMap = new HashMap<>();
        List<String> businessKeys = new ArrayList<>();
        for (StabilityResultReviewDTO c : candidates) {
            String key = String.valueOf(c.getInspectionOrderId());
            candidateMap.put(key, c);
            businessKeys.add(key);
        }

        // 3. 从工作流查 STABILITY_RESULT_AUDIT 类别的当前用户待办任务
        FlowAuditTaskDTO flowDto = new FlowAuditTaskDTO();
        flowDto.setCategory(AuditCategoryCodeEnum.STABILITY_RESULT_AUDIT.getCode());
        flowDto.setCurrent(queryDTO.getPageNum());
        flowDto.setSize(queryDTO.getPageSize());
        flowDto.setBusinessKeyList(businessKeys);
        PageQueryResp<List<TaskListResp>> todo = flowAuditService.queryToDoListByCategory(flowDto);

        if (todo == null || todo.getTotal() == 0) {
            CommonPage<StabilityResultReviewDTO> empty = new CommonPage<>();
            empty.setPageNum(queryDTO.getPageNum());
            empty.setPageSize(queryDTO.getPageSize());
            empty.setTotal(0);
            empty.setList(Collections.emptyList());
            return empty;
        }

        // 4. 合并工作流待办字段到业务DTO
        List<StabilityResultReviewDTO> resultList = new ArrayList<>();
        for (TaskListResp task : todo.getData()) {
            StabilityResultReviewDTO base = candidateMap.get(task.getBusinessKey());
            if (base != null) {
                base.setTaskId(task.getTaskId());
                base.setExecutionId(task.getExecutionId());
                base.setDeploymentId(task.getDeploymentId());
                base.setCurrentNodeName(task.getElementName());
                base.setInitiator(task.getProcessStartBy());
                base.setInitiateTime(task.getProcessStartTime());
                base.setPayload(task.getPayload());
                resultList.add(base);
            }
        }

        resultList.sort(
                Comparator.comparing(StabilityResultReviewDTO::getRequestTime, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(StabilityResultReviewDTO::getOrderNo, Comparator.nullsLast(Comparator.naturalOrder()))
        );

        CommonPage<StabilityResultReviewDTO> page = new CommonPage<>();
        page.setPageNum(queryDTO.getPageNum());
        page.setPageSize(queryDTO.getPageSize());
        page.setTotal(todo.getTotal().intValue());
        page.setList(resultList);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String startReview(Long orderId) {
        InspectionOrder order = inspectionOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_ORDER_NOT_EXITS);
        }
        // 状态流转校验：CONFIRMED -> SAMPLE_AUDIT_PENDING
        OrderTransitionGuard.checkOrThrow(order.getOrderStatus(), InspectionOrderStatusEnum.SAMPLE_AUDIT_PENDING);

        // 使用 STABILITY_RESULT_AUDIT 流程类别发起审核
        FlowStartDTO startDTO = new FlowStartDTO();
        startDTO.setBusinessKey(String.valueOf(orderId));
        startDTO.setCode(AuditCategoryCodeEnum.STABILITY_RESULT_AUDIT.getCode());
        startDTO.setCategoryCode(AuditCategoryCodeEnum.STABILITY_RESULT_AUDIT.getCode());
        startDTO.setName(order.getOrderNo());
        startDTO.setExtField(order.getBatchNo());
        String processInstanceId = flowAuditService.flowAuditStart(startDTO);

        // 更新检验单状态
        InspectionOrder update = new InspectionOrder();
        update.setId(order.getId());
        update.setSampleAuditProcessInstanceId(processInstanceId);
        update.setOrderStatus(InspectionOrderStatusEnum.SAMPLE_AUDIT_PENDING);
        inspectionOrderMapper.updateById(update);

        // 同步任务状态至 SAMPLE_AUDIT_PENDING
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskMapper.selectList(
                new LambdaQueryWrapper<Task>()
                        .eq(Task::getInspectionOrderId, order.getId())
                        .ne(Task::getStatus, TaskStatusEnum.TERMINATED)
        );
        com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<Task> uw =
                new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
        uw.eq(Task::getInspectionOrderId, order.getId())
                .ne(Task::getStatus, TaskStatusEnum.TERMINATED)
                .set(Task::getStatus, TaskStatusEnum.SAMPLE_AUDIT_PENDING);
        taskMapper.update(null, uw);

        String operatorId = SysUserHolder.getUser().getUserId();
        for (Task t : tasks) {
            TaskTransitionGuard.checkOrThrow(t.getStatus(), TaskStatusEnum.SAMPLE_AUDIT_PENDING);
            TaskStatusHistory h = new TaskStatusHistory();
            h.setTaskId(t.getId());
            h.setOperationType(TaskOperationTypeEnum.SAMPLE_AUDIT_SUBMIT);
            h.setFromStatus(t.getStatus() == null ? null : t.getStatus().getValue());
            h.setToStatus(TaskStatusEnum.SAMPLE_AUDIT_PENDING.getValue());
            h.setOperatorId(operatorId);
            h.setOperateTime(now);
            taskStatusHistoryMapper.insert(h);
        }
        return processInstanceId;
    }

    @Override
    public StabilityAuditHeaderDTO getAuditHeader(Long orderId) {
        StabilityAuditHeaderDTO header = stabilityResultReviewMapper.selectStabilityAuditHeader(orderId);
        if (header == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_ORDER_NOT_EXITS);
        }
        return header;
    }

    @Override
    public StabilityAuditDetailDTO getAuditDetail(Long orderId) {
        // 1. 查询稳定性特有字段（planCode / schemeCode / schemeVersion / schemeName / remark）
        StabilityAuditHeaderDTO header = stabilityResultReviewMapper.selectStabilityAuditHeader(orderId);
        if (header == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_ORDER_NOT_EXITS);
        }

        // 2. 复用常规样品审核逻辑，获取检验任务数据（任务分组、数据点、requestUserName 等）
        InspectionOrderEntryDTO base = sampleAuditService.getSampleAuditDetail(orderId);

        // 3. 将 base 数据合并到结果对象（继承字段），再用稳定性特有字段覆盖
        StabilityAuditDetailDTO result = new StabilityAuditDetailDTO();
        cn.hutool.core.bean.BeanUtil.copyProperties(base, result);
        result.setPlanCode(header.getPlanCode());
        result.setSchemeCode(header.getSchemeCode());
        result.setSchemeVersion(header.getSchemeVersion());
        result.setSchemeName(header.getSchemeName());
        result.setRemark(header.getRemark());

        return result;
    }

    @Override
    public void approve(StabilityAuditApproveDTO dto) {
        // 电子签名密码验证
        SignatureValidateDTO signDTO = new SignatureValidateDTO();
        signDTO.setLoginName(dto.getLoginName());
        signDTO.setPassword(dto.getPassword());
        signDTO.setSignatureData(String.valueOf(dto.getInspectionOrderId()));
        Boolean valid = signatureService.validate(signDTO);
        if (Boolean.FALSE.equals(valid)) {
            throw new BmosException(LimsResponseCode.STABILITY_RESULT_AUDIT_PASSWORD_WRONG);
        }

        // 调用工作流完成当前节点
        CompleteDTO completeDTO = new CompleteDTO();
        completeDTO.setProcessInstanceId(dto.getProcessInstanceId());
        completeDTO.setTaskId(dto.getTaskId());
        completeDTO.setComment(dto.getComment());
        completeDTO.setRemark(dto.getRemark());
        flowAuditService.flowAuditComplete(completeDTO);
        // 业务状态更新由 AuditFlowProcessEndListener 通过 auditProcessSuccessCallBack 完成
    }

    @Override
    public void returnToPrev(StabilityAuditReturnDTO dto) {
        AuditBackToPrevDTO backDTO = new AuditBackToPrevDTO();
        backDTO.setExecutionId(dto.getExecutionId());
        backDTO.setComment(dto.getComment());
        backDTO.setRemark(dto.getRemark());
        flowAuditService.flowAuditBackToPrev(backDTO);

        // 记录退回历史
        flowAuditService.saveAuditBackHistory(
                String.valueOf(dto.getInspectionOrderId()),
                dto.getComment(),
                dto.getRemark(),
                null,
                AuditBusinessModule.STABILITY_RESULT_AUDIT.getCode()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(StabilityAuditRejectDTO dto) {
        if (dto.getTaskIds() == null || dto.getTaskIds().isEmpty()) {
            throw new BmosException(LimsResponseCode.STABILITY_RESULT_AUDIT_ORDER_STATUS_ERROR);
        }

        InspectionOrder order = inspectionOrderMapper.selectById(dto.getInspectionOrderId());
        if (order == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_ORDER_NOT_EXITS);
        }

        // 校验任务归属与状态（用于后续状态流转校验与历史记录）
        List<Task> rejectedTasks = taskMapper.selectBatchIds(dto.getTaskIds());
        if (rejectedTasks.stream().anyMatch(t -> t == null || !dto.getInspectionOrderId().equals(t.getInspectionOrderId()))) {
            throw new BmosException(LimsResponseCode.STABILITY_RESULT_AUDIT_ORDER_STATUS_ERROR);
        }

        LocalDateTime now = LocalDateTime.now();
        String userId = SysUserHolder.getUser().getUserId();

        // 状态流转校验：SAMPLE_AUDIT_PENDING -> SAMPLE_AUDIT_REJECTED
        OrderTransitionGuard.checkOrThrow(order.getOrderStatus(), InspectionOrderStatusEnum.SAMPLE_AUDIT_REJECTED);

        // 重置检验单为 CONFIRMED（允许重新发起审核）
        InspectionOrder update = new InspectionOrder();
        update.setId(order.getId());
        update.setOrderStatus(InspectionOrderStatusEnum.CONFIRMED);
        inspectionOrderMapper.updateById(update);

        // 被选中任务：标记为 SAMPLE_AUDIT_REJECTED
        com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<Task> rejectUw =
                new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
        rejectUw.eq(Task::getInspectionOrderId, order.getId())
                .in(Task::getId, dto.getTaskIds())
                .ne(Task::getStatus, TaskStatusEnum.TERMINATED)
                .set(Task::getStatus, TaskStatusEnum.SAMPLE_AUDIT_REJECTED)
                .set(Task::getSampleAuditBy, userId)
                .set(Task::getSampleAuditTime, now)
                .set(Task::getRejectReason, dto.getComment())
                .set(Task::getRemark,dto.getRemark())
        ;
        taskMapper.update(null, rejectUw);

        // 其余任务：恢复为 REVIEW_PASSED
        com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<Task> passUw =
                new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
        passUw.eq(Task::getInspectionOrderId, order.getId())
                .notIn(Task::getId, dto.getTaskIds())
                .ne(Task::getStatus, TaskStatusEnum.TERMINATED)
                .set(Task::getStatus, TaskStatusEnum.REVIEW_PASSED)
                .set(Task::getSampleAuditBy, userId)
                .set(Task::getSampleAuditTime, now);
        taskMapper.update(null, passUw);

        // 记录被选中任务的历史
        for (Task t : rejectedTasks) {
            TaskTransitionGuard.checkOrThrow(t.getStatus(), TaskStatusEnum.SAMPLE_AUDIT_REJECTED);
            TaskStatusHistory h = new TaskStatusHistory();
            h.setTaskId(t.getId());
            h.setOperationType(TaskOperationTypeEnum.REVIEW_REJECT);
            h.setFromStatus(t.getStatus() == null ? null : t.getStatus().getValue());
            h.setToStatus(TaskStatusEnum.SAMPLE_AUDIT_REJECTED.getValue());
            h.setOperatorId(userId);
            h.setOperateTime(now);
            h.setComment(dto.getComment());
            h.setRemark(dto.getRemark());
            taskStatusHistoryMapper.insert(h);
        }

        // 调用工作流：审批不通过
        CompleteDTO completeDTO = new CompleteDTO();
        completeDTO.setProcessInstanceId(dto.getProcessInstanceId());
        completeDTO.setTaskId(dto.getTaskId());
        completeDTO.setComment(dto.getComment());
        completeDTO.setRemark(dto.getRemark());
        flowAuditService.flowAuditCompleteNotApprove(completeDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessSuccessCallBack(String processInstanceId, String comment, String remark,
                                             String userId, String nodeName) {
        LambdaQueryWrapper<InspectionOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(InspectionOrder::getSampleAuditProcessInstanceId, processInstanceId);
        InspectionOrder order = inspectionOrderMapper.selectOne(qw);
        if (order == null) {
            log.warn("稳定性结果审核通过回调：未找到检验单，流程实例ID: {}", processInstanceId);
            return;
        }
        LocalDateTime now = LocalDateTime.now();

        // 订单状态流转校验：SAMPLE_AUDIT_PENDING -> COMPLETED
        OrderTransitionGuard.checkOrThrow(order.getOrderStatus(), InspectionOrderStatusEnum.COMPLETED);

        // 标记检验单审核完成
        InspectionOrder update = new InspectionOrder();
        update.setId(order.getId());
        update.setSampleAuditTime(now);
        update.setOrderStatus(InspectionOrderStatusEnum.COMPLETED);
        update.setFinished(Boolean.TRUE);
        update.setFinishedTime(now);
        inspectionOrderMapper.updateById(update);

        // 回调稳定性考察计划，将关联时间点任务标记为 COMPLETED
        stabilityInspectPlanService.onInspectionOrderFinished(order.getId());

        // 任务状态更新为 COMPLETED
        List<Task> tasks = taskMapper.selectList(
                new LambdaQueryWrapper<Task>()
                        .eq(Task::getInspectionOrderId, order.getId())
                        .ne(Task::getStatus, TaskStatusEnum.TERMINATED)
        );
        com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<Task> uw =
                new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
        uw.eq(Task::getInspectionOrderId, order.getId())
                .ne(Task::getStatus, TaskStatusEnum.TERMINATED)
                .set(Task::getStatus, TaskStatusEnum.COMPLETED)
                .set(Task::getSampleAuditBy, userId)
                .set(Task::getSampleAuditTime, now);
        taskMapper.update(null, uw);

        for (Task t : tasks) {
            TaskTransitionGuard.checkOrThrow(t.getStatus(), TaskStatusEnum.COMPLETED);
            TaskStatusHistory h = new TaskStatusHistory();
            h.setTaskId(t.getId());
            h.setOperationType(TaskOperationTypeEnum.REVIEW_PASS);
            h.setFromStatus(t.getStatus() == null ? null : t.getStatus().getValue());
            h.setToStatus(TaskStatusEnum.COMPLETED.getValue());
            h.setOperatorId(userId);
            h.setOperateTime(now);
            h.setNodeName(nodeName);
            h.setComment(comment);
            h.setRemark(remark);
            taskStatusHistoryMapper.insert(h);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auditProcessRejectCallBack(String processInstanceId, String comment, String remark,
                                            String userId, String nodeName) {
        LambdaQueryWrapper<InspectionOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(InspectionOrder::getSampleAuditProcessInstanceId, processInstanceId);
        InspectionOrder order = inspectionOrderMapper.selectOne(qw);
        if (order == null) {
            log.warn("稳定性结果审核不通过回调：未找到检验单，流程实例ID: {}", processInstanceId);
            return;
        }

        // 保存审核操作日志
        auditOperationLogService.save(AuditOperationLogEntity.builder()
                .module(AuditBusinessModule.STABILITY_RESULT_AUDIT.name())
                .businessId(order.getId())
                .operationType(OperationType.REJECT_AUDIT.getValue())
                .remark(remark)
                .nodeName(nodeName)
                .comment(comment)
                .createBy(userId)
                .build());
    }
}
