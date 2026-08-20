package com.bmos.lims2.server.task.service;

import com.bmos.common.base.user.SysUser;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.server.task.entity.TaskStatusHistory;
import com.bmos.lims2.server.task.dto.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 任务服务接口
 * 
 * @author system
 * @since 2025/01/29
 */
public interface TaskService {

    /**
     * 样品接收完成后生成任务
     *
     * @param sampleId 样品ID
     * @param user
     */
    void generateTasksAfterSampleReceived(Long sampleId, SysUser user);

    /**
     * 分页查询分析项分配列表（母子列表结构）
     * 
     * @param queryDTO 查询条件
     * @return 分析项分配列表
     */
    CommonPage<AnalysisItemAssignmentDTO> queryAnalysisItemAssignmentPage(TaskQueryDTO queryDTO);

    /**
     * 分页查询检验单任务列表（检验单分配页签）
     * 
     * @param queryDTO 查询条件
     * @return 检验单任务列表
     */
    CommonPage<TaskDTO> queryInspectionOrderTaskPage(TaskQueryDTO queryDTO);

    /**
     * 分页查询检验单分配列表（母子列表结构）
     * 
     * @param queryDTO 查询条件
     * @return 检验单分配列表
     */
    CommonPage<InspectionOrderAssignmentDTO> queryInspectionOrderAssignmentPage(TaskQueryDTO queryDTO);

    /**
     * 查询任务状态统计
     * 
     * @param queryDTO 查询条件
     * @return 状态统计
     */
    List<TaskStatusCountDTO> queryTaskStatusCount(TaskQueryDTO queryDTO);

    /**
     * 分配任务
     * 
     * @param assignDTO 分配参数
     */
    void assignTasks(TaskAssignDTO assignDTO);

    /**
     * 领取任务
     * 
     * @param taskIds 任务ID列表
     */
    void claimTasks(List<Long> taskIds);

    /**
     * 退回任务
     * 
     * @param returnDTO 退回参数
     */
    void returnTasks(TaskReturnDTO returnDTO);

    /**
     * 审批退回任务
     * 
     * @param approvalDTO 审批参数
     */
    void approveReturnTasks(TaskApprovalDTO approvalDTO);

    /**
     * 终止任务
     * 
     * @param terminateDTO 终止参数
     */
    void terminateTasks(TaskTerminateDTO terminateDTO);

    /**
     * 查询任务状态变更历史
     *
     * @param taskId 任务ID
     * @return 状态变更历史列表
     */
    List<TaskStatusHistoryDTO> queryTaskStatusHistory(Long taskId);

    /**
     * 查询可分配的人员列表
     * 
     * @param taskIds 任务ID列表
     * @return 人员列表及其当前待完成任务数量
     */
    List<AssignableUserDTO> queryAssignableUsers(List<Long> taskIds);

    /**
     * 设置检验单分配页签的数据权限（不需要数据权限控制）
     * 
     * @param reqVO 请求VO
     * @return 查询DTO
     */
    TaskQueryDTO setTaskPermission(Object reqVO);

    /**
     * APP-ELN 完成任务（录入完成，置为待复核）
     *
     * @param taskId 任务ID
     * @return 任务状态
     */
    TaskStatusEnum completeTaskForAppEln(Long taskId);
}
