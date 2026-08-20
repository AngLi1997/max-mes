package com.bmos.lims2.server.inspect.entry.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
// import com.bmos.lims2.common.enums.EntryStatusEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.entry.dto.*;
import com.bmos.lims2.server.inspect.entry.entity.InspectionEntryHistory;
import com.bmos.lims2.server.inspect.entry.entity.InspectionEntryRecord;
import com.bmos.lims2.server.inspect.entry.mapper.InspectionEntryHistoryMapper;
import com.bmos.lims2.server.inspect.entry.mapper.InspectionEntryRecordMapper;
import com.bmos.lims2.server.inspect.entry.service.InspectionEntryService;
import com.bmos.lims2.server.inspect.entry.service.JudgmentExpressionService;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameterOption;
import com.bmos.lims2.server.task.entity.Task;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeJudgmentMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeParameterMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeJudgmentMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.lims2.common.enums.TaskOperationTypeEnum;
import com.bmos.lims2.server.task.entity.TaskStatusHistory;
import com.bmos.lims2.server.task.mapper.TaskStatusHistoryMapper;
import com.bmos.lims2.server.task.service.TaskService;
import com.bmos.lims2.server.inspect.entry.service.AppTaskQueryService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 检验录入服务实现类
 *
 * @author system
 * @since 2025/01/30
 */
@Service
@Slf4j
public class InspectionEntryServiceImpl implements InspectionEntryService {
    @Autowired
    private InspectionEntryRecordMapper inspectionEntryRecordMapper;

    @Autowired
    private InspectionEntryHistoryMapper inspectionEntryHistoryMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AppTaskQueryService appTaskQueryService;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private InspectionSchemeVersionMapper inspectionSchemeVersionMapper;

    @Autowired
    private JudgmentExpressionService judgmentExpressionService;

    @Autowired
    private TaskStatusHistoryMapper taskStatusHistoryMapper;

    @Autowired
    private com.bmos.lims2.server.eln.conclusion.service.ConclusionService conclusionService;

    @Autowired
    private InspectionSchemeJudgmentMapper inspectionSchemeJudgmentMapper;

    @Autowired
    private StabilitySchemeMapper stabilitySchemeMapper;

    @Autowired
    private StabilitySchemeParameterMapper stabilitySchemeParameterMapper;

    @Autowired
    private StabilitySchemeJudgmentMapper stabilitySchemeJudgmentMapper;

    @Autowired
    private com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    @Override
    public CommonPage<AnalysisItemEntryDTO> getAnalysisItemEntryPage(AnalysisItemEntryQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<AnalysisItemEntryDTO> list = inspectionEntryRecordMapper.selectAnalysisItemEntryList(queryDTO);

        // 为每个“分析项-方法版本”查询检验单任务分组（子列表）
        for (AnalysisItemEntryDTO analysisItem : list) {
            List<AnalysisItemEntryDTO.InspectionOrderEntryItemDTO> inspectionOrders =
                    inspectionEntryRecordMapper.selectInspectionOrdersByAnalysisItem(
                            analysisItem.getInspectItemId(),
                            analysisItem.getInspectParameterId(),
                            analysisItem.getRecordVersionId(),
                            queryDTO);

            // 为每个检验单任务查询关联的数据点
            java.util.Map<Long, Boolean> stabilityVersionCache1 = new java.util.HashMap<>();
            for (AnalysisItemEntryDTO.InspectionOrderEntryItemDTO inspectionOrder : inspectionOrders) {
                Long svId = inspectionOrder.getSchemeVersionId();
                boolean isStab = svId != null && stabilityVersionCache1.computeIfAbsent(svId,
                        id -> stabilitySchemeVersionMapper.selectById(id) != null);
                List<InspectionEntryRecordDTO> dataPoints = isStab
                        ? inspectionEntryRecordMapper.selectByTaskIdForStability(inspectionOrder.getId())
                        : inspectionEntryRecordMapper.selectByTaskId(inspectionOrder.getId());
                // 解析选项
                for (InspectionEntryRecordDTO dp : dataPoints) {
                    if (dp.getPointType() == com.bmos.lims2.common.enums.DataPointTypeEnum.OPTION
                            && dp.getOptions() == null && dp.getOptionsJson() != null) {
                        dp.setOptions(parseOptions(dp.getOptionsJson()));
                    }
                }
                inspectionOrder.setDataPoints(dataPoints);
                // 是否允许录入判定结论：当未配置判定条件时允许
                boolean hasConfig = hasJudgmentConfigured(dataPoints);
                inspectionOrder.setCanInputJudgment(!hasConfig);
            }

            analysisItem.setInspectionTasks(inspectionOrders);
        }

        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<com.bmos.lims2.server.inspect.entry.dto.AnalysisItemMethodGroupDTO> getAnalysisItemMethodEntryPage(AnalysisItemEntryQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<com.bmos.lims2.server.inspect.entry.dto.AnalysisItemMethodGroupDTO> list =
                inspectionEntryRecordMapper.selectAnalysisItemMethodEntryList(queryDTO);
        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<InspectionOrderEntryDTO> getInspectionOrderEntryPage(InspectionOrderEntryQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 1. 查询检验单录入列表（母列表）
        List<InspectionOrderEntryDTO> list = inspectionEntryRecordMapper.selectInspectionOrderEntryList(queryDTO);

        // 2. 为每个检验单查询对应的任务列表（子列表）
        for (InspectionOrderEntryDTO inspectionOrder : list) {
            List<InspectionOrderEntryDTO.AnalysisItemEntryItemDTO> analysisItems =
                    inspectionEntryRecordMapper.selectTasksByInspectionOrderPage(inspectionOrder.getInspectionOrderId(), queryDTO);

            // 3. 为每个任务查询关联的数据点
            java.util.Map<Long, Boolean> stabilityVersionCache2 = new java.util.HashMap<>();
            for (InspectionOrderEntryDTO.AnalysisItemEntryItemDTO analysisItem : analysisItems) {
                Long svId = analysisItem.getSchemeVersionId();
                boolean isStab = svId != null && stabilityVersionCache2.computeIfAbsent(svId,
                        id -> stabilitySchemeVersionMapper.selectById(id) != null);
                List<InspectionEntryRecordDTO> dataPoints = isStab
                        ? inspectionEntryRecordMapper.selectByTaskIdForStability(analysisItem.getId())
                        : inspectionEntryRecordMapper.selectByTaskId(analysisItem.getId());
                // 解析选项
                for (InspectionEntryRecordDTO dp : dataPoints) {
                    if (dp.getPointType() == com.bmos.lims2.common.enums.DataPointTypeEnum.OPTION
                            && dp.getOptions() == null && dp.getOptionsJson() != null) {
                        dp.setOptions(parseOptions(dp.getOptionsJson()));
                    }
                }
                analysisItem.setDataPoints(dataPoints);
                // 是否允许录入判定结论：当未配置判定条件时允许
                boolean hasConfig = hasJudgmentConfigured(dataPoints);
                analysisItem.setCanInputJudgment(!hasConfig);
            }

            inspectionOrder.setInspectionTasks(analysisItems);
        }

        return CommonPage.convertPage(list);
    }

    @Override
    public List<InspectionEntryRecordDTO> getEntryRecordsByTaskId(Long taskId) {
        if (taskId == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_NOT_FOUND);
        }
        Task task = taskMapper.selectById(taskId);
        if (task == null || task.getSchemeVersionId() == null) {
            return inspectionEntryRecordMapper.selectByTaskId(taskId);
        }
        boolean isStability = stabilitySchemeVersionMapper.selectById(task.getSchemeVersionId()) != null;
        return isStability
            ? inspectionEntryRecordMapper.selectByTaskIdForStability(taskId)
            : inspectionEntryRecordMapper.selectByTaskId(taskId);
    }

    @Override
    public List<InspectionEntryRecordDTO> getEntryRecordsByInspectionOrderIdAndParameterId(Long inspectionOrderId, Long parameterId) {
        if (inspectionOrderId == null || parameterId == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_ORDER_NOT_FOUND);
        }
        return inspectionEntryRecordMapper.selectByInspectionOrderIdAndParameterId(inspectionOrderId, parameterId);
    }

