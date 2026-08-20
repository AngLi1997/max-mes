package com.bmos.lims2.server.task.mapper;

import com.bmos.lims2.server.task.entity.Task;
import com.bmos.lims2.server.task.dto.*;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.time.LocalDateTime;

/**
 * 任务Mapper接口
 * 
 * @author system
 * @since 2025/01/29
 */
@Mapper
public interface TaskMapper extends BaseMapperX<Task> {

    /**
     * 分页查询分析项分配列表（母子列表结构）
     * 
     * @param queryDTO 查询条件
     * @return 分析项分配列表
     */
    List<AnalysisItemAssignmentDTO> selectAnalysisItemAssignmentPage(@Param("query") TaskQueryDTO queryDTO);

    /**
     * 查询分析项下的检验单任务分组
     * 
     *
     * @param inspectItemId
     * @param parameterId 分析项ID
     * @param queryDTO 查询条件
     * @return 检验单任务分组列表
     */
    List<InspectionOrderTaskGroupDTO> selectInspectionOrderTaskGroups(@Param("inspectItemId") Long inspectItemId, @Param("parameterId") Long parameterId, @Param("query") TaskQueryDTO queryDTO);

    /**
     * 查询检验单在指定分析项下的任务列表
     * 
     * @param inspectionOrderId 检验单ID
     * @param parameterId 分析项ID
     * @param queryDTO 查询条件
     * @return 任务列表
     */
    List<TaskDTO> selectTasksByInspectionOrderAndParameter(@Param("inspectionOrderId") Long inspectionOrderId, 
                                                          @Param("parameterId") Long parameterId, 
                                                          @Param("query") TaskQueryDTO queryDTO);

    /**
     * 分页查询检验单任务列表（检验单分配页签）
     * 
     * @param queryDTO 查询条件
     * @return 检验单任务列表
     */
    List<TaskDTO> selectInspectionOrderTaskPage(@Param("query") TaskQueryDTO queryDTO);

    /**
     * 分页查询检验单分配列表（母列表）
     * 
     * @param queryDTO 查询条件
     * @return 检验单分配列表
     */
    List<InspectionOrderAssignmentDTO> selectInspectionOrderAssignmentPage(@Param("query") TaskQueryDTO queryDTO);

    /**
     * 查询检验单下的任务列表
     * 
     * @param inspectionOrderId 检验单ID
     * @param queryDTO 查询条件
     * @return 任务列表
     */
    List<TaskDTO> selectTasksByInspectionOrder(@Param("inspectionOrderId") Long inspectionOrderId, @Param("query") TaskQueryDTO queryDTO);

    /**
     * 查询任务状态统计
     * 
     * @param queryDTO 查询条件
     * @return 状态统计
     */
    List<TaskStatusCountDTO> selectTaskStatusCount(@Param("query") TaskQueryDTO queryDTO);





    /**
     * 查询用户当前待完成任务数量
     * 
     * @param userIds 用户ID列表
     * @return 用户任务数量统计
     */
    List<UserPendingTaskCountDTO> selectUserPendingTaskCount(@Param("userIds") List<Long> userIds);

    /**
     * 查询用户所在的检验班组ID列表
     * 
     * @param userId 用户ID
     * @return 检验班组ID列表
     */
    List<Long> selectUserInspectionTeamIds(@Param("userId") String userId);

    /**
     * 查询任务对应的检验项目ID列表
     * 
     * @param taskIds 任务ID列表
     * @return 检验项目ID列表
     */
    List<Long> selectInspectItemIdsByTaskIds(@Param("taskIds") List<Long> taskIds);

    /**
     * 查询检验项目对应的班组ID列表
     * 
     * @param inspectItemIds 检验项目ID列表
     * @return 班组ID列表
     */
    List<Long> selectTeamIdsByInspectItemIds(@Param("inspectItemIds") List<Long> inspectItemIds);

    /**
     * 根据任务ID列表查询任务的检验方案配置信息
     * 
     * @param taskIds 任务ID列表
     * @return 任务检验方案配置信息列表
     */
    List<TaskSchemeConfigDTO> selectTaskSchemeConfigByTaskIds(@Param("taskIds") List<Long> taskIds);

    /**
     * 根据检验项目ID和检验方案版本ID查询班组ID列表
     * 
     * @param inspectItemId 检验项目ID
     * @param schemeVersionId 检验方案版本ID
     * @return 班组ID列表
     */
    List<Long> selectTeamIdsByInspectItemAndSchemeVersion(@Param("inspectItemId") Long inspectItemId,
                                                         @Param("schemeVersionId") Long schemeVersionId);

    /**
     * 根据检验项目ID和稳定性方案版本ID查询班组ID列表（稳定性方案）
     *
     * @param inspectItemId   检验项目ID
     * @param schemeVersionId 稳定性方案版本ID
     * @return 班组ID列表
     */
    List<Long> selectTeamIdsByInspectItemAndStabilitySchemeVersion(@Param("inspectItemId") Long inspectItemId,
                                                                   @Param("schemeVersionId") Long schemeVersionId);

    /**
     * 查询班组中的用户列表
     * 
     * @param teamIds 班组ID列表
     * @return 用户信息列表
     */
    List<AssignableUserDTO> selectUsersByTeamIds(@Param("teamIds") List<Long> teamIds);

    /**
     * 根据方案版本ID集合查询班组ID列表
     *
     * @param schemeVersionIds 方案版本ID列表
     * @return 班组ID列表
     */
    List<Long> selectTeamIdsBySchemeVersionIds(@Param("schemeVersionIds") List<Long> schemeVersionIds);

    /**
     * 根据检验方案版本ID查询检验项目和分析项配置
     * 
     * @param schemeVersionId 检验方案版本ID
     * @return 检验项目和分析项配置列表
     */
    List<SchemeParameterDTO> selectSchemeItemsAndParameters(@Param("schemeVersionId") Long schemeVersionId);

    /**
     * 检查任务是否已存在
     * 
     * @param sampleId 样品ID
     * @param parameterId 分析项ID
     * @return 是否存在
     */
    boolean existsTaskBySampleAndParameter(@Param("sampleId") Long sampleId, 
                                         @Param("parameterId") Long parameterId);

    /**
     * 检查任务是否已存在（基于检验单 + 检验项目 + 分析项）
     *
     * @param inspectionOrderId 检验单ID
     * @param inspectItemId 检验项目ID
     * @param parameterId 分析项ID
     * @return 是否存在
     */
    boolean existsTaskByOrderItemAndParameter(@Param("inspectionOrderId") Long inspectionOrderId,
                                              @Param("inspectItemId") Long inspectItemId,
                                              @Param("parameterId") Long parameterId);

    /**
     * 验证用户是否有权限操作指定的任务列表
     * 
     * @param userId 用户ID
     * @param taskIds 任务ID列表
     * @return 用户有权限操作的任务数量
     */
    int countUserAuthorizedTasks(@Param("userId") String userId, @Param("taskIds") List<Long> taskIds);

    /**
     * 查询检验单的最早任务开始时间（领取时间或分配时间，以较早者为准）
     * @param inspectionOrderId 检验单ID
     * @return 最早任务开始时间
     */
    LocalDateTime selectEarliestStartTimeByOrderId(@Param("inspectionOrderId") Long inspectionOrderId);
}
