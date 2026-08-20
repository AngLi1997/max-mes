package com.bmos.lims2.server.inspect.entry.service;

import com.bmos.lims2.server.inspect.entry.dto.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * @Description: APP 任务查询专用服务（与录入列表查询解耦）
 * @Author: yigaohui
 * @Date: 2025/11/17 00:20
 */
public interface AppTaskQueryService {

    /**
     * APP-分析项-记录分组分页（母列表）
     */
    CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO> pageAnalysisItemMethodGroup(AppAnalysisItemEntryQueryDTO queryDTO);

    /**
     * APP-检验单录入分页（母列表）
     */
    CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO> pageInspectionOrderEntry(InspectionOrderEntryQueryDTO queryDTO);

    /**
     * APP-分析项复核分页（母列表）
     */
    CommonPage<AnalysisItemEntryDTO> pageAnalysisItemReview(AppAnalysisItemEntryQueryDTO queryDTO);

    /**
     * APP-检验单复核分页（母列表）
     */
    CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO> pageInspectionOrderReview(InspectionOrderEntryQueryDTO queryDTO);

    /**
     * APP-任务统计（未完成/异常/全部），仅统计当前登录人
     */
    EntryStatsDTO taskStats();

    /**
     * APP-复核任务数量统计（班组内所有待复核任务数量）
     */
    Long reviewTaskStats();

    /**
     * 设置APP分析项查询权限（录入场景：仅当前登录人）
     */
    AppAnalysisItemEntryQueryDTO setAnalysisItemEntryPermission(Object reqVO);

    /**
     * 设置APP检验单查询权限（录入场景：仅当前登录人）
     */
    InspectionOrderEntryQueryDTO setInspectionOrderEntryPermission(Object reqVO);

    /**
     * 设置APP分析项复核查询权限（复核场景：班组内所有待复核任务）
     */
    AppAnalysisItemEntryQueryDTO setAnalysisItemReviewPermission(Object reqVO);

    /**
     * 设置APP检验单复核查询权限（复核场景：班组内所有待复核任务）
     */
    InspectionOrderEntryQueryDTO setInspectionOrderReviewPermission(Object reqVO);

    /**
     * 查询用户所在检验班组的ID列表
     */
    List<Long> queryUserInspectionTeamIds(String userId);

    /**
     * 通用：按检验单ID查询该单下所有ELN任务，并按"检验项目-分析项"树形分组
     * 不限制负责人，返回统一的APP任务子项结构
     * @param inspectionOrderId 检验单ID
     * @param taskId 当前任务ID（可选，如果传入则确保该任务包含在返回结果中）
     * @return 分组结果
     */
    java.util.List<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO.InspectItemTaskGroupDTO> listElnTaskTreeByInspectionOrder(Long inspectionOrderId, Long taskId);

    /**
     * APP-任务详情查询（按任务ID，含检验时间、复核人/审核人名称-编码等全量信息）
     */
    AppTaskDetailDTO getTaskDetail(Long taskId);

    /**
     * APP-查询任务所属检验项目班组的所有成员用户ID列表
     */
    List<String> listTeamMembersByTaskId(Long taskId);

    /**
     * APP-复核-分析项+方法分组分页（班组范围，待复核）
     */
    CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO> pageAnalysisItemMethodGroupForReview(AppAnalysisItemEntryQueryDTO queryDTO);

    /**
     * APP-复核-检验单分页（班组范围，待复核）
     */
    CommonPage<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO> pageInspectionOrderForReview(InspectionOrderEntryQueryDTO queryDTO);
}


