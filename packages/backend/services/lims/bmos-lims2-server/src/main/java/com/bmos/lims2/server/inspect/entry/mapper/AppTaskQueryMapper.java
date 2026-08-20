package com.bmos.lims2.server.inspect.entry.mapper;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.server.inspect.entry.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: APP 任务查询专用 Mapper（与录入列表查询分离）
 * @Author: yigaohui
 * @Date: 2025/11/17 00:30
 */
@Mapper
public interface AppTaskQueryMapper {

    List<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO> appSelectAnalysisItemMethodEntryList(@Param("query") AppAnalysisItemEntryQueryDTO queryDTO);

    List<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO> appSelectInspectionOrderEntryList(@Param("query") InspectionOrderEntryQueryDTO queryDTO);

    List<com.bmos.lims2.server.inspect.entry.dto.AppTaskEntryItemDTO> appSelectTasksByInspectionOrderPage(@Param("inspectionOrderId") Long inspectionOrderId,
                                                                                                           @Param("query") InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 根据检验单ID查询任务（仅ELN），用于树形返回按检验项目分组（不限制负责人）
     */
    List<com.bmos.lims2.server.inspect.entry.dto.AppTaskEntryItemDTO> selectElnTasksByInspectionOrderForTree(@Param("inspectionOrderId") Long inspectionOrderId);

    /**
     * 根据任务ID查询单个任务（仅ELN），用于确保当前任务包含在返回结果中
     */
    com.bmos.lims2.server.inspect.entry.dto.AppTaskEntryItemDTO selectElnTaskByIdForTree(@Param("taskId") Long taskId);

    List<AnalysisItemEntryDTO> appSelectAnalysisItemEntryList(@Param("query") AppAnalysisItemEntryQueryDTO queryDTO);

    List<com.bmos.lims2.server.inspect.entry.dto.AppTaskEntryItemDTO> appSelectInspectionOrdersByAnalysisItem(@Param("inspectItemId") Long inspectItemId,
                                                                                                               @Param("inspectParameterId") Long inspectParameterId,
                                                                                                               @Param("recordVersionId") Long recordVersionId,
                                                                                                               @Param("query") AppAnalysisItemEntryQueryDTO queryDTO);

    /**
     * 复核场景：保持原有复核DTO结构
     */
    List<AnalysisItemEntryDTO.InspectionOrderEntryItemDTO> appSelectInspectionOrdersByAnalysisItemForReview(@Param("inspectItemId") Long inspectItemId,
                                                                                                             @Param("inspectParameterId") Long inspectParameterId,
                                                                                                             @Param("recordVersionId") Long recordVersionId,
                                                                                                             @Param("query") AppAnalysisItemEntryQueryDTO queryDTO);

    EntryStatsDTO appTaskStats(@Param("currentUserId") String currentUserId,
                               @Param("executeMethod") ExecuteMethodEnum executeMethod);

    /**
     * 复核任务数量统计（按班组权限，仅统计待复核）
     */
    Long appReviewTaskStats(@Param("query") AppAnalysisItemEntryQueryDTO queryDTO);

    /**
     * 根据任务ID查询任务详情（含审核人信息）
     */
    AppTaskDetailDTO selectTaskDetailById(@Param("taskId") Long taskId);

    /**
     * 复核视角：按分析项+方法分组（班组范围，待复核）
     */
    List<com.bmos.lims2.server.inspect.entry.dto.AppAnalysisItemMethodGroupDTO> appSelectAnalysisItemMethodReviewList(@Param("query") AppAnalysisItemEntryQueryDTO queryDTO);

    /**
     * 复核视角：分析项子列表（班组范围，待复核）
     */
    List<com.bmos.lims2.server.inspect.entry.dto.AppTaskEntryItemDTO> appSelectInspectionOrdersByAnalysisItemTeamReview(
            @Param("inspectItemId") Long inspectItemId,
            @Param("inspectParameterId") Long inspectParameterId,
            @Param("recordVersionId") Long recordVersionId,
            @Param("query") AppAnalysisItemEntryQueryDTO queryDTO);

    /**
     * 复核视角：检验单母列表（班组范围，待复核）
     */
    List<com.bmos.lims2.server.inspect.entry.dto.AppInspectionOrderEntryDTO> appSelectInspectionOrderReviewList(@Param("query") InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 复核视角：检验单子列表（班组范围，待复核）
     */
    List<com.bmos.lims2.server.inspect.entry.dto.AppTaskEntryItemDTO> appSelectTasksByInspectionOrderTeamReview(
            @Param("inspectionOrderId") Long inspectionOrderId,
            @Param("query") InspectionOrderEntryQueryDTO queryDTO);
}


