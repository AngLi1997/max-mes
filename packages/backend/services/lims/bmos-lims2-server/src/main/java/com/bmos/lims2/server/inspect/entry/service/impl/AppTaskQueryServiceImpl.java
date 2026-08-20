package com.bmos.lims2.server.inspect.entry.service.impl;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.InspectionOrderSourceEnum;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.server.inspect.entry.dto.*;
import com.bmos.lims2.server.inspect.entry.mapper.AppTaskQueryMapper;
import com.bmos.lims2.server.inspect.entry.service.AppTaskQueryService;
import com.bmos.lims2.server.inspect.entry.mapper.InspectionEntryRecordMapper;
import com.bmos.lims2.server.inspect.entry.service.JudgmentExpressionService;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeJudgmentMapper;
import com.bmos.lims2.server.inspect.team.service.InspectionTeamService;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeParameter;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeJudgmentMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeParameterMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: APP 任务查询专用服务实现
 * @Author: yigaohui
 * @Date: 2025/11/17 00:20
 */
@Service
public class AppTaskQueryServiceImpl implements AppTaskQueryService {

    @Autowired
    private AppTaskQueryMapper appMapper;

    @Autowired
    private JudgmentExpressionService judgmentExpressionService;

    @Autowired
    private InspectionSchemeJudgmentMapper inspectionSchemeJudgmentMapper;

    @Autowired
    private StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    @Autowired
    private StabilitySchemeParameterMapper stabilitySchemeParameterMapper;

    @Autowired
    private StabilitySchemeJudgmentMapper stabilitySchemeJudgmentMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private InspectionEntryRecordMapper entryMapper;

