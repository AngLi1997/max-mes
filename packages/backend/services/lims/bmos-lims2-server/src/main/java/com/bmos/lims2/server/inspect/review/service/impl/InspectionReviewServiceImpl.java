package com.bmos.lims2.server.inspect.review.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.common.enums.InspectionOrderSourceEnum;
import com.bmos.lims2.server.inspect.audit.service.SampleAuditService;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.inspect.entry.dto.*;
import com.bmos.lims2.server.inspect.entry.mapper.InspectionEntryRecordMapper;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.stability.review.service.StabilityResultReviewService;
import com.bmos.lims2.server.inspect.review.service.InspectionReviewService;
import com.bmos.lims2.server.task.entity.Task;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.lims2.common.enums.TaskOperationTypeEnum;
import com.bmos.lims2.server.task.entity.TaskStatusHistory;
import com.bmos.lims2.server.task.mapper.TaskStatusHistoryMapper;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.mapper.ExecuteFormDataMapper;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordComponentMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InspectionReviewServiceImpl implements InspectionReviewService {

    private final InspectionEntryRecordMapper entryMapper;
    private final TaskMapper taskMapper;
    private final TaskStatusHistoryMapper taskStatusHistoryMapper;
    private final InspectionOrderMapper orderMapper;
    private final SampleAuditService sampleAuditService;
    private final ExecuteFormDataMapper executeFormDataMapper;
    private final BatchRecordComponentMapper batchRecordComponentMapper;
    private final StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    @Autowired
    @Lazy
    private StabilityResultReviewService stabilityResultReviewService;

    @Override
    public CommonPage<AnalysisItemEntryDTO> getAnalysisItemReviewPage(AnalysisItemEntryQueryDTO queryDTO) {
        queryDTO.setUnreviewedOnly(Boolean.TRUE);
        if (CollUtil.isEmpty(queryDTO.getTaskStatuses())) {
            queryDTO.setTaskStatuses(Collections.singletonList(TaskStatusEnum.TO_REVIEW));
        }
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<AnalysisItemEntryDTO> list = entryMapper.selectAnalysisItemEntryList(queryDTO);
        for (AnalysisItemEntryDTO item : list) {
            List<AnalysisItemEntryDTO.InspectionOrderEntryItemDTO> children =
                    entryMapper.selectInspectionOrdersByAnalysisItem(item.getInspectItemId(), item.getInspectParameterId(), item.getRecordVersionId(), queryDTO);
            // 为每个检验单任务查询关联的数据点
            java.util.Map<Long, Boolean> stabilityVersionCache1 = new java.util.HashMap<>();
            for (AnalysisItemEntryDTO.InspectionOrderEntryItemDTO inspectionOrder : children) {
                Long svId = inspectionOrder.getSchemeVersionId();
                boolean isStab = svId != null && stabilityVersionCache1.computeIfAbsent(svId,
                        id -> stabilitySchemeVersionMapper.selectById(id) != null);
                List<InspectionEntryRecordDTO> dataPoints = isStab
                        ? entryMapper.selectByTaskIdForStability(inspectionOrder.getId())
                        : entryMapper.selectByTaskId(inspectionOrder.getId());
                inspectionOrder.setDataPoints(dataPoints);
            }
            item.setInspectionTasks(children);
        }
        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<InspectionOrderEntryDTO> getInspectionOrderReviewPage(InspectionOrderEntryQueryDTO queryDTO) {
        queryDTO.setUnreviewedOnly(Boolean.TRUE);
        if (CollUtil.isEmpty(queryDTO.getTaskStatuses())) {
            queryDTO.setTaskStatuses(Collections.singletonList(TaskStatusEnum.TO_REVIEW));
        }
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<InspectionOrderEntryDTO> list = entryMapper.selectInspectionOrderEntryList(queryDTO);
        for (InspectionOrderEntryDTO order : list) {
            List<InspectionOrderEntryDTO.AnalysisItemEntryItemDTO> children =
                    entryMapper.selectTasksByInspectionOrderPage(order.getInspectionOrderId(), queryDTO);
            // 为每个检验单任务查询关联的数据点
            java.util.Map<Long, Boolean> stabilityVersionCache2 = new java.util.HashMap<>();
            for (InspectionOrderEntryDTO.AnalysisItemEntryItemDTO task : children) {
                Long svId = task.getSchemeVersionId();
                boolean isStab = svId != null && stabilityVersionCache2.computeIfAbsent(svId,
                        id -> stabilitySchemeVersionMapper.selectById(id) != null);
                List<InspectionEntryRecordDTO> dataPoints = isStab
                        ? entryMapper.selectByTaskIdForStability(task.getId())
                        : entryMapper.selectByTaskId(task.getId());
                task.setDataPoints(dataPoints);
            }
            order.setInspectionTasks(children);
        }
        return CommonPage.convertPage(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchReview(List<Long> taskIds, String reviewerId, boolean approve, String reason) {
        if (CollUtil.isEmpty(taskIds)) {
            return;
        }
        String currentUserId = SysUserHolder.getUser().getUserId();

        // 校验任务状态：必须待复核且未被复核
        List<Task> tasks = taskMapper.selectList(new LambdaQueryWrapper<Task>().in(Task::getId, taskIds));
        if (tasks.stream().anyMatch(Objects::isNull)) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_NOT_FOUND);
        }
        for (Task t : tasks) {
            if (t.getStatus() != TaskStatusEnum.TO_REVIEW) {
                throw new BmosException(LimsResponseCode.REVIEW_TASK_STATUS_ERROR);
            }
        }


        //录入人校验：复核人不可为任一数据点录入人
        List<TaskOperatorDTO> operatorPairs = entryMapper.selectTaskOperatorsByTaskIds(taskIds);
        Set<String> operatorIds = operatorPairs.stream()
                .map(TaskOperatorDTO::getOperatorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (operatorIds.contains(reviewerId)) {
            throw new BmosException(LimsResponseCode.REVIEW_OPERATOR_ERROR, "录入人不得复核本人录入的数据");
        }

        // TODO: 2025/10/29 需求改变，不再进行这个校验
//        // 若复核通过，则不允许存在判定异常
//        if (approve) {
//            boolean anyAbnormal = tasks.stream().anyMatch(t -> Boolean.TRUE.equals(t.getJudgedAbnormal()));
//            if (anyAbnormal) {
//                throw new BmosException(LimsResponseCode.REVIEW_DATA_ERROR);
//            }
//        }

        // 批量更新复核结果：统一到任务状态（复核通过/不通过）
        LocalDateTime now = LocalDateTime.now();
        LambdaUpdateWrapper<Task> uw = new LambdaUpdateWrapper<>();
        uw.in(Task::getId, taskIds)
                .set(Task::getStatus, approve ? TaskStatusEnum.REVIEW_PASSED : TaskStatusEnum.REVIEW_REJECTED)
                .set(Task::getUpdateBy, currentUserId)
                .set(Task::getReviewedBy, reviewerId)
                .set(Task::getReviewedTime, now)
                .set(!approve, Task::getReviewedAbnormalReason, reason)
                .set(Task::getUpdateTime, now);
        taskMapper.update(null, uw);

        // 写入任务状态变更到任务状态历史表（使用ENTRY_STATUS_CHANGE操作类型）
        for (Task t : tasks) {
            TaskStatusHistory h = new TaskStatusHistory();
            h.setTaskId(t.getId());
            h.setOperationType(approve ? TaskOperationTypeEnum.REVIEW_PASS : TaskOperationTypeEnum.REVIEW_REJECT);
            // 读取真实的fromStatus，避免写死为TO_REVIEW
            TaskStatusEnum from = t.getStatus();
            // 守卫：确保TO->目标的流转合法
            com.bmos.lims2.server.task.util.TaskTransitionGuard.checkOrThrow(from, approve ? TaskStatusEnum.REVIEW_PASSED : TaskStatusEnum.REVIEW_REJECTED);
            // 注意：上面已批量更新数据库中的状态，但此处t仍为旧对象，t.getStatus()为原始状态
            h.setFromStatus(from == null ? null : from.getValue());
            h.setToStatus(approve ? TaskStatusEnum.REVIEW_PASSED.getValue() : TaskStatusEnum.REVIEW_REJECTED.getValue());
            h.setOperatorId(currentUserId);
            h.setOperateTime(now);
            h.setComment(( reason));
            taskStatusHistoryMapper.insert(h);
        }

        // 如果本次操作为”复核通过”，尝试自动发起样品审核流程（后台发起）
        if (approve) {
            // 聚合受影响的检验单ID
            java.util.Set<Long> orderIds = tasks.stream()
                    .map(Task::getInspectionOrderId)
                    .filter(java.util.Objects::nonNull)
                    .collect(java.util.stream.Collectors.toSet());
            if (!orderIds.isEmpty()) {
                // 找出满足条件（所有任务已 REVIEW_PASSED，参数任务齐备，未发起流程，未完成）的订单
                java.util.List<Long> eligibleOrderIds = this.orderMapper.selectEligibleForSampleAuditStart(new java.util.ArrayList<>(orderIds));
                if (eligibleOrderIds != null && !eligibleOrderIds.isEmpty()) {
                    List<InspectionOrder> eligibleOrders = this.orderMapper.selectBatchIds(eligibleOrderIds);
                    for (InspectionOrder order : eligibleOrders) {
                        if (InspectionOrderSourceEnum.STABILITY == order.getSchemeSource()) {
                            // 稳定性考察检验单 → 自动发起稳定性结果审核流程
                            this.stabilityResultReviewService.startReview(order.getId());
                        } else {
                            // 常规检验单 → 自动发起样品审核流程
                            this.sampleAuditService.startSampleAudit(order.getId());
                        }
                    }
                }
            }
        }
    }
}


