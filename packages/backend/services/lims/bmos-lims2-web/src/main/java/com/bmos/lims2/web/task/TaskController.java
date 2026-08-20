package com.bmos.lims2.web.task;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.task.entity.TaskStatusHistory;
import com.bmos.lims2.server.task.service.TaskService;
import com.bmos.lims2.server.task.dto.*;
import com.bmos.lims2.web.task.vo.req.*;
import com.bmos.mybatis.page.CommonPage;
import org.springframework.beans.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 任务管理控制器
 * 
 * @author system
 * @since 2025/01/29
 */
@RestController
@RequestMapping("/task")
@Api(tags = "任务管理-接口")
@Validated
@Slf4j
public class TaskController {

    @Autowired
    private TaskService taskService;

    @ApiOperation("分页查询分析项分配列表（母子列表结构）")
    @PostMapping("/analysis-assignment/task-page")
    public ResponseInfo<CommonPage<AnalysisItemAssignmentDTO>> queryAnalysisItemAssignmentPage(
            @RequestBody @Valid TaskQueryReqVO reqVO) {
        TaskQueryDTO queryDTO = taskService.setTaskPermission(reqVO);
        // 如果人员没有班组，直接返回
        if (CollectionUtil.isEmpty(queryDTO.getCurrentUserTeamIds())){
            return ResponseInfo.success(CommonPage.CommonPage(Collections.emptyList(), 0L, reqVO));
        }
        CommonPage<AnalysisItemAssignmentDTO> result = taskService.queryAnalysisItemAssignmentPage(queryDTO);
        return ResponseInfo.success(result);
    }

    @ApiOperation("分页查询检验单任务列表（检验单分配页签）")
    @PostMapping("/inspection-order-assignment/page")
    public ResponseInfo<CommonPage<InspectionOrderAssignmentDTO>> queryInspectionOrderTaskPage(
            @RequestBody @Valid TaskQueryReqVO reqVO) {
        TaskQueryDTO queryDTO = taskService.setTaskPermission(reqVO);
        // 如果人员没有班组，直接返回
        if (CollectionUtil.isEmpty(queryDTO.getCurrentUserTeamIds())){
            return ResponseInfo.success(CommonPage.CommonPage(Collections.emptyList(), 0L, reqVO));
        }
        CommonPage<InspectionOrderAssignmentDTO> result = taskService.queryInspectionOrderAssignmentPage(queryDTO);
        return ResponseInfo.success(result);
    }

    @ApiOperation("查询任务状态统计")
    @PostMapping("/status-count")
    public ResponseInfo<List<TaskStatusCountDTO>> queryTaskStatusCount(
            @RequestBody @Valid TaskQueryReqVO reqVO) {
        TaskQueryDTO queryDTO = taskService.setTaskPermission(reqVO);
        if (CollectionUtil.isEmpty(queryDTO.getCurrentUserTeamIds())){
            return ResponseInfo.success(CollectionUtil.newArrayList());
        }
        List<TaskStatusCountDTO> result = taskService.queryTaskStatusCount(queryDTO);
        return ResponseInfo.success(result);
    }
    @ApiOperation("分配任务")
    @PostMapping("/assign")
    public ResponseInfo<Void> assignTasks(@RequestBody @Valid TaskAssignReqVO reqVO) {
        TaskAssignDTO assignDTO = new TaskAssignDTO();
        BeanUtils.copyProperties(reqVO, assignDTO);
        taskService.assignTasks(assignDTO);
        return ResponseInfo.success();
    }

    @ApiOperation("领取任务")
    @PostMapping("/claim")
    public ResponseInfo<Void> claimTasks(@RequestBody List<Long> taskIds) {
        taskService.claimTasks(taskIds);
        return ResponseInfo.success();
    }

    @ApiOperation("退回任务")
    @PostMapping("/return")
    public ResponseInfo<Void> returnTasks(@RequestBody @Valid TaskReturnReqVO reqVO) {
        TaskReturnDTO returnDTO = new TaskReturnDTO();
        BeanUtils.copyProperties(reqVO, returnDTO);
        taskService.returnTasks(returnDTO);
        return ResponseInfo.success();
    }

    @ApiOperation("审批退回任务")
    @PostMapping("/approve-return")
    public ResponseInfo<Void> approveReturnTasks(@RequestBody @Valid TaskApprovalReqVO reqVO) {
        TaskApprovalDTO approvalDTO = new TaskApprovalDTO();
        BeanUtils.copyProperties(reqVO, approvalDTO);
        taskService.approveReturnTasks(approvalDTO);
        return ResponseInfo.success();
    }

    @ApiOperation("终止任务")
    @PostMapping("/terminate")
    public ResponseInfo<Void> terminateTasks(@RequestBody @Valid TaskTerminateReqVO reqVO) {
        TaskTerminateDTO terminateDTO = new TaskTerminateDTO();
        BeanUtils.copyProperties(reqVO, terminateDTO);
        taskService.terminateTasks(terminateDTO);
        return ResponseInfo.success();
    }

    @ApiOperation("查询任务状态变更历史")
    @GetMapping("/{taskId}/status-history")
    public ResponseInfo<List<TaskStatusHistoryDTO>> queryTaskStatusHistory(@PathVariable Long taskId) {
        List<TaskStatusHistoryDTO> result = taskService.queryTaskStatusHistory(taskId);
        return ResponseInfo.success(result);
    }

    @ApiOperation("查询可分配的人员列表")
    @PostMapping("/assignable-users")
    public ResponseInfo<List<AssignableUserDTO>> queryAssignableUsers(@RequestBody List<Long> taskIds) {
        List<AssignableUserDTO> result = taskService.queryAssignableUsers(taskIds);
        return ResponseInfo.success(result);
    }
}
