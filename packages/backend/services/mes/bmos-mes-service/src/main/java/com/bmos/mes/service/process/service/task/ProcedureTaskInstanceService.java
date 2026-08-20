package com.bmos.mes.service.process.service.task;

import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstance;
import com.bmos.mes.service.workflow.change.dto.ChangeTeamDTO;
import com.bmos.mes.service.workflow.dto.ProcedureRestartDTO;
import com.bmos.mes.service.workflow.dto.query.WorkFlowProcedureStepDTO;
import com.bmos.mes.service.workflow.dto.query.WorkFlowStepProgressDTO;
import com.bmos.mes.service.workflow.vo.CompleteTaskVO;
import com.bmos.mes.service.workflow.vo.StepChangeTeamListVO;
import com.bmos.mes.service.workflow.vo.TaskProgressVO;
import com.bmos.mes.service.workflow.vo.WorkflowTodoPageVO;

import java.util.List;
import java.util.Map;

public interface ProcedureTaskInstanceService {

    List<ProcedureTaskInstance> selectListByPlanIdAndStepModel(Long plan,
                                                               List<ProcedureStepModel> modelList, Long procedureModelId);

    /**
     * 工序重做or工序换班
     * @param dto
     */
    void restart(ProcedureRestartDTO dto);

    CompleteTaskVO completeTaskByExecution(String executionId);

    /**
     * 初始化任务
     *
     * @param plan 计划
     */
    void initTaskInstance(Plan plan);

    /**
     * @param taskIds 任务id集合
     * @param planId  计划id
     * @return 结果
     */
    List<ProcedureTaskInstance> selectTaskNotComplete(List<Long> taskIds, Long planId);

    /**
     * 激活任务
     *
     * @param taskInstanceId          任务实例id
     * @param planId                  计划id
     * @param getProcedureStepModelId 工步模型id
     * @return 是否已经激活
     */
    boolean active(String taskInstanceId, Long planId, Long getProcedureStepModelId);

    /**
     * 强制激活任务
     *
     * @param taskInstanceId 任务实例id
     * @param coerceUser     强制激活人
     * @return 是否已经激活
     */
    boolean coerceActive(String taskInstanceId, String coerceUser);

    /**
     * 终止流程同时终止任务
     *
     * @param processInstanceId
     */
    void terminate(String processInstanceId);

    /**
     * 工艺换班任务操作
     *
     * @param plan 当前计划
     */
    void changeTeamProcess(Plan plan,Integer processChangeNumber);

    List<WorkflowTodoPageVO> queryTodoFresh(List<Plan> planIds,String userId);

    List<ProcedureTaskInstance> selectChangeListByPlanIdAndStepModeId(WorkFlowProcedureStepDTO stepDTO, List<ProcedureStepModel> stepModels);

    /**
     * 查询任务生产进度数据
     *
     * @param dto 参数
     * @return
     */
    List<TaskProgressVO> listTaskProgress(WorkFlowStepProgressDTO dto);

    List<StepChangeTeamListVO> queryChangeTeamListByStepModelIdAndPlanId(Long procedureStepModelId, Long planId);

    List<ProcedureTaskInstance> selectTask(ChangeTeamDTO teamDTO);

    List<ProcedureTaskInstance> queryCompleteTaskByPlanId(Long planId);
}
