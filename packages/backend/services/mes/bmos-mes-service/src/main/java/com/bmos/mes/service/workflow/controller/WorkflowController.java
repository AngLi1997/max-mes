package com.bmos.mes.service.workflow.controller;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.workflow.dto.*;
import com.bmos.mes.service.workflow.dto.query.PlanSubRecordQueryDTO;
import com.bmos.mes.service.workflow.vo.PlanSubRecordVO;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceService;
import com.bmos.mes.service.workflow.change.dto.ChangeTeamDTO;
import com.bmos.mes.service.workflow.dto.query.*;
import com.bmos.mes.service.workflow.service.WorkflowExecutor;
import com.bmos.mes.service.workflow.service.WorkflowService;
import com.bmos.mes.service.workflow.vo.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/flow")
@Api(tags = "流程相关接口")
@Validated
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private WorkflowExecutor workflowExecutor;

    @Resource
    private ITaskConditionCalculator taskConditionCalculator;

    @Autowired
    private ProcedureTaskInstanceService taskInstanceService;

    @GetMapping("/model")
    @ApiOperation("查询流程模型")
    public ResponseInfo<String> getProcessModel(String processModelId) {
        return ResponseInfo.success(workflowService.getProcessModel(processModelId));
    }

    @GetMapping("/plan/manage/page")
    @ApiOperation("查询生产管理分页接口")
    public ResponseInfo<CommonPage<WorkflowPlanManagePageVO>> getPlanManagePage(@Validated WorkflowPlanManagePageDTO dto) {
        return ResponseInfo.success(workflowService.getPlanManagePage(dto));
    }

    @GetMapping("/plan/history/page")
    @ApiOperation("生产历史")
    public ResponseInfo<CommonPage<WorkflowPlanHistoryPageVO>> getPlanHistoryPage(@Validated AppPlanHistoryDTO dto) {
        return ResponseInfo.success(workflowService.getPlanHistoryPage(dto));
    }

    @GetMapping("/procedures")
    @ApiOperation("查询工序节点")
    public ResponseInfo<List<WorkflowNodeVO>> getWorkflowProcedures(@NotBlank String processInstanceId,
                                                                    @NotNull Long processVersionId) {
        return ResponseInfo.success(workflowService.getWorkflowProcedures(processInstanceId, processVersionId));
    }

    @GetMapping("/procedures/history")
    @ApiOperation("查询历史工序节点")
    public ResponseInfo<List<WorkflowNodeVO>> getWorkflowHistoryProcedures(@NotBlank String processInstanceId,
                                                                           @NotNull Long processVersionId) {
        return ResponseInfo.success(workflowService.getWorkflowHistoryProcedures(processInstanceId, processVersionId));
    }


    @GetMapping("/steps")
    @ApiOperation("查询工序步骤节点")
    public ResponseInfo<WorkflowStepVO> getWorkflowProcedureSteps(@Validated WorkFlowProcedureStepDTO stepDTO) {
        return ResponseInfo.success(workflowService.getWorkflowProcedureSteps(stepDTO));
    }

    @GetMapping("/steps/history")
    @ApiOperation("查询历史工序步骤节点")
    public ResponseInfo<List<WorkflowNodeVO>> getWorkflowHistoryProcedureSteps(@Validated WorkFlowProcedureStepDTO stepDTO) {
        return ResponseInfo.success(workflowService.getWorkflowHistoryProcedureSteps(stepDTO));
    }

    @GetMapping("/todoPage/fresh")
    @ApiOperation("app执行端分页")
    public ResponseInfo<WorkFlowToDoVO> getTodoPageFresh(@Validated WorkflowTodoPageDTO dto){
        return ResponseInfo.success(workflowService.getTodoPageFresh(dto));
    }

    @PostMapping("/procedure/restart")
    @ApiOperation("工序重做")
    public ResponseInfo<Void> restart(@Validated @RequestBody WorkflowRestartDTO dto) {
        workflowExecutor.restart(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/change/team")
    @ApiOperation("工序换班/工艺换班")
    public ResponseInfo<Void> changeTeam(@Validated @RequestBody ChangeTeamDTO teamDTO){
        workflowService.changeTeam(teamDTO);
        return ResponseInfo.success();
    }

    @PostMapping("/complete/task")
    @ApiOperation("完成任务--待办")
    public ResponseInfo<Void> completeTask(@Validated @RequestBody CompleteTaskDTO dto) {
        workflowExecutor.completeTask(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/complete/execution")
    @ApiOperation("完成执行实例任务--生产管理")
    public ResponseInfo<Boolean> completeTaskByExecution(@Validated @RequestBody CompleteTaskByExecutionDTO dto) {
        return ResponseInfo.success(workflowExecutor.completeTaskByExecution(dto));
    }

    @PostMapping("/terminate/{processInstanceId}")
    @ApiOperation("流程终止")
    public ResponseInfo<Void> terminate(@PathVariable("processInstanceId") String processInstanceId) {
        workflowExecutor.terminate(processInstanceId);
        return ResponseInfo.success();
    }

    @PostMapping("/coerce/active")
    @ApiOperation("强制开始工步或任务")
    public ResponseInfo<Void> coerceActive(@Validated @RequestBody CoerceActivateStepDTO stepDto){
        // 如果是任务进行短路
        if (taskInstanceService.coerceActive(stepDto.getExecutionId(),stepDto.getUserId())) {
            return ResponseInfo.success();
        }
        workflowService.coerceActiveStep(stepDto);
        return ResponseInfo.success();
    }

    @PostMapping("/coerce/procedure/complete")
    @ApiOperation("强制完成工序")
    public ResponseInfo<Void> coerceProcedureComplete(@Validated @RequestBody WorkFlowProcedureStepDTO stepDTO){
        workflowService.coerceProcedureComplete(stepDTO);
        return ResponseInfo.success();
    }

    @PostMapping("/active/step")
    @ApiOperation("激活工步待办")
    public ResponseInfo<Void> activeStep(@Validated @RequestBody ActivateStepDTO stepDto) {
        //执行条件是否满足,满足条件步骤可激活
        Pair<Boolean, List<ProcedureConditionInstance>> calculateTaskOrStepExpression =
                taskConditionCalculator.calculateTaskOrStepExpression(stepDto.getPlanId(), stepDto.getProcedureStepModelId(),
                        ExpressionTypeEnum.EXECUTE_CONDITION);
        if (!calculateTaskOrStepExpression.getLeft()) {
            List<ProcedureConditionInstance> procedureConditionInstances = calculateTaskOrStepExpression.getRight();
            // 筛选出来条件实例执行结果为false的
            String msg = procedureConditionInstances.stream().filter(procedureConditionInstance ->
                    !procedureConditionInstance.getTaskResult()).map(ProcedureConditionInstance::getName).collect(Collectors.joining(","));
            throw new BmosException(MesResponseCode.STEP_ACTIVE_ERROR);
        }
        // 如果是任务进行短路
        if (taskInstanceService.active(stepDto.getExecutionId(), stepDto.getPlanId(), stepDto.getProcedureStepModelId())) {
            return ResponseInfo.success();
        }
        workflowService.activeStep(stepDto);
        return ResponseInfo.success();
    }

    @GetMapping("/procedure/progress")
    @ApiOperation("查询工序生产进度")
    @ApiImplicitParam(value = "processInstanceId",name = "流程实例id",required = true)
    public ResponseInfo<List<ProcedureProgressVO>> procedureProgress(@Validated @NotNull String processInstanceId){
        return ResponseInfo.success(workflowService.procedureProgress(processInstanceId));
    }

    @GetMapping("/plan/progress/page")
    @ApiOperation("生产进度查询")
    public ResponseInfo<CommonPage<WorkflowPlanManagePageVO>> getPlanProgressPage(@Validated PlanProgressDTO dto) {
        return ResponseInfo.success(workflowService.getPlanProgressPage(dto));
    }

    @GetMapping("/subRecordList")
    @ApiOperation("批次辅助记录列表查询")
    public ResponseInfo<CommonPage<PlanSubRecordVO>> queryPlanSubRecordList(@Validated PlanSubRecordQueryDTO dto) {
        return ResponseInfo.success(workflowService.queryPlanSubRecordList(dto));
    }


    @GetMapping("/list/step/progress")
    @ApiOperation("查询工步生产进度")
    public ResponseInfo<WorkFlowStepProgressVO> listStepProgress(@Validated WorkFlowStepProgressDTO dto){
        return ResponseInfo.success(workflowService.listStepProgress(dto));
    }

    @GetMapping("/list/change/team")
    @ApiOperation("查询工步换班信息")
    public ResponseInfo<List<StepChangeTeamListVO>> listChangeTeam(@Validated StepChangeTeamDTO dto){
        return ResponseInfo.success(workflowService.listChangeTeam(dto));
    }

    @GetMapping("/procedure/step/detail")
    @ApiOperation("查询生产中工步详情信息")
    public ResponseInfo<PlanProcedureStepDetailVO> getProductionProcedureStepDetailInfo(@Validated PlanStepDetailQueryDTO dto) {
        return ResponseInfo.success(workflowService.getProductionProcedureStepDetailInfo(dto));
    }


}