    @Autowired
    private InspectionTeamService inspectionTeamService;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Override
    public CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO> pageAnalysisItemMethodGroup(AppAnalysisItemEntryQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        if (queryDTO.getRequestStartTime() != null && queryDTO.getRequestEndTime() != null) {
            queryDTO.setRequestStartTime(queryDTO.getRequestStartTime().toLocalDate().atStartOfDay());
            queryDTO.setRequestEndTime(queryDTO.getRequestEndTime().toLocalDate().atTime(23,59,59));
        }
        List<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO> list =
                appMapper.appSelectAnalysisItemMethodEntryList(queryDTO);
        // 组装子列表：按现有母列表结构（parameterId + recordVersionId），查询检验单任务子列表
        for (com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO group : list) {
            List<AppTaskEntryItemDTO> children =
                    appMapper.appSelectInspectionOrdersByAnalysisItem(
                            null, // 母列表无检验项目维度，此处不限定检验项目
                            group.getInspectParameterId(),
                            group.getRecordVersionId(),
                            queryDTO
                    );
            supplementStabilityFields(children);
            // 填充 canInputJudgment（与 LIMS 端口径一致）
            if (children != null) {
                for (AppTaskEntryItemDTO c : children) {
                    Boolean hasConfig = hasJudgmentConfiguredBasic(c.getSchemeVersionId(), c.getParameterConfigId());
                    c.setCanInputJudgment(Boolean.FALSE.equals(hasConfig));
                }
            }
            group.setInspectionTasks(children);
        }
        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<AppInspectionOrderEntryDTO> pageInspectionOrderEntry(InspectionOrderEntryQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        if (queryDTO.getRequestStartTime() != null && queryDTO.getRequestEndTime() != null) {
            queryDTO.setRequestStartTime(queryDTO.getRequestStartTime().toLocalDate().atStartOfDay());
            queryDTO.setRequestEndTime(queryDTO.getRequestEndTime().toLocalDate().atTime(23,59,59));
        }
        List<AppInspectionOrderEntryDTO> list = appMapper.appSelectInspectionOrderEntryList(queryDTO);
        for (AppInspectionOrderEntryDTO order : list) {
            List<AppTaskEntryItemDTO> items =
                    appMapper.appSelectTasksByInspectionOrderPage(order.getInspectionOrderId(), queryDTO);
            supplementStabilityFields(items);
            // 填充 canInputJudgment（与 LIMS 端口径一致）
            if (items != null) {
                for (AppTaskEntryItemDTO t : items) {
                    Boolean hasConfig = hasJudgmentConfiguredBasic(t.getSchemeVersionId(), t.getParameterConfigId());
                    t.setCanInputJudgment(Boolean.FALSE.equals(hasConfig));
                }
            }
            order.setInspectionTasks(items);
        }
        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<AnalysisItemEntryDTO> pageAnalysisItemReview(AppAnalysisItemEntryQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<AnalysisItemEntryDTO> list = appMapper.appSelectAnalysisItemEntryList(queryDTO);
        for (AnalysisItemEntryDTO item : list) {
            List<AnalysisItemEntryDTO.InspectionOrderEntryItemDTO> children =
                    appMapper.appSelectInspectionOrdersByAnalysisItemForReview(item.getInspectItemId(), item.getInspectParameterId(), item.getRecordVersionId(), queryDTO);
            // 填充 canInputJudgment（与 LIMS 端口径一致）
            if (children != null) {
                for (AnalysisItemEntryDTO.InspectionOrderEntryItemDTO c : children) {
                    Boolean hasConfig = hasJudgmentConfiguredBasic(c.getSchemeVersionId(), c.getParameterConfigId());
                    c.setCanInputJudgment(Boolean.FALSE.equals(hasConfig));
                }
            }
            item.setInspectionTasks(children);
        }
        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<AppInspectionOrderEntryDTO> pageInspectionOrderReview(InspectionOrderEntryQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<AppInspectionOrderEntryDTO> list = appMapper.appSelectInspectionOrderEntryList(queryDTO);
        for (AppInspectionOrderEntryDTO order : list) {
            List<AppTaskEntryItemDTO> children =
                    appMapper.appSelectTasksByInspectionOrderPage(order.getInspectionOrderId(), queryDTO);
            supplementStabilityFields(children);
            // 填充 canInputJudgment（与 LIMS 端口径一致）
            if (children != null) {
                for (AppTaskEntryItemDTO t : children) {
                    Boolean hasConfig = hasJudgmentConfiguredBasic(t.getSchemeVersionId(), t.getParameterConfigId());
                    t.setCanInputJudgment(Boolean.FALSE.equals(hasConfig));
                }
            }
            order.setInspectionTasks(children);
        }
        return CommonPage.convertPage(list);
    }


    @Override
    public EntryStatsDTO taskStats() {
        // 仅统计当前登录人的任务，执行方式固定为 ELN
        String currentUserId = SysUserHolder.getUser().getUserId();
        EntryStatsDTO stats = appMapper.appTaskStats(currentUserId, com.bmos.lims2.common.enums.ExecuteMethodEnum.ELN);
        if (stats == null) {
            stats = new EntryStatsDTO();
            stats.setAllCount(0L);
            stats.setIncompleteCount(0L);
            stats.setAbnormalCount(0L);
        }
        return stats;
    }

    @Override
    public Long reviewTaskStats() {
        String currentUserId = SysUserHolder.getUser().getUserId();
        List<Long> userTeamIds = queryUserInspectionTeamIds(currentUserId);
        AppAnalysisItemEntryQueryDTO queryDTO = new AppAnalysisItemEntryQueryDTO();
        queryDTO.setCurrentUserId(currentUserId);
        queryDTO.setCurrentUserTeamIds(userTeamIds);
        queryDTO.setExecuteMethod(com.bmos.lims2.common.enums.ExecuteMethodEnum.ELN);
        Long count = appMapper.appReviewTaskStats(queryDTO);
        return count != null ? count : 0L;
    }

    @Override
    public AppAnalysisItemEntryQueryDTO setAnalysisItemEntryPermission(Object reqVO) {
        AppAnalysisItemEntryQueryDTO queryDTO = new AppAnalysisItemEntryQueryDTO();
        org.springframework.beans.BeanUtils.copyProperties(reqVO, queryDTO);
        String currentUserId = SysUserHolder.getUser().getUserId();
        // 仅查询当前登录人的任务，不再依据班组权限
        queryDTO.setCurrentUserId(currentUserId);
        queryDTO.setOwnerOnly(Boolean.TRUE);
        queryDTO.setCurrentUserTeamIds(null);
        return queryDTO;
    }

    @Override
    public InspectionOrderEntryQueryDTO setInspectionOrderEntryPermission(Object reqVO) {
        InspectionOrderEntryQueryDTO queryDTO = new InspectionOrderEntryQueryDTO();
        BeanUtils.copyProperties(reqVO, queryDTO);
        String currentUserId = SysUserHolder.getUser().getUserId();
        // 仅查询当前登录人的任务，不再依据班组权限
        queryDTO.setCurrentUserId(currentUserId);
        queryDTO.setOwnerOnly(Boolean.TRUE);
        queryDTO.setCurrentUserTeamIds(null);
        return queryDTO;
    }

    @Override
    public AppAnalysisItemEntryQueryDTO setAnalysisItemReviewPermission(Object reqVO) {
        AppAnalysisItemEntryQueryDTO queryDTO = new AppAnalysisItemEntryQueryDTO();
        BeanUtils.copyProperties(reqVO, queryDTO);
        String currentUserId = SysUserHolder.getUser().getUserId();
        List<Long> userTeamIds = queryUserInspectionTeamIds(currentUserId);
        queryDTO.setCurrentUserId(currentUserId);
        queryDTO.setCurrentUserTeamIds(userTeamIds);
        return queryDTO;
    }

    @Override
    public InspectionOrderEntryQueryDTO setInspectionOrderReviewPermission(Object reqVO) {
        InspectionOrderEntryQueryDTO queryDTO = new InspectionOrderEntryQueryDTO();
        BeanUtils.copyProperties(reqVO, queryDTO);
        String currentUserId = SysUserHolder.getUser().getUserId();
        List<Long> userTeamIds = queryUserInspectionTeamIds(currentUserId);
        queryDTO.setCurrentUserId(currentUserId);
        queryDTO.setCurrentUserTeamIds(userTeamIds);
        return queryDTO;
    }

    @Override
    public java.util.List<AppInspectionOrderEntryDTO.InspectItemTaskGroupDTO> listElnTaskTreeByInspectionOrder(Long inspectionOrderId, Long taskId) {
        List<AppTaskEntryItemDTO> items = appMapper.selectElnTasksByInspectionOrderForTree(inspectionOrderId);
        supplementStabilityFields(items);
        if (items != null) {
            for (AppTaskEntryItemDTO t : items) {
                Boolean hasConfig = hasJudgmentConfiguredBasic(t.getSchemeVersionId(), t.getParameterConfigId());
                t.setCanInputJudgment(Boolean.FALSE.equals(hasConfig));
            }
        }

        // 如果传入了taskId，确保该任务包含在返回结果中
        if (taskId != null) {
            // 检查该任务是否已经在查询结果中
            boolean taskExists = false;
            if (items != null) {
                for (AppTaskEntryItemDTO item : items) {
                    if (taskId.equals(item.getId())) {
                        taskExists = true;
                        break;
                    }
                }
            }

            // 如果任务不在查询结果中，单独查询该任务
            if (!taskExists) {
                AppTaskEntryItemDTO currentTask = appMapper.selectElnTaskByIdForTree(taskId);
                if (currentTask != null) {
                    supplementStabilityFields(java.util.Collections.singletonList(currentTask));
                    Boolean hasConfig = hasJudgmentConfiguredBasic(currentTask.getSchemeVersionId(), currentTask.getParameterConfigId());
                    currentTask.setCanInputJudgment(Boolean.FALSE.equals(hasConfig));

                    // 将当前任务添加到列表中
                    if (items == null) {
                        items = new java.util.ArrayList<>();
                    }
                    items.add(currentTask);
                }
            }
        }

        java.util.Map<Long, AppInspectionOrderEntryDTO.InspectItemTaskGroupDTO> groupMap = new java.util.LinkedHashMap<>();
        if (items != null) {
            for (AppTaskEntryItemDTO t : items) {
                Long key = t.getInspectItemId();
                AppInspectionOrderEntryDTO.InspectItemTaskGroupDTO group = groupMap.get(key);
                if (group == null) {
                    group = new AppInspectionOrderEntryDTO.InspectItemTaskGroupDTO();
                    group.setInspectItemId(t.getInspectItemId());
                    group.setInspectItemName(t.getInspectItemName());
                    group.setInspectItemCode(t.getInspectItemCode());
                    group.setTasks(new java.util.ArrayList<>());
                    groupMap.put(key, group);
                }
                group.getTasks().add(t);
            }
        }
        return new java.util.ArrayList<>(groupMap.values());
    }

    @Override
    public List<Long> queryUserInspectionTeamIds(String userId) {
        return taskMapper.selectUserInspectionTeamIds(userId);
    }

    @Override
    public AppTaskDetailDTO getTaskDetail(Long taskId) {
        AppTaskDetailDTO detail = appMapper.selectTaskDetailById(taskId);
        if (detail == null) {
            return null;
        }
        // 稳定性任务补全 schemeId
        if (detail.getSchemeId() == null && detail.getSchemeVersionId() != null) {
            StabilitySchemeVersion ssv = stabilitySchemeVersionMapper.selectById(detail.getSchemeVersionId());
            if (ssv != null) {
                detail.setSchemeId(ssv.getSchemeId());
            }
        }
        Boolean hasConfig = hasJudgmentConfiguredBasic(detail.getSchemeVersionId(), detail.getParameterConfigId());
        detail.setCanInputJudgment(Boolean.FALSE.equals(hasConfig));

        boolean isStability = detail.getSchemeId() == null || stabilitySchemeVersionMapper.selectById(detail.getSchemeVersionId()) != null;
        detail.setDataPoints(isStability
            ? entryMapper.selectByTaskIdForStability(taskId)
            : entryMapper.selectByTaskId(taskId));
        return detail;
    }

    @Override
    public List<String> listTeamMembersByTaskId(Long taskId) {
        com.bmos.lims2.server.task.entity.Task task = taskMapper.selectById(taskId);
        if (task == null) {
            return java.util.Collections.emptyList();
        }
        List<com.bmos.lims2.server.inspect.team.dto.InspectionTeamUserDTO> users =
                inspectionTeamService.listUsersBySchemeVersionAndInspectItem(task.getSchemeVersionId(), task.getInspectItemId());
        return users.stream()
                .map(com.bmos.lims2.server.inspect.team.dto.InspectionTeamUserDTO::getUserId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO> pageAnalysisItemMethodGroupForReview(AppAnalysisItemEntryQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        if (queryDTO.getRequestStartTime() != null && queryDTO.getRequestEndTime() != null) {
            queryDTO.setRequestStartTime(queryDTO.getRequestStartTime().toLocalDate().atStartOfDay());
            queryDTO.setRequestEndTime(queryDTO.getRequestEndTime().toLocalDate().atTime(23, 59, 59));
        }
        List<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO> list =
                appMapper.appSelectAnalysisItemMethodReviewList(queryDTO);
        for (com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO group : list) {
            List<AppTaskEntryItemDTO> children =
                    appMapper.appSelectInspectionOrdersByAnalysisItemTeamReview(
                            null,
                            group.getInspectParameterId(),
                            group.getRecordVersionId(),
                            queryDTO
                    );
            supplementStabilityFields(children);
            if (children != null) {
                for (AppTaskEntryItemDTO c : children) {
                    Boolean hasConfig = hasJudgmentConfiguredBasic(c.getSchemeVersionId(), c.getParameterConfigId());
                    c.setCanInputJudgment(Boolean.FALSE.equals(hasConfig));
                }
            }
            group.setInspectionTasks(children);
        }
        return CommonPage.convertPage(list);
    }

    @Override
    public CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO> pageInspectionOrderForReview(InspectionOrderEntryQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        if (queryDTO.getRequestStartTime() != null && queryDTO.getRequestEndTime() != null) {
            queryDTO.setRequestStartTime(queryDTO.getRequestStartTime().toLocalDate().atStartOfDay());
            queryDTO.setRequestEndTime(queryDTO.getRequestEndTime().toLocalDate().atTime(23, 59, 59));
        }
        List<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO> list =
                appMapper.appSelectInspectionOrderReviewList(queryDTO);
        for (com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO order : list) {
            List<AppTaskEntryItemDTO> items =
                    appMapper.appSelectTasksByInspectionOrderTeamReview(order.getInspectionOrderId(), queryDTO);
            supplementStabilityFields(items);
            if (items != null) {
                for (AppTaskEntryItemDTO t : items) {
                    Boolean hasConfig = hasJudgmentConfiguredBasic(t.getSchemeVersionId(), t.getParameterConfigId());
                    t.setCanInputJudgment(Boolean.FALSE.equals(hasConfig));
                }
            }
            order.setInspectionTasks(items);
        }
        return CommonPage.convertPage(list);
    }

    /**
     * 为稳定性任务补全 schemeId 和 standardRule。
     * schemeId 为 null 时说明是稳定性任务，查 lm_stability_scheme_version 补全。
     */
    private void supplementStabilityFields(List<AppTaskEntryItemDTO> tasks) {
        if (tasks == null || tasks.isEmpty()) return;
        for (AppTaskEntryItemDTO task : tasks) {
            if (task.getSchemeId() != null) continue;
            Long schemeVersionId = task.getSchemeVersionId();
            if (schemeVersionId == null) continue;
            StabilitySchemeVersion ssv = stabilitySchemeVersionMapper.selectById(schemeVersionId);
            if (ssv == null) continue;
            task.setSchemeId(ssv.getSchemeId());
            if (task.getStandardRule() == null && task.getParameterConfigId() != null) {
                StabilitySchemeParameter ssp = stabilitySchemeParameterMapper.selectById(task.getParameterConfigId());
                if (ssp != null) {
                    task.setStandardRule(ssp.getStandardRule());
                }
            }
        }
    }

    /**
     * 是否已配置结论判定 - 按来源分支查询
     * 先查常规检验判定表，再查稳定性判定表
     */
    private Boolean hasJudgmentConfiguredBasic(Long schemeVersionId, Long parameterConfigId) {
        if (parameterConfigId == null) return Boolean.FALSE;
        List<com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO> regularList =
                inspectionSchemeJudgmentMapper.listByParameterConfigId(parameterConfigId);
        if (regularList != null && !regularList.isEmpty()) return Boolean.TRUE;
        if (!stabilitySchemeJudgmentMapper.listByParamConfigId(parameterConfigId).isEmpty()) return Boolean.TRUE;
        return Boolean.FALSE;
    }
}


