package com.bmos.lims2.server.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.InspectionOrderSourceEnum;
import com.bmos.lims2.common.enums.TaskOperationTypeEnum;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.task.entity.Task;
import com.bmos.lims2.server.task.entity.TaskStatusHistory;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.lims2.server.task.mapper.TaskStatusHistoryMapper;
import com.bmos.lims2.server.task.service.TaskService;
import com.bmos.lims2.server.inspect.entry.service.AppTaskQueryService;
import com.bmos.lims2.server.task.dto.*;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.bmos.lims2.server.eln.entry.mapper.ExecuteFormDataMapper;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordComponentMapper;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeJudgmentMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;

/**
 * 任务服务实现类
 *
 * @author system
 * @since 2025/01/29
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskStatusHistoryMapper taskStatusHistoryMapper;

    @Autowired
    private AppTaskQueryService appTaskQueryService;

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private ExecuteFormDataMapper executeFormDataMapper;

    @Autowired
    private BatchRecordComponentMapper batchRecordComponentMapper;

    @Autowired
    private StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    @Autowired
    private StabilitySchemeJudgmentMapper stabilitySchemeJudgmentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateTasksAfterSampleReceived(Long sampleId, SysUser user) {
        if (sampleId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品ID不能为空");
        }

        log.info("开始为样品{}生成任务（触发点：样品领取）", sampleId);

        // 1. 查询样品信息
        Sample sample = sampleMapper.selectById(sampleId);
        if (sample == null) {
            log.warn("样品不存在，样品ID：{}", sampleId);
            return;
        }
        log.debug("查询到样品信息：{}", sample.getSampleNo());

        // 2. 查询样品对应的检验单
        InspectionOrder inspectionOrder = inspectionOrderMapper.selectById(sample.getInspectionOrderId());
        if (inspectionOrder == null) {
            log.warn("检验单不存在，检验单ID：{}", sample.getInspectionOrderId());
            return;
        }
        log.debug("查询到检验单信息：{}", inspectionOrder.getOrderNo());

        // 3. 校验触发条件：必须为已领取样品，且指定了检验项目
        if (!Boolean.TRUE.equals(sample.getCollected())) {
            log.info("样品{}未处于已领取状态，跳过任务生成", sample.getSampleNo());
            return;
        }
        if (sample.getInspectItemId() == null) {
            log.info("样品{}未指定检验项目，跳过任务生成", sample.getSampleNo());
            return;
        }

        // 4. 根据检验方案版本ID查询检验项目和分析项配置
        List<SchemeParameterDTO> schemeItemsAndParameters = taskMapper.selectSchemeItemsAndParameters(inspectionOrder.getSchemeVersionId());
        if (CollectionUtils.isEmpty(schemeItemsAndParameters)) {
            log.warn("检验方案版本{}未配置检验项目和分析项", inspectionOrder.getSchemeVersionId());
            return;
        }

        // 5. 过滤出与样品检验项目匹配的分析项
        List<SchemeParameterDTO> matchedParameters = schemeItemsAndParameters.stream()
                .filter(item -> sample.getInspectItemId().equals(item.getInspectItemId()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(matchedParameters)) {
            log.warn("样品{}的检验项目{}在检验方案中未找到匹配的分析项",
                    sample.getSampleNo(), sample.getInspectItemId());
            return;
        }

        log.info("为样品{}找到{}个分析项，开始生成任务", sample.getSampleNo(), matchedParameters.size());

        // 6. 为每个分析项生成任务
        int generatedTaskCount = 0;
        for (SchemeParameterDTO parameterConfig : matchedParameters) {
            Long parameterId = parameterConfig.getParameterId();

            // 检查任务是否已存在（基于检验单 + 检验项目 + 分析项），避免重复生成
            if (taskMapper.existsTaskByOrderItemAndParameter(
                    inspectionOrder.getId(), parameterConfig.getInspectItemId(), parameterId)) {
                log.debug("任务已存在，跳过生成：检验单ID={}，检验项目ID={}，分析项ID={}",
                        inspectionOrder.getId(), parameterConfig.getInspectItemId(), parameterId);
                continue;
            }

            LocalDateTime now = LocalDateTime.now();

            // 创建任务
            Task task = createTask(inspectionOrder, parameterConfig,user);
            task.setCreateTime( now);
            task.setUpdateTime( now);
            taskMapper.insert(task);

            // 记录任务状态变更历史
            recordStatusHistory(task.getId(), null, TaskStatusEnum.PENDING_ASSIGNMENT,
                    TaskOperationTypeEnum.CREATE, "样品接收完成后自动生成", now,user);

            generatedTaskCount++;
            log.debug("为样品{}生成任务成功：, 分析项={}",
                    sample.getSampleNo(), parameterConfig.getParameterName());
        }

        log.info("为样品{}生成任务完成，共生成{}个任务", sample.getSampleNo(), generatedTaskCount);
    }

    /**
     * 创建任务对象
     */
    private Task createTask(InspectionOrder inspectionOrder, SchemeParameterDTO parameterConfig, SysUser user) {
        Task task = new Task();

        // 基本信息
        task.setInspectionOrderId(inspectionOrder.getId());
        task.setSchemeVersionId(inspectionOrder.getSchemeVersionId());

        // 检验项目信息
        task.setInspectItemId(parameterConfig.getInspectItemId());
        task.setInspectItemCode(parameterConfig.getInspectItemCode());

        // 方案配置冗余信息
        task.setItemConfigId(parameterConfig.getSchemeItemId());

        // 分析项信息
        task.setParameterId(parameterConfig.getParameterId());
        task.setParameterCode(parameterConfig.getParameterCode());
        task.setIsExecutable(parameterConfig.getIsExecutable());
        task.setIsReportable(parameterConfig.getIsReportable());
        // 执行方式
        task.setExecuteMethod(parameterConfig.getExecuteMethod());

        // 若为ELN执行方式：必须写入方法ID与生效版本ID（从方案配置快照中获取）
        if (parameterConfig.getExecuteMethod() == ExecuteMethodEnum.ELN) {
            // 保存方案参数上配置的方法项ID（如有）
            if (parameterConfig.getRecordItemId() != null) {
                task.setRecordItemId(parameterConfig.getRecordItemId());
            }
            if (parameterConfig.getRecordId() == null || parameterConfig.getRecordVersionId() == null) {
                throw new BmosException(LimsResponseCode.INVALID_PARAM, "方案配置缺少ELN方法或方法版本，请检查");
            }
            task.setRecordId(parameterConfig.getRecordId());
            task.setRecordVersionId(parameterConfig.getRecordVersionId());
        }

        // 方案配置冗余信息（分析项）
        task.setParameterConfigId(parameterConfig.getSchemeParameterId());

        // 任务状态（合并录入状态到任务状态，待完成=待录入）
        task.setStatus(TaskStatusEnum.PENDING_ASSIGNMENT);

        // 创建信息
        task.setCreateBy(user.getUserId());
        task.setUpdateBy(user.getUserId());

        return task;
    }

    @Override
    public CommonPage<AnalysisItemAssignmentDTO> queryAnalysisItemAssignmentPage(TaskQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 1. 查询分析项分配列表（母列表）
        List<AnalysisItemAssignmentDTO> analysisItems = taskMapper.selectAnalysisItemAssignmentPage(queryDTO);

        // 2. 为每个分析项查询对应的检验单任务分组（子列表）
        for (AnalysisItemAssignmentDTO analysisItem : analysisItems) {
            List<InspectionOrderTaskGroupDTO> inspectionOrderGroups =
                    taskMapper.selectInspectionOrderTaskGroups(analysisItem.getInspectItemId(),analysisItem.getInspectParameterId(), queryDTO);
            supplementStabilityGroupFields(inspectionOrderGroups);
            analysisItem.setInspectionOrders(inspectionOrderGroups);
        }

        return CommonPage.convertPage(analysisItems);
    }

    @Override
    public CommonPage<TaskDTO> queryInspectionOrderTaskPage(TaskQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<TaskDTO> taskDTOS = taskMapper.selectInspectionOrderTaskPage(queryDTO);
        return CommonPage.convertPage(taskDTOS);
    }

    @Override
    public CommonPage<InspectionOrderAssignmentDTO> queryInspectionOrderAssignmentPage(TaskQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 1. 查询检验单分配列表（母列表）
        List<InspectionOrderAssignmentDTO> inspectionOrders = taskMapper.selectInspectionOrderAssignmentPage(queryDTO);

        // 2. 为每个检验单查询对应的任务列表（子列表）
        for (InspectionOrderAssignmentDTO inspectionOrder : inspectionOrders) {
            List<TaskDTO> tasks = taskMapper.selectTasksByInspectionOrder(inspectionOrder.getInspectionOrderId(), queryDTO);
            supplementStabilityTaskFields(tasks);
            inspectionOrder.setTasks(tasks);
        }

        return CommonPage.convertPage(inspectionOrders);
    }

    @Override
    public List<TaskStatusCountDTO> queryTaskStatusCount(TaskQueryDTO queryDTO) {
        return taskMapper.selectTaskStatusCount(queryDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignTasks(TaskAssignDTO assignDTO) {
        if (CollectionUtils.isEmpty(assignDTO.getTaskIds())) {
            throw new BmosException(LimsResponseCode.TASK_ERROR_ID_EMPTY);
        }

        // 校验任务状态
        List<Task> tasks = taskMapper.selectBatchIds(assignDTO.getTaskIds());
        for (Task task : tasks) {
            if (TaskStatusEnum.PENDING_ASSIGNMENT!=task.getStatus()) {
                throw new BmosException(LimsResponseCode.TASK_ERROR_STATUS_NOT_MATCH);
            }
        }

        // 校验分配人员权限
        validateUserTaskPermission(assignDTO.getAssigneeId()+"", assignDTO.getTaskIds());

        // 批量更新任务状态
        String currentUserId = SysUserHolder.getUser().getUserId();
        String currentUserName = SysUserHolder.getUser().getUserName();

        LocalDateTime now = LocalDateTime.now();

        tasks.forEach(task -> {
            task.setUpdateBy(currentUserId);
            task.setUpdateTime(LocalDateTime.now());
            task.setStatus(TaskStatusEnum.PENDING_COMPLETION);
            task.setOwnerId(assignDTO.getAssigneeId()+"");
            task.setOwnerName(assignDTO.getAssigneeName());
            task.setAssignTime(now);
            task.setAssignerId(currentUserId);
            task.setAssignerName(currentUserName);
        });

        taskMapper.updateBatch(tasks);
//
//        taskMapper.batchUpdateTaskStatus(
//                assignDTO.getTaskIds(),
//                TaskStatusEnum.PENDING_COMPLETION,
//                assignDTO.getAssigneeId(),
//                assignDTO.getAssigneeName(),
//                currentUserId,
//                currentUserName
//        );

        // 记录状态变更历史
        for (Long taskId : assignDTO.getTaskIds()) {
            recordStatusHistory(taskId, TaskStatusEnum.PENDING_ASSIGNMENT,
                    TaskStatusEnum.PENDING_COMPLETION, TaskOperationTypeEnum.ASSIGNMENT, assignDTO.getRemark(),now, SysUserHolder.getUser());
        }

        log.info("任务分配完成，任务数量：{}，分配给：{}", assignDTO.getTaskIds().size(), assignDTO.getAssigneeName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claimTasks(List<Long> taskIds) {
        if (CollectionUtils.isEmpty(taskIds)) {
            throw new BmosException(LimsResponseCode.TASK_ERROR_ID_EMPTY);
        }

        // 校验任务状态
        List<Task> tasks = taskMapper.selectBatchIds(taskIds);
        for (Task task : tasks) {
            if (!TaskStatusEnum.PENDING_ASSIGNMENT.equals(task.getStatus())) {
                throw new BmosException(LimsResponseCode.TASK_ERROR_STATUS_NOT_MATCH_CLAIM);
            }
        }

        // 校验领取人员权限
        String currentUserId = SysUserHolder.getUser().getUserId();
        validateUserTaskPermission(currentUserId, taskIds);

        // 批量更新任务状态
        String currentUserName = SysUserHolder.getUser().getUserName();
        LocalDateTime now = LocalDateTime.now();

        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Task::getId, taskIds)
                .set(Task::getStatus, TaskStatusEnum.PENDING_COMPLETION)
                .set(Task::getOwnerId, currentUserId)
                .set(Task::getOwnerName, currentUserName)
                .set(Task::getUpdateBy, currentUserId)
                .set(Task::getUpdateTime, now);
        taskMapper.update(null, updateWrapper);

        // 记录状态变更历史
        for (Long taskId : taskIds) {
            recordStatusHistory(taskId, TaskStatusEnum.PENDING_ASSIGNMENT,
                    TaskStatusEnum.PENDING_COMPLETION, TaskOperationTypeEnum.CLAIM, null, now, SysUserHolder.getUser());
        }

        log.info("任务领取完成，任务数量：{}，领取人：{}", taskIds.size(), currentUserName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnTasks(TaskReturnDTO returnDTO) {
        if (CollectionUtils.isEmpty(returnDTO.getTaskIds())) {
            throw new BmosException(LimsResponseCode.TASK_ERROR_ID_EMPTY);
        }

        // 校验任务状态和权限
        List<Task> tasks = taskMapper.selectBatchIds(returnDTO.getTaskIds());
        String currentUserId = SysUserHolder.getUser().getUserId();

        for (Task task : tasks) {
            if (TaskStatusEnum.PENDING_COMPLETION!=task.getStatus()) {
                throw new BmosException(LimsResponseCode.TASK_ERROR_STATUS_NOT_MATCH_RETURN);
            }else if (!currentUserId.equals(task.getOwnerId())) {
                throw new BmosException(LimsResponseCode.TASK_ERROR_OWNER_NOT_MATCH_RETURN);
            }
        }


        // 批量更新任务状态
        LocalDateTime now = LocalDateTime.now();

        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Task::getId, returnDTO.getTaskIds())
                .set(Task::getStatus, TaskStatusEnum.RETURN_PENDING_APPROVAL)
                .set(Task::getUpdateBy, currentUserId)
                .set(Task::getReturnReason, returnDTO.getReturnReason())
                .set(Task::getUpdateTime, now);
        taskMapper.update(null, updateWrapper);

        // 记录状态变更历史
        for (Long taskId : returnDTO.getTaskIds()) {
            recordStatusHistory(taskId, TaskStatusEnum.PENDING_COMPLETION,
                    TaskStatusEnum.RETURN_PENDING_APPROVAL, TaskOperationTypeEnum.RETURN, returnDTO.getReturnReason(), now, SysUserHolder.getUser());
        }

        log.info("任务退回完成，任务数量：{}", returnDTO.getTaskIds().size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveReturnTasks(TaskApprovalDTO approvalDTO) {
        if (CollectionUtils.isEmpty(approvalDTO.getTaskIds())) {
            throw new BmosException(LimsResponseCode.TASK_ERROR_ID_EMPTY);
        }

        // 校验任务状态
        List<Task> tasks = taskMapper.selectBatchIds(approvalDTO.getTaskIds());
        for (Task task : tasks) {
            if (!TaskStatusEnum.RETURN_PENDING_APPROVAL.equals(task.getStatus())) {
                throw new BmosException(LimsResponseCode.TASK_ERROR_APPROVE_STATUS_NOT_MATCH);
            }
        }

        TaskStatusEnum newStatus;
        TaskOperationTypeEnum operationType;

        String currentUserId = SysUserHolder.getUser().getUserId();
        String currentUserName = SysUserHolder.getUser().getUserName();
        LocalDateTime now = LocalDateTime.now();

        if (approvalDTO.getApproved()) {
            // 审批通过，任务状态变为待分配，清空所有人、所有人名称、分配人、分配人名称、分配时间
            newStatus = TaskStatusEnum.PENDING_ASSIGNMENT;
            operationType = TaskOperationTypeEnum.APPROVAL_PASS;

            // 使用UpdateWrapper批量更新，可以设置null值
            LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(Task::getId, approvalDTO.getTaskIds())
                    .set(Task::getStatus, newStatus)
                    .set(Task::getOwnerId, null)
                    .set(Task::getOwnerName, null)
                    .set(Task::getAssignerId, null)
                    .set(Task::getAssignerName, null)
                    .set(Task::getAssignTime, null)
                    .set(Task::getClaimTime, null)
                    .set(Task::getReturnReason, null)
                    .set(Task::getRejectReason, null)
                    .set(Task::getUpdateBy, currentUserId)
                    .set(Task::getUpdateTime, now);
            taskMapper.update(null, updateWrapper);

        } else {
            // 审批不通过，任务状态变为待完成，所有人不变
            newStatus = TaskStatusEnum.PENDING_COMPLETION;
            operationType = TaskOperationTypeEnum.APPROVAL_REJECT;

            // 使用UpdateWrapper批量更新任务状态，保持其他字段不变
            LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.in(Task::getId, approvalDTO.getTaskIds())
                    .set(Task::getStatus, newStatus)
                    .set(Task::getUpdateBy, currentUserId)
                    .set(Task::getRejectReason, approvalDTO.getApprovalReason())
                    .set(Task::getUpdateTime, now);
            taskMapper.update(null, updateWrapper);
        }

        // 记录状态变更历史
        for (Long taskId : approvalDTO.getTaskIds()) {
            recordStatusHistory(taskId, TaskStatusEnum.RETURN_PENDING_APPROVAL,
                    newStatus, operationType, approvalDTO.getApprovalReason(), now, SysUserHolder.getUser());
        }

        log.info("任务退回审批完成，任务数量：{}，审批结果：{}",
                approvalDTO.getTaskIds().size(), approvalDTO.getApproved() ? "通过" : "不通过");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void terminateTasks(TaskTerminateDTO terminateDTO) {
        if (CollectionUtils.isEmpty(terminateDTO.getTaskIds())) {
            throw new BmosException(LimsResponseCode.TASK_ERROR_ID_EMPTY);
        }

        // 校验任务状态
        List<Task> tasks = taskMapper.selectBatchIds(terminateDTO.getTaskIds());
        for (Task task : tasks) {
            if (TaskStatusEnum.TERMINATED.equals(task.getStatus()) ||
                    TaskStatusEnum.COMPLETED.equals(task.getStatus())) {
                throw new BmosException(LimsResponseCode.TASK_ERROR_STATUS_NOT_MATCH_TERMINATE);
            }
        }

        // 批量更新任务状态
        String currentUserId = SysUserHolder.getUser().getUserId();
        LocalDateTime now = LocalDateTime.now();

        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Task::getId, terminateDTO.getTaskIds())
                .set(Task::getStatus, TaskStatusEnum.TERMINATED)
                .set(Task::getTerminateReason, terminateDTO.getTerminateReason())
                .set(Task::getTerminateTime, now)
                .set(Task::getUpdateBy, currentUserId)
                .set(Task::getUpdateTime, now);
        taskMapper.update(null, updateWrapper);

        // 记录状态变更历史
        for (Task task : tasks) {
            recordStatusHistory(task.getId(), task.getStatus(),
                    TaskStatusEnum.TERMINATED, TaskOperationTypeEnum.TERMINATED, terminateDTO.getTerminateReason(), now, SysUserHolder.getUser());
        }

        log.info("任务终止完成，任务数量：{}", terminateDTO.getTaskIds().size());
    }

    @Override
    public List<TaskStatusHistoryDTO> queryTaskStatusHistory(Long taskId) {
        if (taskId == null) {
            throw new BmosException(LimsResponseCode.TASK_ERROR_ID_EMPTY);
        }
        List<TaskStatusHistory> histories = taskStatusHistoryMapper.selectByTaskId(taskId);
        return histories.stream().map(history -> {
            TaskStatusHistoryDTO dto = new TaskStatusHistoryDTO();
            org.springframework.beans.BeanUtils.copyProperties(history, dto);
            return dto;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<AssignableUserDTO> queryAssignableUsers(List<Long> taskIds) {
        if (CollectionUtils.isEmpty(taskIds)) {
            throw new BmosException(LimsResponseCode.TASK_ERROR_ID_EMPTY);
        }

        log.info("开始查询可分配人员，任务数量：{}", taskIds.size());

        // 1. 查询任务的检验方案配置信息
        List<TaskSchemeConfigDTO> taskSchemeConfigs = taskMapper.selectTaskSchemeConfigByTaskIds(taskIds);
        if (CollectionUtils.isEmpty(taskSchemeConfigs)) {
            log.warn("未找到任务的检验方案配置信息，任务ID列表：{}", taskIds);
            return new ArrayList<>();
        }
        log.debug("查询到任务检验方案配置信息：{}", taskSchemeConfigs);

        // 2. 计算“可分配人员”的交集（而非班组交集）：
        //    对于每个任务，根据检验项目+方案版本查出可操作的班组，再查班组下成员；
        //    最终对各任务得到的人员集合做交集。
        if (taskSchemeConfigs.size() == 1) {
            // 单任务场景：直接查询该任务对应班组下的所有人员并去重
            TaskSchemeConfigDTO config = taskSchemeConfigs.get(0);
            List<Long> teamIds = queryTeamIdsByConfig(config);
            if (CollectionUtils.isEmpty(teamIds)) {
                log.warn("任务对应的班组为空，inspectItemId={}, schemeVersionId={}",
                        config.getInspectItemId(), config.getSchemeVersionId());
                return new ArrayList<>();
            }
            List<AssignableUserDTO> users = taskMapper.selectUsersByTeamIds(teamIds);
            if (CollectionUtils.isEmpty(users)) {
                log.warn("班组中无人员，班组ID列表：{}", teamIds);
                return new ArrayList<>();
            }
            List<AssignableUserDTO> dedupedUsers = new ArrayList<>(
                    users.stream()
                            .collect(Collectors.toMap(
                                    AssignableUserDTO::getUserId,
                                    x -> x,
                                    (existing, replacement) -> existing,
                                    LinkedHashMap::new
                            ))
                            .values()
            );

            List<Long> userIds = dedupedUsers.stream()
                    .map(u -> Long.valueOf(u.getUserId()))
                    .collect(Collectors.toList());
            List<UserPendingTaskCountDTO> counts = taskMapper.selectUserPendingTaskCount(userIds);
            Map<Long, Long> countMap = counts.stream().collect(Collectors.toMap(
                    UserPendingTaskCountDTO::getUserId,
                    UserPendingTaskCountDTO::getPendingTaskCount,
                    (e, r) -> e
            ));
            dedupedUsers.forEach(u -> u.setPendingTaskCount(countMap.getOrDefault(Long.valueOf(u.getUserId()), 0L)));
            log.info("查询可分配人员完成（单任务），人员数量：{}", dedupedUsers.size());
            return dedupedUsers;
        }

        // 多任务：逐任务取人员集合并做交集
        Set<Long> intersectionUserIds = null;
        Map<Long, AssignableUserDTO> userInfoMap = new LinkedHashMap<>();

        for (TaskSchemeConfigDTO config : taskSchemeConfigs) {
            List<Long> teamIds = queryTeamIdsByConfig(config);
            if (CollectionUtils.isEmpty(teamIds)) {
                log.warn("任务对应的班组为空，inspectItemId={}, schemeVersionId={}",
                        config.getInspectItemId(), config.getSchemeVersionId());
                return new ArrayList<>();
            }

            List<AssignableUserDTO> usersForTask = taskMapper.selectUsersByTeamIds(teamIds);
            if (CollectionUtils.isEmpty(usersForTask)) {
                log.warn("班组中无人员，班组ID列表：{}", teamIds);
                return new ArrayList<>();
            }

            // 当前任务用户集合（去重到 userId）
            Map<Long, AssignableUserDTO> currentTaskUserMap = usersForTask.stream().collect(Collectors.toMap(
                    u -> Long.valueOf(u.getUserId()),
                    u -> u,
                    (e, r) -> e,
                    LinkedHashMap::new
            ));
            Set<Long> currentUserIds = currentTaskUserMap.keySet();

            if (intersectionUserIds == null) {
                intersectionUserIds = new LinkedHashSet<>(currentUserIds);
                // 保存一份用户信息映射（取首次出现的）
                currentTaskUserMap.forEach(userInfoMap::putIfAbsent);
            } else {
                intersectionUserIds.retainAll(currentUserIds);
                if (intersectionUserIds.isEmpty()) {
                    log.warn("计算人员交集为空，无法分配人员");
                    return new ArrayList<>();
                }
                // 累积用户信息（首次为准）
                currentTaskUserMap.forEach(userInfoMap::putIfAbsent);
            }
        }

        if (intersectionUserIds == null || intersectionUserIds.isEmpty()) {
            log.warn("计算人员交集为空，无法分配人员");
            return new ArrayList<>();
        }

        // 3. 依据人员交集组装返回，并统计每个人当前待完成任务数量
        List<Long> finalUserIds = new ArrayList<>(intersectionUserIds);
        List<AssignableUserDTO> finalUsers = finalUserIds.stream()
                .map(userInfoMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<UserPendingTaskCountDTO> userTaskCounts = taskMapper.selectUserPendingTaskCount(finalUserIds);
        Map<Long, Long> taskCountMap = userTaskCounts.stream()
                .collect(Collectors.toMap(
                        UserPendingTaskCountDTO::getUserId,
                        UserPendingTaskCountDTO::getPendingTaskCount,
                        (existing, replacement) -> existing
                ));

        finalUsers.forEach(user -> {
            Long uid = Long.valueOf(user.getUserId());
            user.setPendingTaskCount(taskCountMap.getOrDefault(uid, 0L));
        });

        log.info("查询可分配人员完成（多任务人员交集），人员数量：{}", finalUsers.size());
        return finalUsers;
    }

    /**
     * 计算多个任务的班组交集
     * 只有在所有任务对应的检验项目都有权限的班组才能分配
     */
    private List<Long> calculateTeamIntersection(List<Long> inspectItemIds) {
        if (inspectItemIds.size() == 1) {
            // 如果只有一个检验项目，直接返回该项目的班组
            return taskMapper.selectTeamIdsByInspectItemIds(inspectItemIds);
        }

        // 获取第一个检验项目的班组作为初始交集
        List<Long> firstItemTeams = taskMapper.selectTeamIdsByInspectItemIds(
                Collections.singletonList(inspectItemIds.get(0)));

        if (CollectionUtils.isEmpty(firstItemTeams)) {
            return new ArrayList<>();
        }

        Set<Long> intersectionSet = new HashSet<>(firstItemTeams);

        // 依次与其他检验项目的班组求交集
        for (int i = 1; i < inspectItemIds.size(); i++) {
            List<Long> currentItemTeams = taskMapper.selectTeamIdsByInspectItemIds(
                    Collections.singletonList(inspectItemIds.get(i)));

            if (CollectionUtils.isEmpty(currentItemTeams)) {
                return new ArrayList<>(); // 如果任何一个项目没有班组，交集为空
            }

            intersectionSet.retainAll(currentItemTeams); // 求交集

            if (intersectionSet.isEmpty()) {
                break; // 如果交集已经为空，不需要继续计算
            }
        }

        return new ArrayList<>(intersectionSet);
    }

    /**
     * 根据 schemeSource 分支查询对应班组表中的班组ID列表
     */
    private List<Long> queryTeamIdsByConfig(TaskSchemeConfigDTO config) {
        if (InspectionOrderSourceEnum.STABILITY == config.getSchemeSource()) {
            return taskMapper.selectTeamIdsByInspectItemAndStabilitySchemeVersion(
                    config.getInspectItemId(), config.getSchemeVersionId());
        }
        return taskMapper.selectTeamIdsByInspectItemAndSchemeVersion(
                config.getInspectItemId(), config.getSchemeVersionId());
    }

    /**
     * 计算多个任务的班组交集（基于检验方案版本）
     * 只有在所有任务对应的检验项目在相应检验方案版本中都有权限的班组才能分配
     */
    private List<Long> calculateTeamIntersectionBySchemeConfig(List<TaskSchemeConfigDTO> taskSchemeConfigs) {
        if (taskSchemeConfigs.size() == 1) {
            // 如果只有一个任务，直接返回该任务对应的班组
            TaskSchemeConfigDTO config = taskSchemeConfigs.get(0);
            return queryTeamIdsByConfig(config);
        }

        // 获取第一个任务的班组作为初始交集
        TaskSchemeConfigDTO firstConfig = taskSchemeConfigs.get(0);
        List<Long> firstTaskTeams = queryTeamIdsByConfig(firstConfig);

        if (CollectionUtils.isEmpty(firstTaskTeams)) {
            return new ArrayList<>();
        }

        Set<Long> intersectionSet = new HashSet<>(firstTaskTeams);

        // 依次与其他任务的班组求交集
        for (int i = 1; i < taskSchemeConfigs.size(); i++) {
            TaskSchemeConfigDTO currentConfig = taskSchemeConfigs.get(i);
            List<Long> currentTaskTeams = queryTeamIdsByConfig(currentConfig);

            if (CollectionUtils.isEmpty(currentTaskTeams)) {
                return new ArrayList<>(); // 如果任何一个任务没有班组，交集为空
            }

            intersectionSet.retainAll(currentTaskTeams); // 求交集

            if (intersectionSet.isEmpty()) {
                return new ArrayList<>(); // 交集为空，提前退出
            }
        }

        return new ArrayList<>(intersectionSet);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TaskStatusEnum completeTaskForAppEln(Long taskId) {
        if (taskId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_EMPTY, "任务ID");
        }
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_NOT_FOUND);
        }
        // 权限校验：仅允许任务所有人操作
        String currentUserId = SysUserHolder.getUser().getUserId();
        if (task.getOwnerId() == null || !currentUserId.equals(task.getOwnerId())) {
            throw new BmosException(LimsResponseCode.PERMISSION_DENIED);
        }
        // 仅允许 ELN 任务
        if (ExecuteMethodEnum.ELN != task.getExecuteMethod()) {
            throw new BmosException(LimsResponseCode.APP_ELN_TASK_ONLY_ELN);
        }
        // 校验任务状态：待完成/进行中/样品审核不通过可提交
        if (!(TaskStatusEnum.PENDING_COMPLETION == task.getStatus()
                || TaskStatusEnum.IN_PROGRESS == task.getStatus()
                || TaskStatusEnum.SAMPLE_AUDIT_REJECTED == task.getStatus())) {
            throw new BmosException(LimsResponseCode.APP_ELN_TASK_STATUS_NOT_ALLOWED);
        }
        // 状态流转为待复核，并记录完成时间
        LocalDateTime now = LocalDateTime.now();
        TaskStatusEnum fromStatus = task.getStatus();
        LambdaUpdateWrapper<Task> update = new LambdaUpdateWrapper<>();
        update.eq(Task::getId, taskId)
                .set(Task::getStatus, TaskStatusEnum.TO_REVIEW)
                .set(Task::getCompleteTime, now)
                // APP完成任务时，同步记录检验时间为完成时刻；若已存在检验时间则不覆盖
                .set(task.getTestTime() == null, Task::getTestTime, now)
                .set(Task::getUpdateBy, currentUserId)
                .set(Task::getUpdateTime, now);
        taskMapper.update(null, update);
        // 记录状态变更历史
        recordStatusHistory(taskId, fromStatus, TaskStatusEnum.TO_REVIEW,
                TaskOperationTypeEnum.ENTRY_COMPLETE, "APP-ELN录入完成，提交待复核", now, SysUserHolder.getUser());
        // 返回任务状态
        return TaskStatusEnum.TO_REVIEW;
    }

    @Override
    public TaskQueryDTO setTaskPermission(Object reqVO) {
        TaskQueryDTO queryDTO = new TaskQueryDTO();
        BeanUtils.copyProperties(reqVO, queryDTO);

        // 设置当前用户的数据权限
        String currentUserId = SysUserHolder.getUser().getUserId();
        List<Long> userTeamIds = appTaskQueryService.queryUserInspectionTeamIds(currentUserId);
        queryDTO.setCurrentUserId(Long.valueOf(currentUserId));
        queryDTO.setCurrentUserTeamIds(userTeamIds);

        return queryDTO;
    }

    /**
     * 记录任务状态变更历史
     */
    private void recordStatusHistory(Long taskId, TaskStatusEnum fromStatus,
                                     TaskStatusEnum toStatus, TaskOperationTypeEnum operationType, String reason, LocalDateTime now, SysUser user) {
        TaskStatusHistory history = new TaskStatusHistory();
        history.setTaskId(taskId);
        history.setOperationType(operationType);
        history.setFromStatus(fromStatus==null?null:fromStatus.getValue());
        history.setToStatus(toStatus.getValue());
        history.setOperatorId(user.getUserId());
        history.setOperateTime(now);
        history.setCreateBy(user.getUserId());
        history.setUpdateBy(user.getUserId());
        history.setCreateTime( now);
        history.setUpdateTime( now);
        history.setComment(reason);

        taskStatusHistoryMapper.insert(history);
    }

    /**
     * 验证用户是否有权限操作指定的任务列表
     *
     * @param userId  用户ID
     * @param taskIds 任务ID列表
     * @throws BmosException 如果用户没有权限操作任何一个任务
     */
    private void validateUserTaskPermission(String userId, List<Long> taskIds) {
        if (userId == null || CollectionUtils.isEmpty(taskIds)) {
            return;
        }

        // 查询用户有权限操作的任务数量
        int authorizedTaskCount = taskMapper.countUserAuthorizedTasks(userId, taskIds);

        // 如果有权限的任务数量不等于总任务数量，说明存在无权限的任务
        if (authorizedTaskCount != taskIds.size()) {
            log.warn("用户{}没有权限操作部分任务，总任务数：{}，有权限任务数：{}",
                    userId, taskIds.size(), authorizedTaskCount);
            throw new BmosException(LimsResponseCode.PERMISSION_DENIED);
        }

        log.debug("用户{}权限验证通过，任务数量：{}", userId, taskIds.size());
    }

    /**
     * 为稳定性任务补全 schemeId 和 hasJudgmentCondition。
     * 稳定性检验单的 scheme_version_id 指向 lm_stability_scheme_version，
     * 常规 LEFT JOIN lm_inspection_scheme_version 返回 NULL，需在此处补全。
     */
    private void supplementStabilityTaskFields(List<TaskDTO> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return;
        }
        for (TaskDTO task : tasks) {
            if (task.getSchemeId() != null) {
                continue; // 常规任务已有 schemeId，无需处理
            }
            Long schemeVersionId = task.getSchemeVersionId();
            if (schemeVersionId == null) {
                continue;
            }
            // 尝试从稳定性方案版本表获取 schemeId
            StabilitySchemeVersion sv = stabilitySchemeVersionMapper.selectById(schemeVersionId);
            if (sv == null) {
                continue;
            }
            task.setSchemeId(sv.getSchemeId());
            // 补全 hasJudgmentCondition：检查稳定性判定表
            if (Boolean.FALSE.equals(task.getHasJudgmentCondition()) && task.getParameterConfigId() != null) {
                boolean hasStabilityJudgment = !stabilitySchemeJudgmentMapper
                        .listByParamConfigId(task.getParameterConfigId()).isEmpty();
                if (hasStabilityJudgment) {
                    task.setHasJudgmentCondition(true);
                }
            }
        }
    }

    /**
     * 为稳定性任务分组补全 hasJudgmentCondition。
     */
    private void supplementStabilityGroupFields(List<InspectionOrderTaskGroupDTO> groups) {
        if (groups == null || groups.isEmpty()) {
            return;
        }
        for (InspectionOrderTaskGroupDTO group : groups) {
            if (Boolean.TRUE.equals(group.getHasJudgmentCondition())) {
                continue; // 常规判定已命中，无需处理
            }
            Long schemeVersionId = group.getSchemeVersionId();
            Long parameterConfigId = group.getParameterConfigId();
            if (schemeVersionId == null || parameterConfigId == null) {
                continue;
            }
            // 只有稳定性方案版本才需要补全
            StabilitySchemeVersion sv = stabilitySchemeVersionMapper.selectById(schemeVersionId);
            if (sv == null) {
                continue;
            }
            boolean hasStabilityJudgment = !stabilitySchemeJudgmentMapper
                    .listByParamConfigId(parameterConfigId).isEmpty();
            if (hasStabilityJudgment) {
                group.setHasJudgmentCondition(true);
            }
        }
    }
}
