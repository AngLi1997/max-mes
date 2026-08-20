package com.bmos.mes.service.process.service.impl.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.constant.ProcessConstant;
import com.bmos.mes.common.enums.plan.ProductTaskStatusEnum;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.dto.SubRecordNodeQueryDTO;
import com.bmos.mes.service.execute.mapper.ExecuteSubsidiaryRecordMapper;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.convert.Task.ProcessTaskConverter;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.mapper.task.ProcedureTaskInstanceMapper;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstance;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstanceHistory;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.condition.event.ProcedureRestartType;
import com.bmos.mes.service.process.service.condition.event.TaskCompleteType;
import com.bmos.mes.service.process.service.task.ProcedureConditionInstanceHistoryService;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceHistoryService;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceService;
import com.bmos.mes.service.unit.service.UnitService;
import com.bmos.mes.service.utils.ChangeTeamUtils;
import com.bmos.mes.service.workflow.change.dto.ChangeTeamDTO;
import com.bmos.mes.service.workflow.change.vo.TeamListVO;
import com.bmos.mes.service.workflow.dto.ProcedureRestartDTO;
import com.bmos.mes.service.workflow.dto.query.TaskTodoPageDTO;
import com.bmos.mes.service.workflow.dto.query.WorkFlowProcedureStepDTO;
import com.bmos.mes.service.workflow.dto.query.WorkFlowStepProgressDTO;
import com.bmos.mes.service.workflow.service.impl.WorkflowServiceImpl;
import com.bmos.mes.service.workflow.vo.*;
import com.bmos.orchestrator.engine.core.command.PauseEndCmd;
import com.bmos.orchestrator.engine.core.model.ProcessInstance;
import com.bmos.orchestrator.engine.core.service.ProcessInstanceService;
import com.bmos.orchestrator.engine.core.service.TaskInstanceService;
import com.bmos.orchestrator.engine.core.state.ProcessState;
import org.apache.commons.lang3.tuple.Pair;
import org.docx4j.wml.P;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

@Service
public class ProcedureTaskInstanceServiceImpl implements ProcedureTaskInstanceService {

    @Autowired
    private ProcedureTaskInstanceMapper instanceMapper;


    @Autowired
    private ProcedureStepModelMapper stepModelMapper;

    @Autowired
    private ITaskConditionCalculator conditionChangeHandler;

    @Autowired
    private ProcessInstanceService instanceService;

    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private ProcedureTaskInstanceHistoryService taskInstanceHistoryService;

    @Autowired
    private ProcedureConditionInstanceHistoryService conditionInstanceHistoryService;
    @Resource
    private ExecuteSubsidiaryRecordMapper executeSubsidiaryRecordMapper;

    @Resource
    private TaskInstanceService taskInstanceService;

    @Resource
    @Lazy
    private ProcedureModelService modelService;

    @Resource
    @Lazy
    private WorkflowServiceImpl service;

