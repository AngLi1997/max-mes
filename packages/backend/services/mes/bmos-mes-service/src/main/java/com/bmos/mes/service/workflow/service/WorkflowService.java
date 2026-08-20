package com.bmos.mes.service.workflow.service;

import com.bmos.mes.service.workflow.change.dto.ChangeTeamDTO;
import com.bmos.mes.service.workflow.dto.AppPlanHistoryDTO;
import com.bmos.mes.service.workflow.dto.BindDeploymentDTO;
import com.bmos.mes.service.workflow.dto.PlanProgressDTO;
import com.bmos.mes.service.workflow.dto.PlanStepDetailQueryDTO;
import com.bmos.mes.service.workflow.dto.query.*;
import com.bmos.mes.service.workflow.vo.*;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.orchestrator.engine.core.command.CreateDeploymentCmd;
import com.bmos.orchestrator.engine.core.command.StartProcessInstanceCmd;
import com.bmos.orchestrator.engine.core.model.TaskInstance;

import java.util.List;
import java.util.Map;

public interface WorkflowService {

    String createDeployment(CreateDeploymentCmd cmd);

    String getProcessModel(String processModelId);

    void bindDeployment(BindDeploymentDTO dto);
    void bindBatchDeployment(String deploymentId, Map<String,String> bindings);

    String startProcessInstance(StartProcessInstanceCmd cmd);

    CommonPage<WorkflowPlanManagePageVO> getPlanManagePage(WorkflowPlanManagePageDTO dto);

    List<WorkflowNodeVO> getWorkflowProcedures(String processInstanceId,Long processVersionId);

    WorkflowStepVO getWorkflowProcedureSteps(WorkFlowProcedureStepDTO stepDTO);

    void validateDeployment(String processModelId);

    void deployBatch(List<String> processModelIds);

    CommonPage<WorkflowPlanHistoryPageVO> getPlanHistoryPage(AppPlanHistoryDTO dto);

    List<WorkflowNodeVO> getWorkflowHistoryProcedures(String processInstanceId,Long processVersionId);

    List<WorkflowNodeVO> getWorkflowHistoryProcedureSteps(WorkFlowProcedureStepDTO stepDTO);

    void activeStep(ActivateStepDTO dto);

    void changeTeam(ChangeTeamDTO teamDTO);

    void coerceActiveStep(CoerceActivateStepDTO stepDto);

    WorkFlowToDoVO getTodoPageFresh(WorkflowTodoPageDTO dto);

    List<ProcedureProgressVO> procedureProgress(String processInstanceId);

    WorkFlowStepProgressVO listStepProgress(WorkFlowStepProgressDTO dto);

    List<StepChangeTeamListVO> listChangeTeam(StepChangeTeamDTO dto);

    /**
     * 查询生产辅助记录
     * @param dto
     * @return
     */
    CommonPage<PlanSubRecordVO> queryPlanSubRecordList(PlanSubRecordQueryDTO dto);
    /**
     * 查询工步及其所属详情信息
     * @param dto
     * @return
     */
    PlanProcedureStepDetailVO getProductionProcedureStepDetailInfo(PlanStepDetailQueryDTO dto);

    CommonPage<WorkflowPlanManagePageVO> getPlanProgressPage(PlanProgressDTO dto);

    void coerceProcedureComplete(WorkFlowProcedureStepDTO stepDTO);

    List<TaskInstance> findByExecutionIdAndProcessInstanceId(String executionId, String processInstanceId);
}