    @Override
    public List<InspectionEntryRecordDTO> getEntryRecordsBySchemeDataPointId(Long dataPointConfigId) {
        if (dataPointConfigId == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_DATAPOINT_NOT_FOUND);
        }
        return inspectionEntryRecordMapper.selectBySchemeDataPointId(dataPointConfigId);
    }

    @Override
    public List<InspectionEntryRecordDTO> getEntryRecordsBySchemeDataPointIdAndTaskId(Long dataPointConfigId, Long taskId) {
        if (dataPointConfigId == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_DATAPOINT_NOT_FOUND);
        }
        if (taskId == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_NOT_FOUND);
        }
        return inspectionEntryRecordMapper.selectBySchemeDataPointIdAndTaskId(dataPointConfigId, taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSaveEntryRecords(BatchEntryDTO batchEntryDTO) {
        return batchSaveEntryRecords(batchEntryDTO, false);
    }

    /**
     * 批量保存录入记录
     * @param batchEntryDTO 数据
     * @param skipStatusRecalc 是否跳过任务状态重算（ELN同步场景）
     */
    private int batchSaveEntryRecords(BatchEntryDTO batchEntryDTO, boolean skipStatusRecalc) {
        if (CollUtil.isEmpty(batchEntryDTO.getEntryItems())) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_DATA_NOT_EMPTY);
        }

        String currentUserId = SysUserHolder.getUser().getUserId();
        String currentUserName = SysUserHolder.getUser().getUserName();
        LocalDateTime now = LocalDateTime.now();

        int savedCount = 0;
        Set<Long> affectedTaskIds = new HashSet<>();

        for (BatchEntryDTO.EntryItemDTO item : batchEntryDTO.getEntryItems()) {
            // 验证数据有效性
            validateEntryItem(item);

            // 权限与状态校验
            validateEntryPermission(item);

            InspectionEntryRecord record;
            InspectionEntryRecord oldRecord = null;

            if (item.getId() != null) {
                // 更新现有记录
                record = inspectionEntryRecordMapper.selectById(item.getId());
                if (record == null) {
                    throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
                }
                oldRecord = copyRecord(record); // 保存旧值用于历史记录

                // 仅允许修改已录入的数据点（有历史值）
                if (!hasValueDTO(inspectionEntryRecordMapper.selectById(item.getId()))) {
                    throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
                }
                updateRecordFromItem(record, item, currentUserId, currentUserName);
                inspectionEntryRecordMapper.updateById(record);
                // 写入任务状态历史（数据点修改，不改变任务状态也记录）
                try {
                    TaskStatusHistory h = new TaskStatusHistory();
                    h.setTaskId(record.getTaskId());
                    h.setOperationType(TaskOperationTypeEnum.ENTRY_VALUE_MODIFY);
                    h.setFromStatus(null);
                    h.setToStatus(null);
                    h.setOperatorId(currentUserId);
                    h.setOperateTime(now);
                    java.util.Map<String, Object> detail = new java.util.HashMap<>();
                    detail.put("dataPointId", record.getDataPointId());
                    detail.put("dataPointName", record.getDataPointName());
                    detail.put("pointType", record.getPointType() == null ? null : record.getPointType().getValue());
                    detail.put("oldValue", DataPointTypeEnum.NUMBER == record.getPointType() ? oldRecord.getValueNumber() : oldRecord.getValueText());
                    detail.put("newValue", DataPointTypeEnum.NUMBER == record.getPointType() ? record.getValueNumber() : record.getValueText());
                    detail.put("remark", record.getRemark());
                    h.setDetail(JSON.toJSONString(detail));
                    taskStatusHistoryMapper.insert(h);
                } catch (Exception e) {
                    log.warn("record task status history failed on modify, taskId={}, recordId={}", record.getTaskId(), record.getId(), e);
                }
            } else {
                // 新增记录
                // 禁止在待复核状态新增录入
                Task task = taskMapper.selectById(item.getTaskId());
                if (task != null && TaskStatusEnum.TO_REVIEW.equals(task.getStatus())) {
                    throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
                }
                record = createRecordFromItem(item, currentUserId, currentUserName);
                inspectionEntryRecordMapper.insert(record);
                // 写入任务状态历史（数据点录入，不改变任务状态也记录）
                try {
                    TaskStatusHistory h = new TaskStatusHistory();
                    h.setTaskId(record.getTaskId());
                    h.setOperationType(TaskOperationTypeEnum.ENTRY_VALUE_CREATE);
                    h.setFromStatus(null);
                    h.setToStatus(null);
                    h.setOperatorId(currentUserId);
                    h.setOperateTime(now);
                    java.util.Map<String, Object> detail = new java.util.HashMap<>();
                    detail.put("dataPointId", record.getDataPointId());
                    detail.put("dataPointName", record.getDataPointName());
                    detail.put("pointType", record.getPointType() == null ? null : record.getPointType().getValue());
                    detail.put("newValueText", record.getValueText());
                    detail.put("newValueNumber", record.getValueNumber());
                    detail.put("remark", record.getRemark());
                    h.setDetail(JSON.toJSONString(detail));
                    taskStatusHistoryMapper.insert(h);
                } catch (Exception e) {
                    log.warn("record task status history failed on create, taskId={}, recordId={}", record.getTaskId(), record.getId(), e);
                }
            }

            // 记录历史
            if (oldRecord != null) {
                saveEntryHistory(record, oldRecord, "数据修改", currentUserId, currentUserName, now);
            }

            affectedTaskIds.add(item.getTaskId());
            savedCount++;
        }

        // 更新相关任务的录入状态和判定结果
        if (!skipStatusRecalc) {
            for (Long taskId : affectedTaskIds) {
                updateTaskJudgment(taskId, true);
                updateTaskStatus(taskId);
            }
        } else {
            // ELN 同步场景：不重算任务状态，但仍需生成判定结论
            for (Long taskId : affectedTaskIds) {
                updateTaskJudgment(taskId, true);
            }
        }

        return savedCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateTestTime(List<BatchTestTimeDTO> batchTestTimeDTO) {
        if (batchTestTimeDTO == null || CollUtil.isEmpty(batchTestTimeDTO)) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_EMPTY);
        }

        String currentUserId = SysUserHolder.getUser().getUserId();
        String currentUserName = SysUserHolder.getUser().getUserName();
        int updateCount = 0;

        // 批量更新任务的检验时间（每个任务可单独时间）
        for (BatchTestTimeDTO item : batchTestTimeDTO) {
            Long taskId = item.getId();
            LocalDateTime testTime = item.getTestTime();
            Task task = taskMapper.selectById(taskId);
            if (task != null) {
                // 仅任务所有人且任务可录入时允许修改检验时间
                if (!TaskStatusEnum.PENDING_COMPLETION.equals(task.getStatus())
                        && !TaskStatusEnum.IN_PROGRESS.equals(task.getStatus())
                        && !TaskStatusEnum.REVIEW_REJECTED.equals(task.getStatus()) && !TaskStatusEnum.TO_REVIEW.equals(task.getStatus())) {
                    throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_STATUS_NOT_MATCH);
                }
                if (task.getOwnerId() != null && !currentUserId.equals(task.getOwnerId())) {
                    throw new BmosException(LimsResponseCode.PERMISSION_DENIED);
                }
                // 记录旧检验时间用于历史
                LocalDateTime oldTestTime = task.getTestTime();
                task.setTestTime(testTime);
                taskMapper.updateById(task);
                updateCount++;
                // 更新相关任务的录入状态（不触发自动判定重算）
                updateTaskStatus(taskId);

                // 写入任务历史：设置检验时间（不改变状态也记录）
                try {
                    TaskStatusHistory h = new TaskStatusHistory();
                    h.setTaskId(taskId);
                    h.setOperationType(TaskOperationTypeEnum.TEST_TIME_SET);
                    h.setFromStatus(null);
                    h.setToStatus(null);
                    h.setOperatorId(currentUserId);
                    h.setOperateTime(LocalDateTime.now());
                    java.util.Map<String, Object> detail = new java.util.HashMap<>();
                    detail.put("oldTestTime", oldTestTime);
                    detail.put("newTestTime", testTime);
                    h.setDetail(com.alibaba.fastjson.JSON.toJSONString(detail));
                    taskStatusHistoryMapper.insert(h);
                } catch (Exception e) {
                    log.warn("record task status history failed on testTime set, taskId={}", taskId, e);
                }
            }
        }

        return updateCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateJudgment(java.util.List<com.bmos.lims2.server.inspect.entry.dto.BatchJudgmentDTO> list) {
        if (list == null || cn.hutool.core.collection.CollUtil.isEmpty(list)) {
            throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.DATA_ENTRY_TASK_EMPTY);
        }
        String currentUserId = com.bmos.common.holder.SysUserHolder.getUser().getUserId();
        String currentUserName = com.bmos.common.holder.SysUserHolder.getUser().getUserName();
        int updated = 0;
        for (com.bmos.lims2.server.inspect.entry.dto.BatchJudgmentDTO item : list) {
            Long taskId = item.getId();
            com.bmos.lims2.server.task.entity.Task task = taskMapper.selectById(taskId);
            if (task == null) {
                throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.DATA_ENTRY_TASK_NOT_FOUND);
            }
            // 状态与权限校验，参考批量设置检验时间
            if (!com.bmos.lims2.common.enums.TaskStatusEnum.PENDING_COMPLETION.equals(task.getStatus())
                    && !com.bmos.lims2.common.enums.TaskStatusEnum.IN_PROGRESS.equals(task.getStatus())
                    && !com.bmos.lims2.common.enums.TaskStatusEnum.TO_REVIEW.equals(task.getStatus())
                    && !com.bmos.lims2.common.enums.TaskStatusEnum.REVIEW_REJECTED.equals(task.getStatus())
                    && !com.bmos.lims2.common.enums.TaskStatusEnum.SAMPLE_AUDIT_REJECTED.equals(task.getStatus())
            ){
                throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.DATA_ENTRY_TASK_STATUS_NOT_MATCH);
            }
            if (task.getOwnerId() != null && !currentUserId.equals(task.getOwnerId())) {
                throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.PERMISSION_DENIED);
            }

            // 不允许在已配置判定条件时人工录入
            java.util.List<com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO> records = getEntryRecordsByTaskId(taskId);
            if (hasJudgmentConfigured(records)) {
                throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.PERMISSION_DENIED_AUTO_JUDGMENT);
            }

            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.LocalDateTime judgedTime = item.getJudgedTime() != null ? item.getJudgedTime() : now;

            com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<com.bmos.lims2.server.task.entity.Task> uw = new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
            uw.eq(com.bmos.lims2.server.task.entity.Task::getId, taskId)
              .set(com.bmos.lims2.server.task.entity.Task::getJudgedResult, item.getJudgedResult())
              .set(com.bmos.lims2.server.task.entity.Task::getJudgedAbnormal, item.getJudgedResult() == null ? null : !item.getJudgedResult())
              .set(com.bmos.lims2.server.task.entity.Task::getJudgedTime, judgedTime)
              .set(com.bmos.lims2.server.task.entity.Task::getUpdateBy, currentUserId)
              .set(com.bmos.lims2.server.task.entity.Task::getUpdateTime, now);
            // 记录旧结论用于历史
            Boolean oldJudgedResult = task.getJudgedResult();
            Boolean oldJudgedAbnormal = task.getJudgedAbnormal();
            java.time.LocalDateTime oldJudgedTime = task.getJudgedTime();
            taskMapper.update(null, uw);

            // 触发状态重算（不触发自动判定，避免覆盖人工结论）
            updateTaskStatus(taskId);
            updated++;

            // 写入任务历史：设置判定结论（不改变状态也记录）
            try {
                com.bmos.lims2.server.task.entity.TaskStatusHistory h = new com.bmos.lims2.server.task.entity.TaskStatusHistory();
                h.setTaskId(taskId);
                h.setOperationType(com.bmos.lims2.common.enums.TaskOperationTypeEnum.JUDGMENT_SET);
                h.setFromStatus(null);
                h.setToStatus(null);
                h.setOperatorId(currentUserId);
                h.setOperateTime(now);
                java.util.Map<String, Object> detail = new java.util.HashMap<>();
                detail.put("oldJudgedResult", oldJudgedResult);
                detail.put("oldJudgedAbnormal", oldJudgedAbnormal);
                detail.put("oldJudgedTime", oldJudgedTime);
                detail.put("newJudgedResult", item.getJudgedResult());
                detail.put("newJudgedAbnormal", item.getJudgedResult() == null ? null : !item.getJudgedResult());
                detail.put("newJudgedTime", judgedTime);
                h.setDetail(com.alibaba.fastjson.JSON.toJSONString(detail));
                taskStatusHistoryMapper.insert(h);
            } catch (Exception e) {
                log.warn("record task status history failed on judgment set, taskId={}", taskId, e);
            }
        }
        return updated;
    }

    @Override
    public Long countIncompleteAnalysisItems(AnalysisItemEntryQueryDTO queryDTO) {
        return inspectionEntryRecordMapper.countIncompleteAnalysisItems(queryDTO);
    }

    @Override
    public Long countIncompleteInspectionOrders(InspectionOrderEntryQueryDTO queryDTO) {
        return inspectionEntryRecordMapper.countIncompleteInspectionOrders(queryDTO);
    }

    @Override
    public Long countAbnormalAnalysisItems(AnalysisItemEntryQueryDTO queryDTO) {
        return inspectionEntryRecordMapper.countAbnormalAnalysisItems(queryDTO);
    }

    @Override
    public Long countAbnormalInspectionOrders(InspectionOrderEntryQueryDTO queryDTO) {
        return inspectionEntryRecordMapper.countAbnormalInspectionOrders(queryDTO);
    }

    @Override
    public EntryStatsDTO getAnalysisItemStats(AnalysisItemEntryQueryDTO queryDTO) {
        EntryStatsDTO stats = inspectionEntryRecordMapper.analysisItemEntryStats(queryDTO);
        if (stats == null) {
            stats = new EntryStatsDTO();
            stats.setAllCount(0L);
            stats.setIncompleteCount(0L);
            stats.setAbnormalCount(0L);
        }
        return stats;
    }

    @Override
    public EntryStatsDTO getInspectionOrderStats(InspectionOrderEntryQueryDTO queryDTO) {
        EntryStatsDTO stats = inspectionEntryRecordMapper.inspectionOrderEntryStats(queryDTO);
        if (stats == null) {
            stats = new EntryStatsDTO();
            stats.setAllCount(0L);
            stats.setIncompleteCount(0L);
            stats.setAbnormalCount(0L);
        }
        return stats;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskEntryStatusAndJudgment(Long taskId) {
        updateTaskJudgment(taskId, true);
        updateTaskStatus(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskJudgment(Long taskId, boolean recalcJudgment) {
        List<InspectionEntryRecordDTO> records = getEntryRecordsByTaskId(taskId);
        updateTaskJudgment(taskId, records, recalcJudgment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskStatus(Long taskId) {
        List<InspectionEntryRecordDTO> records = getEntryRecordsByTaskId(taskId);
        updateTaskStatus(taskId, records);
    }

    /**
     * 更新任务录入状态（不含结论重算）
     * @param taskId 任务ID
     * @param records 任务下的数据点
     */
    private void updateTaskStatus(Long taskId, List<InspectionEntryRecordDTO> records) {
        if (taskId == null) {
            return;
        }
        if (CollUtil.isEmpty(records)) {
            return;
        }

        long enteredCount = records.stream().filter(this::hasValue).count();

        // 读取最新任务，用于状态计算
        Task current = taskMapper.selectById(taskId);
        if (current == null) {
            return;
        }
        TaskStatusEnum oldStatus = current.getStatus();

        // 计算统一任务状态：
        // - 待完成=待录入（均无值）
        // - 待复核=所有数据点有值 且 已设置检验时间 且 判定结果已存在（自动或人工）
        // - 其他情况为进行中
        TaskStatusEnum computedStatus;
        boolean allPointsCompleted = enteredCount == records.size();
        boolean testTimeExists = current.getTestTime() != null;
        boolean judgmentPresent = current.getJudgedResult() != null;
        if (allPointsCompleted && testTimeExists && judgmentPresent) {
            computedStatus = TaskStatusEnum.TO_REVIEW;
        } else {
            computedStatus = TaskStatusEnum.IN_PROGRESS;
        }

        LocalDateTime now = LocalDateTime.now();
        LambdaUpdateWrapper<Task> uw = new LambdaUpdateWrapper<>();
        uw.eq(Task::getId, taskId)
                .set(Task::getStatus, computedStatus)
                .set(Task::getUpdateBy, SysUserHolder.getUser().getUserId())
                .set(Task::getUpdateTime, now);
        taskMapper.update(null, uw);

        // 写入任务状态变更到状态历史表（仅在状态发生变化时）
        if (oldStatus != computedStatus) {
            com.bmos.lims2.common.enums.TaskOperationTypeEnum op;
            if (oldStatus == com.bmos.lims2.common.enums.TaskStatusEnum.PENDING_COMPLETION && computedStatus == com.bmos.lims2.common.enums.TaskStatusEnum.IN_PROGRESS) {
                op = com.bmos.lims2.common.enums.TaskOperationTypeEnum.ENTRY_START;
            } else if (computedStatus == com.bmos.lims2.common.enums.TaskStatusEnum.TO_REVIEW) {
                op = com.bmos.lims2.common.enums.TaskOperationTypeEnum.ENTRY_COMPLETE;
            } else {
                op = com.bmos.lims2.common.enums.TaskOperationTypeEnum.ENTRY_STATUS_CHANGE;
            }
            TaskStatusHistory h = new TaskStatusHistory();
            h.setTaskId(taskId);
            h.setOperationType(op);
            // 记录统一任务状态值
            h.setFromStatus(oldStatus == null ? null : oldStatus.getValue());
            h.setToStatus(computedStatus.getValue());
            h.setOperatorId(SysUserHolder.getUser().getUserId());
            h.setOperateTime(now);
            h.setComment(op == com.bmos.lims2.common.enums.TaskOperationTypeEnum.ENTRY_STATUS_CHANGE ?
                    "任务状态: " + oldStatus + " -> " + computedStatus + "（自动重算）" :
                    (op == com.bmos.lims2.common.enums.TaskOperationTypeEnum.ENTRY_START ? "开始录入" : "完成录入"));
            taskStatusHistoryMapper.insert(h);
        }
    }

    /**
     * 独立处理判定结论的更新与同步
     * @param taskId 任务ID
     * @param records 数据点列表
     * @param recalcJudgment 是否触发自动判定
     * @return 新生成的判定结果（若未生成则为null）
     */
    private Boolean updateTaskJudgment(Long taskId, List<InspectionEntryRecordDTO> records, boolean recalcJudgment) {
        if (taskId == null || CollUtil.isEmpty(records)) {
            return null;
        }

        if (!recalcJudgment || !hasJudgmentConfigured(records) || !isJudgmentRequiredDataComplete(records)) {
            return null;
        }

        Boolean judgedResult = calculateJudgmentResult(taskId, records);
        if (judgedResult == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        LambdaUpdateWrapper<Task> uw = new LambdaUpdateWrapper<>();
        uw.eq(Task::getId, taskId)
                .set(Task::getJudgedResult, judgedResult)
                .set(Task::getJudgedAbnormal, !judgedResult)
                .set(Task::getJudgedTime, now)
                .set(Task::getUpdateBy, SysUserHolder.getUser().getUserId())
                .set(Task::getUpdateTime, now);
        taskMapper.update(null, uw);

        // 若为 ELN 任务，仍需同步结论组件
        try {
            Task fresh = taskMapper.selectById(taskId);
            if (fresh != null && fresh.getExecuteMethod() == com.bmos.lims2.common.enums.ExecuteMethodEnum.ELN) {
                conclusionService.syncAutoConclusionForEln(taskId, judgedResult);
            }
        } catch (Exception e) {
            log.warn("sync eln conclusion component failed, taskId={}", taskId, e);
        }
        return judgedResult;
    }

    @Override
    public CommonPage<DataPointQueryRespDTO.DataRow> queryDataPointRows(DataPointQueryPageReqDTO reqDTO) {
        // 1) 分页查询满足条件的检验单（按请验时间倒序）
        PageHelper.startPage(reqDTO.getPageNum(), reqDTO.getPageSize());
        List<com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionDTO> orders;
        if (reqDTO.getSchemeId() != null) {
            boolean isStability = stabilitySchemeMapper.selectById(reqDTO.getSchemeId()) != null;
            if (isStability) {
                orders = inspectionEntryRecordMapper.pageOrderBaseForDataPointByStabilityScheme(
                        reqDTO.getMaterialId(),
                        reqDTO.getSchemeId(),
                        reqDTO.getRequestStartTime(),
                        reqDTO.getRequestEndTime(),
                        reqDTO.getInspectionOrderIds());
            } else {
                orders = inspectionEntryRecordMapper.pageOrderBaseForDataPointByScheme(
                        reqDTO.getMaterialId(),
                        reqDTO.getSchemeId(),
                        reqDTO.getRequestStartTime(),
                        reqDTO.getRequestEndTime(),
                        reqDTO.getInspectionOrderIds());
            }
        } else {
            orders = inspectionEntryRecordMapper.pageOrderBaseForDataPoint(
                    reqDTO.getMaterialId(),
                    null,
                    reqDTO.getRequestStartTime(),
                    reqDTO.getRequestEndTime(),
                    reqDTO.getInspectionOrderIds());
        }

        CommonPage<com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionDTO> orderPage = CommonPage.convertPage(orders);
        if (CollUtil.isEmpty(orderPage.getList())) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, reqDTO);
        }

        List<Long> orderIds = new ArrayList<>();
        for (com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionDTO o : orderPage.getList()) {
            orderIds.add(o.getId());
        }

        // 2) 批量查询这些检验单的数据点值（同一个点取最新，SQL已按时间倒序）
        List<InspectionEntryRecordDTO> records;
        if (reqDTO.getSchemeId() != null) {
            boolean isStability = stabilitySchemeMapper.selectById(reqDTO.getSchemeId()) != null;
            records = isStability
                ? inspectionEntryRecordMapper.listEntryValuesByOrderIdsByStabilityScheme(orderIds, reqDTO.getSchemeId())
                : inspectionEntryRecordMapper.listEntryValuesByOrderIdsByScheme(orderIds, reqDTO.getSchemeId());
        } else {
            records = inspectionEntryRecordMapper.listEntryValuesByOrderIds(orderIds, null);
        }

        // 3) 组装每行（一个检验单一行）
        // 平铺结构：Map<orderId, Map<pointKey=name|type, DataPointValueDTO>>，只放第一条即最新值
        java.util.Map<Long, java.util.Map<String, DataPointQueryRespDTO.DataPointValueDTO>> orderIdToPointMap = new java.util.HashMap<>();
        // 分组结构：Map<orderId, Map<parameterId, Map<pointName, value>>>
        java.util.Map<Long, java.util.Map<Long, java.util.Map<String, String>>> orderIdToParamPointMap = new java.util.HashMap<>();
        for (InspectionEntryRecordDTO r : records) {
            // 平铺
            java.util.Map<String, DataPointQueryRespDTO.DataPointValueDTO> flat = orderIdToPointMap
                    .computeIfAbsent(r.getInspectionOrderId(), k -> new java.util.HashMap<>());
            String pointType = r.getPointType() == null ? "" : r.getPointType().getValue();
            String pointKey = r.getDataPointName() + "|" + pointType;
            if (!flat.containsKey(pointKey)) {
                String v = r.getValueText();
                if (v == null && r.getValueNumber() != null) {
                    v = r.getValueNumber();
                }
                DataPointQueryRespDTO.DataPointValueDTO dv = new DataPointQueryRespDTO.DataPointValueDTO();
                dv.setValue(v);
                dv.setParameterId(r.getParameterId());
                dv.setParameterCode(r.getParameterCode());
                flat.put(pointKey, dv);
            }

            // 分组（按分析项）
            if (r.getParameterId() != null) {
                java.util.Map<Long, java.util.Map<String, String>> p2map = orderIdToParamPointMap.computeIfAbsent(r.getInspectionOrderId(), k -> new java.util.HashMap<>());
                java.util.Map<String, String> name2v = p2map.computeIfAbsent(r.getParameterId(), k -> new java.util.HashMap<>());
                if (!name2v.containsKey(r.getDataPointName())) {
                    String v2 = r.getValueText();
                    if (v2 == null && r.getValueNumber() != null) {
                        v2 = r.getValueNumber();
                    }
                    name2v.put(r.getDataPointName(), v2);
                }
            }
        }

        List<DataPointQueryRespDTO.DataRow> rows = new ArrayList<>();
        for (com.bmos.lims2.server.inspect.order.dto.InspectionOrderOptionDTO o : orderPage.getList()) {
            DataPointQueryRespDTO.DataRow row = new DataPointQueryRespDTO.DataRow();
            row.setInspectionOrderId(o.getId());
            row.setInspectionOrderNo(o.getOrderNo());
            row.setRequestTime(o.getRequestTime());
            row.setPointNameToValue(orderIdToPointMap.getOrDefault(o.getId(), new java.util.HashMap<>()));
            row.setParameterToPointValues(orderIdToParamPointMap.getOrDefault(o.getId(), new java.util.HashMap<>()));
            rows.add(row);
        }

        CommonPage<DataPointQueryRespDTO.DataRow> resp = new CommonPage<>();
        resp.setPageNum(orderPage.getPageNum());
        resp.setPageSize(orderPage.getPageSize());
        resp.setTotal(orderPage.getTotal());
        resp.setList(rows);
        return resp;
    }

    @Override
    public DataPointQueryRespDTO loadHeaders(DataPointQueryPageReqDTO reqDTO) {
        DataPointQueryRespDTO resp = new DataPointQueryRespDTO();
        if (reqDTO.getSchemeId() != null) {
            // 判断是稳定性方案还是常规方案
            boolean isStability = stabilitySchemeMapper.selectById(reqDTO.getSchemeId()) != null;
            List<com.bmos.lims2.server.inspect.entry.dto.HeaderGroupRowDTO> rows;
            if (isStability) {
                rows = stabilitySchemeParameterMapper.listHeaderGroupsByStabilityScheme(reqDTO.getSchemeId());
            } else {
                rows = inspectionEntryRecordMapper.listHeaderGroupsByScheme(reqDTO.getSchemeId());
            }
            // 组装分组表头，便于前端展示一二级表头
            java.util.Map<Long, DataPointQueryRespDTO.HeaderGroup> groupMap = new java.util.LinkedHashMap<>();
            for (com.bmos.lims2.server.inspect.entry.dto.HeaderGroupRowDTO r : rows) {
                Long pid = r.getParameterId();
                DataPointQueryRespDTO.HeaderGroup g = groupMap.computeIfAbsent(pid, k -> {
                    DataPointQueryRespDTO.HeaderGroup ng = new DataPointQueryRespDTO.HeaderGroup();
                    ng.setParameterId(pid);
                    ng.setParameterCode(r.getParameterCode());
                    ng.setParameterName(r.getParameterName());
                    ng.setDataPoints(new java.util.ArrayList<>());
                    return ng;
                });
                DataPointQueryRespDTO.HeaderLevel2 l2 = new DataPointQueryRespDTO.HeaderLevel2();
                l2.setPointName(r.getPointName());
                l2.setPointType(r.getPointType());
                g.getDataPoints().add(l2);
            }
            resp.setHeaderGroups(new java.util.ArrayList<>(groupMap.values()));
        }
        return resp;
    }


    /**
     * 验证录入项数据
     */
    private void validateEntryItem(BatchEntryDTO.EntryItemDTO item) {
        if (item.getTaskId() == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_EMPTY);
        }
        if (item.getDataPointConfigId() == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_DATAPOINT_NOT_FOUND);
        }
        if (item.getPointType() == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_DATAPOINT_TYPE_EMPTY);
        }
        if (StrUtil.isBlank(item.getDataPointName())) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_DATAPOINT_DATAPOINT_NAME_EMPTY);
        }

        // 任务与检验单、方案状态校验
        Task task = taskMapper.selectById(item.getTaskId());
        if (task == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_NOT_FOUND);
        }
        if (task.getStatus() == null || (task.getStatus() != com.bmos.lims2.common.enums.TaskStatusEnum.PENDING_COMPLETION
                && task.getStatus() != TaskStatusEnum.IN_PROGRESS
                && task.getStatus() != TaskStatusEnum.REVIEW_REJECTED
                && task.getStatus() != TaskStatusEnum.TO_REVIEW
                && task.getStatus() != TaskStatusEnum.SAMPLE_AUDIT_REJECTED)) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_STATUS_NOT_MATCH);
        }

        InspectionOrder order = inspectionOrderMapper.selectById(task.getInspectionOrderId());
        if (order == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_ORDER_NOT_FOUND);
        }
        if (order.getOrderStatus() == null || order.getOrderStatus() != com.bmos.lims2.common.enums.InspectionOrderStatusEnum.CONFIRMED && order.getOrderStatus() != com.bmos.lims2.common.enums.InspectionOrderStatusEnum.SAMPLE_AUDIT_REJECTED) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_ORDER_STATUS_NOT_MATCH);
        }

        // 数据点配置合法性校验：必须属于该任务的方案版本与分析项
        // 通过基于selectByTaskId的联合校验：由SQL selectByTaskId已关联sdp，验证ID是否存在
        // 快速校验：从selectByTaskId结果中匹配传入的dataPointConfigId
        boolean exists = false;
        for (InspectionEntryRecordDTO cfg : getEntryRecordsByTaskId(item.getTaskId())) {
            if (cfg.getDataPointConfigId() != null && cfg.getDataPointConfigId().equals(item.getDataPointConfigId())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_DATAPOINT_NOT_FOUND);
        }
    }

    /**
     * 从录入项创建记录
     */
    private InspectionEntryRecord createRecordFromItem(BatchEntryDTO.EntryItemDTO item,
                                                       String currentUserId, String currentUserName) {
        // 查询任务信息获取冗余字段
        Task task = taskMapper.selectById(item.getTaskId());
        if (task == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_NOT_FOUND);
        }
        // 查询检验单与方案版本，补齐冗余信息
        InspectionOrder order = inspectionOrderMapper.selectById(task.getInspectionOrderId());
        Long schemeVersionId = task.getSchemeVersionId();
        InspectionSchemeVersion schemeVersion = null;
        if (schemeVersionId == null && order != null) {
            schemeVersionId = order.getSchemeVersionId();
        }
        Long resolvedSchemeId = null;
        if (schemeVersionId != null) {
            schemeVersion = inspectionSchemeVersionMapper.selectById(schemeVersionId);
            if (schemeVersion != null) {
                resolvedSchemeId = schemeVersion.getSchemeId();
            } else {
                // 稳定性任务：scheme_version_id 指向 lm_stability_scheme_version
                com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion ssv =
                        stabilitySchemeVersionMapper.selectById(schemeVersionId);
                if (ssv != null) {
                    resolvedSchemeId = ssv.getSchemeId();
                }
            }
        }

        InspectionEntryRecord record = new InspectionEntryRecord();
        record.setInspectionOrderId(task.getInspectionOrderId());
        if (order != null) {
            record.setInspectionOrderNo(order.getOrderNo());
        }
        record.setTaskId(item.getTaskId());
        record.setInspectItemId(task.getInspectItemId());
        record.setInspectItemCode(task.getInspectItemCode());
        record.setParameterId(task.getParameterId());
        record.setParameterCode(task.getParameterCode());
        record.setDataPointConfigId(item.getDataPointConfigId());
        record.setPackageId(item.getPackageId());
        record.setItemConfigId(item.getItemConfigId());
        record.setParameterConfigId(item.getParameterConfigId());
        record.setDataPointId(item.getDataPointId());
        record.setDataPointName(item.getDataPointName());
        // 方案冗余字段
        record.setSchemeVersionId(schemeVersionId);
        if (resolvedSchemeId != null) {
            record.setSchemeId(resolvedSchemeId);
        }
        record.setPointType(item.getPointType());
        // 数值型：仅写入 valueNumber（字符串），文本型/选项型：仅写入 valueText
        if (item.getPointType() == com.bmos.lims2.common.enums.DataPointTypeEnum.NUMBER) {
            String v = item.getValueNumber() != null ? item.getValueNumber() : item.getValueText();
            record.setValueNumber(v);
            record.setValueText(null);
        } else if (item.getPointType() == com.bmos.lims2.common.enums.DataPointTypeEnum.TIME) {
            // TIME 类型：valueText 为格式化显示，valueNumber 为按舍入规则换算后的秒值
            record.setValueText(item.getValueText());
            record.setValueNumber(item.getValueNumber());
        } else {
            record.setValueText(item.getValueText());
            record.setValueNumber(null);
        }
        record.setTestTime(item.getTestTime());
        record.setOperatorId(currentUserId);
        record.setOperatorName(currentUserName);
        record.setRemark(item.getRemark());
        record.setAbnormal(false);

        return record;
    }

    /**
     * 从录入项更新记录
     */
    private void updateRecordFromItem(InspectionEntryRecord record, BatchEntryDTO.EntryItemDTO item,
                                      String currentUserId, String currentUserName) {
        // 数值型：仅写入 valueNumber（字符串），文本型/选项型：仅写入 valueText
        if (item.getPointType() == com.bmos.lims2.common.enums.DataPointTypeEnum.NUMBER) {
            String v = item.getValueNumber() != null ? item.getValueNumber() : item.getValueText();
            record.setValueNumber(v);
            record.setValueText(null);
        } else if (item.getPointType() == com.bmos.lims2.common.enums.DataPointTypeEnum.TIME) {
            record.setValueText(item.getValueText());
            record.setValueNumber(item.getValueNumber());
        } else {
            record.setValueText(item.getValueText());
            record.setValueNumber(null);
        }
        record.setTestTime(item.getTestTime());
        record.setOperatorId(currentUserId);
        record.setOperatorName(currentUserName);
        record.setRemark(item.getRemark());
    }

    /**
     * 复制记录用于历史记录
     */
    private InspectionEntryRecord copyRecord(InspectionEntryRecord record) {
        InspectionEntryRecord copy = new InspectionEntryRecord();
        copy.setId(record.getId());
        copy.setValueText(record.getValueText());
        copy.setValueNumber(record.getValueNumber());
        copy.setTestTime(record.getTestTime());
        copy.setOperatorId(record.getOperatorId());
        copy.setOperatorName(record.getOperatorName());
        copy.setRemark(record.getRemark());
        return copy;
    }

    /**
     * 保存录入历史
     */
    private void saveEntryHistory(InspectionEntryRecord newRecord, InspectionEntryRecord oldRecord,
                                  String changeReason, String operatorId, String operatorName,
                                  LocalDateTime operateTime) {
        InspectionEntryHistory history = new InspectionEntryHistory();
        history.setEntryRecordId(newRecord.getId());
        history.setTaskId(newRecord.getTaskId());
        history.setDataPointId(newRecord.getDataPointId());
        history.setDataPointConfigId(newRecord.getDataPointConfigId());
        // 冗余字段补齐
        history.setInspectionOrderId(newRecord.getInspectionOrderId());
        history.setInspectionOrderNo(newRecord.getInspectionOrderNo());
        history.setSchemeId(newRecord.getSchemeId());
        history.setSchemeVersionId(newRecord.getSchemeVersionId());
        history.setPackageId(newRecord.getPackageId());
        history.setItemConfigId(newRecord.getItemConfigId());
        history.setParameterConfigId(newRecord.getParameterConfigId());
        history.setInspectItemId(newRecord.getInspectItemId());
        history.setInspectItemCode(newRecord.getInspectItemCode());
        history.setParameterId(newRecord.getParameterId());
        history.setParameterCode(newRecord.getParameterCode());
        history.setDataPointName(newRecord.getDataPointName());
        history.setPointType(newRecord.getPointType());
        history.setOldValueText(oldRecord.getValueText());
        history.setOldValueNumber(oldRecord.getValueNumber());
        history.setNewValueText(newRecord.getValueText());
        history.setNewValueNumber(newRecord.getValueNumber());
        history.setChangeReason(changeReason);
        history.setOperatorId(operatorId);
        history.setOperatorName(operatorName);
        history.setOperateTime(operateTime);

        inspectionEntryHistoryMapper.insert(history);
    }

    /**
     * 判断记录是否有值
     */
    private boolean hasValue(InspectionEntryRecordDTO record) {
        return (record.getValueNumber() != null) || StrUtil.isNotBlank(record.getValueText());
    }

    /**
     * 判断数据库记录是否有值（用于修改校验）
     */
    private boolean hasValueDTO(InspectionEntryRecord record) {
        return (record.getValueNumber() != null) || StrUtil.isNotBlank(record.getValueText());
    }

    /**
     * 计算判定结果
     */
    private Boolean calculateJudgmentResult(Long taskId, List<InspectionEntryRecordDTO> records) {
        return judgmentExpressionService.evaluateJudgmentExpression(taskId, records);
    }

    /**
     * 是否已配置判定条件（用于决定是否执行后端自动判定）
     * 判定依据：
     * 1) 存在最终判定表达式；或
     * 2) 该分析项配置下存在任意判定规则
     */
    private boolean hasJudgmentConfigured(List<InspectionEntryRecordDTO> records) {
        if (records == null || records.isEmpty()) {
            return false;
        }
        InspectionEntryRecordDTO first = records.get(0);
        // 1) 最终表达式存在
        String finalExpression = judgmentExpressionService.getJudgmentExpression(first.getParameterId(), first.getSchemeVersionId());
        if (cn.hutool.core.util.StrUtil.isNotBlank(finalExpression)) {
            return true;
        }
        // 2) 该分析项配置下存在任意判定规则
        if (first.getParameterConfigId() != null) {
            java.util.List<com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO> list =
                    inspectionSchemeJudgmentMapper.listByParameterConfigId(first.getParameterConfigId());
            if (list != null && !list.isEmpty()) {
                return true;
            }
            // 稳定性任务兜底
            java.util.List<com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeJudgmentDTO> sList =
                    stabilitySchemeJudgmentMapper.listByParamConfigId(first.getParameterConfigId());
            return sList != null && !sList.isEmpty();
        }
        return false;
    }

    /**
     * 判定所需数据点是否已全部录入
     * 规则：
     * - 读取最终判定表达式；
     * - 找出该分析项下所有判定规则中被最终表达式引用到的规则；
     * - 对这些被引用的规则，检查其绑定的数据点是否均有值（按 dataPointConfigId 优先，其次 dataPointId 兜底）。
     */
    private boolean isJudgmentRequiredDataComplete(List<InspectionEntryRecordDTO> records) {
        if (records == null || records.isEmpty()) {
            return false;
        }
        InspectionEntryRecordDTO first = records.get(0);
        String finalExpression = judgmentExpressionService.getJudgmentExpression(first.getParameterId(), first.getSchemeVersionId());
        if (StrUtil.isBlank(finalExpression) || first.getParameterConfigId() == null) {
            return false;
        }

        // 构建数据点映射
        Map<Long, InspectionEntryRecordDTO> configIdToRecord = new HashMap<>();
        Map<Long, InspectionEntryRecordDTO> pointIdToRecord = new HashMap<>();
        for (InspectionEntryRecordDTO r : records) {
            if (r.getDataPointConfigId() != null) {
                configIdToRecord.put(r.getDataPointConfigId(), r);
            }
            if (r.getDataPointId() != null) {
                pointIdToRecord.put(r.getDataPointId(), r);
            }
        }

        // 仅校验被最终表达式实际引用的规则
        List<com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO> judgments =
                inspectionSchemeJudgmentMapper.listByParameterConfigId(first.getParameterConfigId());
        if (judgments == null || judgments.isEmpty()) {
            // 稳定性任务兜底：转换为相同接口类型
            List<com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeJudgmentDTO> sj =
                    stabilitySchemeJudgmentMapper.listByParamConfigId(first.getParameterConfigId());
            if (sj != null && !sj.isEmpty()) {
                judgments = new java.util.ArrayList<>();
                for (com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeJudgmentDTO s : sj) {
                    com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO dto =
                            new com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO();
                    dto.setParameterConfigId(s.getParameterConfigId());
                    dto.setDataPointConfigId(s.getDataPointConfigId());
                    dto.setDataPointId(s.getDataPointId());
                    dto.setExpression(s.getExpression());
                    judgments.add(dto);
                }
            }
        }
        if (judgments == null || judgments.isEmpty()) {
            return false;
        }

        boolean anyReferenced = false;
        for (com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO j : judgments) {
            String var = j.getExpression();
            if (StrUtil.isBlank(var)) {
                continue;
            }
            if (!finalExpression.contains(var)) {
                continue; // 未被最终表达式引用的规则不纳入“所需数据点”
            }
            anyReferenced = true;

            InspectionEntryRecordDTO rec = null;
            if (j.getDataPointConfigId() != null) {
                rec = configIdToRecord.get(j.getDataPointConfigId());
            }
            if (rec == null && j.getDataPointId() != null) {
                rec = pointIdToRecord.get(j.getDataPointId());
            }
            if (rec == null || !hasValue(rec)) {
                return false; // 任一被引用数据点缺失或无值，视为未就绪
            }
        }
        return anyReferenced; // 至少存在一个被引用规则，且全部对应数据点有值
    }

    /**
     * 解析方案数据点的选项JSON为字符串列表
     */
    private List<String> parseOptions(String optionsJson) {
        try {
            if (cn.hutool.core.util.StrUtil.isBlank(optionsJson)) {
                return java.util.Collections.emptyList();
            }
            return JSON.parseArray(optionsJson, InspectParameterOption.class).stream().map(InspectParameterOption::getOptionValue).collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Failed to parse optionsJson: {}", optionsJson, e);
            return java.util.Collections.emptyList();
        }
    }

    /**
     * 录入权限与状态校验
     * 规则：
     * - 任务状态必须为待完成；不可为已终止/已完成
     * - 当前用户必须为任务所有人（已分配/领取）
     * - 在待复核状态不可新增，仅允许修改
     */
    private void validateEntryPermission(BatchEntryDTO.EntryItemDTO item) {
        Task task = taskMapper.selectById(item.getTaskId());
        if (task == null) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_TASK_NOT_FOUND);
        }
        String currentUserId = SysUserHolder.getUser().getUserId();
        if (task.getOwnerId() != null && !currentUserId.equals(task.getOwnerId())) {
            throw new BmosException(LimsResponseCode.PERMISSION_DENIED);
        }
        // 新增校验在创建时再次判断，这里仅在id为空时提前拦截
        if (item.getId() == null && TaskStatusEnum.TO_REVIEW.equals(task.getStatus())) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }
    }

    @Override
    public AnalysisItemEntryQueryDTO setAnalysisItemEntryPermission(Object reqVO) {
        AnalysisItemEntryQueryDTO queryDTO = new AnalysisItemEntryQueryDTO();
        BeanUtils.copyProperties(reqVO, queryDTO);

        // 设置当前用户的数据权限
        String currentUserId = SysUserHolder.getUser().getUserId();
        List<Long> userTeamIds = appTaskQueryService.queryUserInspectionTeamIds(currentUserId);
        queryDTO.setCurrentUserId(currentUserId);
        queryDTO.setCurrentUserTeamIds(userTeamIds);

        return queryDTO;
    }

    @Override
    public InspectionOrderEntryQueryDTO setInspectionOrderEntryPermission(Object reqVO) {
        InspectionOrderEntryQueryDTO queryDTO = new InspectionOrderEntryQueryDTO();
        BeanUtils.copyProperties(reqVO, queryDTO);

        // 设置当前用户的数据权限
        String currentUserId = SysUserHolder.getUser().getUserId();
        List<Long> userTeamIds = appTaskQueryService.queryUserInspectionTeamIds(currentUserId);
        queryDTO.setCurrentUserId(currentUserId);
        queryDTO.setCurrentUserTeamIds(userTeamIds);

        return queryDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upsertEntryRecordsFromEln(BatchEntryDTO batchEntryDTO) {
        if (batchEntryDTO == null || CollUtil.isEmpty(batchEntryDTO.getEntryItems())) {
            throw new BmosException(LimsResponseCode.DATA_ENTRY_DATA_NOT_EMPTY);
        }
        // 根据 taskId + dataPointConfigId 判断是否已有记录：
        // - 若已存在且已有值：赋值 id，走修改路径
        // - 否则：保持 id 为空，走新增路径
        for (BatchEntryDTO.EntryItemDTO item : batchEntryDTO.getEntryItems()) {
            if (item.getId() != null) {
                continue;
            }
            if (item.getTaskId() == null || item.getDataPointConfigId() == null) {
                continue;
            }
            List<InspectionEntryRecordDTO> list =
                    inspectionEntryRecordMapper.selectBySchemeDataPointIdAndTaskId(item.getDataPointConfigId(), item.getTaskId());
            if (list != null && !list.isEmpty()) {
                // 取最新一条（SQL按create_time升序，取最后一个）
                InspectionEntryRecordDTO latest = list.get(list.size() - 1);
                if (latest != null && hasValue(latest)) {
                    item.setId(latest.getId());
                }
            }
        }
        // 复用原有批量保存（新增/修改合一）逻辑；ELN 同步不重算任务状态
        return batchSaveEntryRecords(batchEntryDTO, true);
    }
}