    @Override
    public List<ProcedureTaskInstance> selectListByPlanIdAndStepModel(Long planId,
                                                                      List<ProcedureStepModel> stepModelIds, Long procedureModelId) {
        if (CollUtil.isEmpty(stepModelIds)) {
            return Collections.emptyList();
        }
        List<Long> modelIds = stepModelIds.stream().map(ProcedureStepModel::getId).collect(Collectors.toList());
        return instanceMapper.selectListByPlanIdAndStepModel(planId, modelIds, procedureModelId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restart(ProcedureRestartDTO dto) {
        List<ProcedureTaskInstance> taskInstances = instanceMapper.queryByProcedureModelIdAndPlanId(dto);
        if (CollUtil.isNotEmpty(taskInstances)) {
            handlerTask(taskInstances);
            //根据stepModelId去重，因为任务可以重复做
            taskInstances = taskInstances.stream().collect(
                    Collectors.collectingAndThen(
                            toCollection(() -> new TreeSet<>(Comparator.comparing(ProcedureTaskInstance::getProcedureStepModelId))),
                            ArrayList::new));
            //初始化条件
            taskInstances.forEach(item -> {
                item.setId(null);
                item.setCompleteTime(null);
                item.setFlowState(ProductTaskStatusEnum.DISABLE.getValue());
                item.setType(ProcessConstant.restart);
                item.setCreateTime(LocalDateTime.now());
                item.setActiveTime(null);
                item.setPauseTag(null);
                //工序换班
                if (dto.getIsChangeTeam()) {
                    item.setType(ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue());
                    item.setProcedureChangeNumber(ObjectUtil.isNull(item.getProcedureChangeNumber()) ? 1 : item.getProcedureChangeNumber() + 1);
                }
            });
            instanceMapper.insertBatch(taskInstances);
        }
        //工序换班或者工序重做刷新的为当前工序的所有条件
        Plan plan = planMapper.selectById(dto.getPlanId());
        List<ProcedureStepModel> stepModelList = stepModelMapper.queryModelListByProcedureModeIdAndProcessId(dto.getProcedureModelId(),
                plan.getProcessId(), plan.getProcessVersion());
        conditionChangeHandler.refreshConditionResult(new ProcedureRestartType(dto.getPlanId(), CollectionUtils.convertList(stepModelList, ProcedureStepModel::getId)));
    }

    /**
     * 工序换班、工艺换班、工序重做处理任务
     *
     * @param taskInstances
     */
    private void handlerTask(List<ProcedureTaskInstance> taskInstances) {
        //找到已激活的并且未完成的任务并完成任务
        List<ProcedureTaskInstance> activatedTask = CollectionUtils.filterList(taskInstances, item ->
                StrUtil.equals(ProductTaskStatusEnum.ACTIVATED.getValue(), item.getFlowState())
                        && !StrUtil.equals(ProductTaskStatusEnum.COMPLETE.getValue(), item.getFlowState()));
        instanceMapper.updateActivatedTask(activatedTask);
        //未激活的任务并删除
        List<Long> ids = CollectionUtils.filterList(taskInstances, item ->
                StrUtil.equals(ProductTaskStatusEnum.DISABLE.getValue(), item.getFlowState()) ||
                StrUtil.equals(ProductTaskStatusEnum.ENABLE.getValue(), item.getFlowState()))
                .stream().map(ProcedureTaskInstance::getId).collect(Collectors.toList());
        instanceMapper.deleteInstanceByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompleteTaskVO completeTaskByExecution(String executionId) {
        try {
            CompleteTaskVO vo = new CompleteTaskVO();
            vo.setStartPauseProcedure(false);
            vo.setCompleteTask(true);
            ProcedureTaskInstance instance = instanceMapper.selectById(executionId);
            if (ObjectUtil.isEmpty(instance)) {
                vo.setCompleteTask(false);
                return vo;
            }
            if (instance.getFlowState().equals(ProductTaskStatusEnum.COMPLETE.getValue())){
                throw new BmosException(MesResponseCode.STEP_MODLE_COMPLETE_ERROR);
            }
            instance.setFlowState(ProductTaskStatusEnum.COMPLETE.getValue());
            instance.setCompleteTime(LocalDateTime.now());
            instanceMapper.saveOrUpdateEntity(instance);
            // 处理辅助记录的节点
            executeSubsidiaryRecordMapper.completeSubRecordNode(SubRecordNodeQueryDTO.builder()
                    .productPlanId(instance.getPlanId())
                    .procedureChangeNumber(instance.getProcedureChangeNumber())
                    .processChangeNumber(instance.getProcessChangeNumber())
                    .procedureStepModelId(instance.getProcedureStepModelId())
                    .build(), SysUserHolder.getUser().getUserId());
            //发出任务完成结束事件
            TaskCompleteType taskCompleteType = new TaskCompleteType(instance.getPlanId(),
                    instance.getProcedureStepModelId());
            //判断计划的工艺流程是否已经全部完成
            ProcessInstance processInstance = instanceService.findProcessInstanceByInstanceId(instance.getProcessInstanceId());
            if (ObjectUtil.isNotEmpty(processInstance) && !processInstance.getProcessState().equals(ProcessState.COMPLETE.getState())) {
                conditionChangeHandler.refreshConditionResult(taskCompleteType);
                //判断工序条件是否全部完成
                if (StrUtil.isNotBlank(instance.getPauseTag())){
                    Pair<Boolean, List<ProcedureConditionInstance>> pair = conditionChangeHandler.calculateProcedureModelExpression(instance.getPlanId(), instance.getProcedureModelId(),
                            instance.getProcedureChangeNumber(), instance.getProcessChangeNumber());
                    if (pair.getLeft()){
                        //开始暂停的任务
                        taskInstanceService.endPause(this.buildEndPauseStartCmd(instance));
                        vo.setStartPauseProcedure(true);
                    }
                }
                return vo;
            }
            //工艺流程结束后迁移历史
            transferCompletedIntoHistory(instance.getPlanId());
            return vo;
        } catch (Exception e) {
            throw new BmosException(MesResponseCode.TASK_COMPLETE_ERROR);
        }
    }

    private PauseEndCmd buildEndPauseStartCmd(ProcedureTaskInstance instance){
        ProcedureModel procedureModel = modelService.getById(instance.getProcedureModelId());
        PauseEndCmd cmd = new PauseEndCmd();
        cmd.setElementKey(procedureModel.getNodeId());
        cmd.setProcessInstanceId(instance.getProcessInstanceId());
        cmd.setProcedureChangeNumber(instance.getProcedureChangeNumber());
        cmd.setProcessChangeNumber(instance.getProcessChangeNumber());
        return cmd;
    }

    /**
     * 迁移历史表数据
     *
     * @param planId 计划id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferCompletedIntoHistory(Long planId) {
        //找到任务当前计划下的所有任务
        List<ProcedureTaskInstance> taskInstances = instanceMapper.selectListByPlanId(planId);
        if (CollUtil.isNotEmpty(taskInstances)) {
            //完成任务
            taskInstances.forEach(taskInstance -> {
                if (!StrUtil.equals(taskInstance.getFlowState(), ProductTaskStatusEnum.ACTIVATED.getValue())) {
                    if (!StrUtil.equals(taskInstance.getFlowState(),ProductTaskStatusEnum.COMPLETE.getValue())){
                    taskInstance.setDeleted(true);
                    }
                    return;
                }
                taskInstance.setFlowState(ProductTaskStatusEnum.COMPLETE.getValue());
                taskInstance.setType(ProcessConstant.IS_END);
                taskInstance.setCompleteTime(LocalDateTime.now());
            });
            instanceMapper.saveOrUpdateBatch(taskInstances);
            taskInstanceHistoryService.save(taskInstances);
        }
        //删除任务
        instanceMapper.deleteInstanceByIds(CollectionUtils.convertList(taskInstances,
                ProcedureTaskInstance::getId));
        //迁移条件
        Plan plan = planMapper.selectById(planId);
        //根据工艺版本以及工艺id找到所有stepModel
        List<ProcedureStepModel> stepModelList = stepModelMapper.getStepModelByProcessIdAndVersion(plan.getProcessId(), plan.getProcessVersion());
        //找到所有的工序
        List<ProcedureModel> models = modelService.getByProcessIdAndVersion(plan.getProcessId(), plan.getProcessVersion());
        conditionInstanceHistoryService.saveConditionHistory(CollectionUtils.convertList(stepModelList
                , ProcedureStepModel::getId), planId,CollectionUtils.convertList(models,ProcedureModel::getId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initTaskInstance(Plan plan) {
        //任务和步骤共用模型配置
        List<ProcedureStepModel> stepModels = stepModelMapper.getStepModelByProcessIdAndVersion(plan.getProcessId(),
                plan.getProcessVersion());
        // 先初始化条件
        List<ProcedureStepModel> taskStepModel =
                stepModels.stream().filter(item -> item.getStepType() == StepTaskTypeEnum.TASK).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(taskStepModel)) {
            this.initTaskEnableStatus(taskStepModel, plan);
        }
    }

    private void initTaskEnableStatus(List<ProcedureStepModel> tasks,
                                      Plan plan) {
        // 初始化任务
        List<ProcedureTaskInstance> procedureTaskInstances = ProcessTaskConverter.INSTANCE.convertInstanceId(tasks,
                plan);
        procedureTaskInstances.forEach(item -> item.setFlowState(ProductTaskStatusEnum.DISABLE.getValue()));
        instanceMapper.insertBatch(procedureTaskInstances);
    }

    @Override
    public List<ProcedureTaskInstance> selectTaskNotComplete(List<Long> stepModeIds, Long planId) {
        return instanceMapper.selectTaskNotCompleteByTaskIds(planId);
    }

    /**
     * 激活任务
     *
     * @param taskInstanceId          任务实例id
     * @param planId                  计划id
     * @param getProcedureStepModelId 工步模型id
     * @return 是否已经激活
     */
    @Override
    public boolean active(String taskInstanceId, Long planId, Long getProcedureStepModelId) {
        ProcedureTaskInstance taskInstance = instanceMapper.selectById(taskInstanceId);
        if (ObjectUtil.isNull(taskInstance)) {
            //查找历史
            ProcedureTaskInstanceHistory history = taskInstanceHistoryService.selectHistoryTaskById(taskInstanceId);
            if (ObjectUtil.isNotEmpty(history)){
                throw new BmosException(MesResponseCode.PLAN_END_ERROR);
            }
            return false;
        }
        // 设置实例状态为已激活
        taskInstance.setFlowState(ProductTaskStatusEnum.ACTIVATED.getValue());
        taskInstance.setActiveTime(LocalDateTime.now());
        instanceMapper.updateById(taskInstance);
        return true;
    }

    /**
     * 激活任务
     *
     * @param taskInstanceId 任务实例id
     * @param coerceUser     激活人
     * @return 是否已经激活
     */
    @Override
    public boolean coerceActive(String taskInstanceId, String coerceUser) {
        ProcedureTaskInstance taskInstance = instanceMapper.selectById(taskInstanceId);
        if (ObjectUtil.isNull(taskInstance)) {
            return false;
        }
        // 设置实例状态为已激活
        taskInstance.setFlowState(ProductTaskStatusEnum.ACTIVATED.getValue());
        taskInstance.setType(ProcessConstant.COERCE_ACTIVE);
        taskInstance.setCoerceUser(coerceUser);
        taskInstance.setCoerceTime(LocalDateTime.now());
        taskInstance.setActiveTime(LocalDateTime.now());
        instanceMapper.updateById(taskInstance);
        return true;
    }

    @Override
    public void terminate(String processInstanceId) {
        //根据流程实例id找到所有计划id
        Plan plan = planMapper.selectByExecuteProcessInstanceId(processInstanceId);
        transferCompletedIntoHistory(plan.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeTeamProcess(Plan plan, Integer processChangeNumber) {
        //找当当前计划下的所有任务
        List<ProcedureTaskInstance> taskInstances = instanceMapper.selectListByPlanId(plan.getId());
        if (CollUtil.isEmpty(taskInstances)) {
            return;
        }
        handlerTask(taskInstances);
        //根据stepModelId去重，因为任务可以重复做
        taskInstances = taskInstances.stream().collect(
                Collectors.collectingAndThen(
                        toCollection(() -> new TreeSet<>(Comparator.comparing(ProcedureTaskInstance::getProcedureStepModelId))),
                        ArrayList::new));
        //任务重新创建
        taskInstances.forEach(item -> {
            item.setId(null);
            item.setCompleteTime(null);
            item.setFlowState(ProductTaskStatusEnum.DISABLE.getValue());
            item.setType(ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue());
            item.setProcessChangeNumber(processChangeNumber);
            item.setProcedureChangeNumber(0);
            item.setActiveTime(null);
            item.setPauseTag(null);
        });
        instanceMapper.saveOrUpdateBatch(taskInstances);
        //工艺换班找到当前工艺版本下的所有任务进行刷新
        List<ProcedureStepModel> stepModelList = stepModelMapper.getStepModelByProcessIdAndVersion(plan.getProcessId(), plan.getProcessVersion());
        List<Long> stepModelIdList = CollectionUtils.convertList(stepModelList, ProcedureStepModel::getId);
        conditionChangeHandler.refreshConditionResult(new ProcedureRestartType(plan.getId(), stepModelIdList));
    }

    @Override
    public List<WorkflowTodoPageVO> queryTodoFresh(List<Plan> plan,String userId) {
        //查询所有待办任务
        List<ProcedureTaskInstance> taskTodoPage = instanceMapper.getTaskTodoPage(CollectionUtils.convertList(plan,Plan::getId));
        //判断权限
        List<Long> planIdList = CollectionUtils.convertList(taskTodoPage, ProcedureTaskInstance::getPlanId);
        if (!AdminUtil.isAdminUser(userId)) {
            List<TeamListVO> team = ChangeTeamUtils.getTeam(planIdList,userId);
            if (CollUtil.isEmpty(team)) {
                return new ArrayList<>();
            }
            Map<Long, Set<Long>> teamMap = team.stream().collect(
                    Collectors.groupingBy(TeamListVO::getProductPlanId,
                            Collectors.mapping(TeamListVO::getProcedureStepModelId, Collectors.toSet())));
            //筛选权限
            taskTodoPage = CollectionUtils.filterList(taskTodoPage,item->
                    CollUtil.isNotEmpty(teamMap.get(item.getPlanId())) && teamMap.get(item.getPlanId()).contains(item.getProcedureStepModelId()));
        }
        if (CollUtil.isEmpty(taskTodoPage)) {
            return new ArrayList<>();
        }
        Set<Long> planIds = CollectionUtils.convertSet(taskTodoPage, ProcedureTaskInstance::getPlanId);
        List<Plan> plans = CollectionUtils.filterList(plan,item->planIds.contains(item.getId()));
        List<Long> procedureStepModeIds = CollectionUtils.convertList(taskTodoPage,
                ProcedureTaskInstance::getProcedureStepModelId);
        List<ProcedureStepDurationVO> durationList =
                stepModelMapper.selectDurationByStepModeIds(procedureStepModeIds);
        Map<Long, ProcedureStepDurationVO> procedureStepMap = CollectionUtils.convertMap(durationList,
                ProcedureStepDurationVO::getProcedureStepModelId);
        List<WorkflowTodoPageVO> list = ProcessTaskConverter.INSTANCE.convertToToDoList(taskTodoPage, plans, procedureStepMap);
        return list;
    }

    @Override
    public List<ProcedureTaskInstance> selectChangeListByPlanIdAndStepModeId(WorkFlowProcedureStepDTO stepDTO, List<ProcedureStepModel> stepModels) {
        if (CollUtil.isEmpty(stepModels)) {
            return Collections.emptyList();
        }
        List<Long> modelIds = stepModels.stream().map(ProcedureStepModel::getId).collect(Collectors.toList());
        return instanceMapper.selectChangeListByPlanIdAndStepModeId(stepDTO, modelIds);
    }

    @Override
    public List<TaskProgressVO> listTaskProgress(WorkFlowStepProgressDTO dto) {
        List<ProcedureTaskInstance> taskInstances = instanceMapper.listTaskProgress(dto);
        if (CollUtil.isEmpty(taskInstances)){
            return Collections.emptyList();
        }
        return ProcessTaskConverter.INSTANCE.convertToVos(taskInstances);
    }

    @Override
    public List<StepChangeTeamListVO> queryChangeTeamListByStepModelIdAndPlanId(Long procedureStepModelId, Long planId) {
        List<ProcedureTaskInstance> taskInstances = instanceMapper.queryChangeTeamListByStepModelIdAndPlanId(procedureStepModelId, planId);
        if (CollUtil.isEmpty(taskInstances)){
            return Collections.emptyList();
        }
        return taskInstances.stream().map(item->{
            StepChangeTeamListVO vo = new StepChangeTeamListVO();
            vo.setCompleteBy(item.getUpdateBy());
            vo.setEndTime(Optional.ofNullable(item.getCompleteTime()).orElse(null));
            vo.setProcedureChangeNumber(item.getProcedureChangeNumber() + 1);
            vo.setProcessChangeNumber(item.getProcessChangeNumber() + 1);
            vo.setStartTime(item.getActiveTime());
            vo.setState(item.getState());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProcedureTaskInstance> selectTask(ChangeTeamDTO teamDTO) {
        return instanceMapper.selectTask(teamDTO);
    }

    @Override
    public List<ProcedureTaskInstance> queryCompleteTaskByPlanId(Long planId) {
        return instanceMapper.queryCompleteTaskByPlanId(planId,ProductTaskStatusEnum.COMPLETE.getValue());
    }
}
