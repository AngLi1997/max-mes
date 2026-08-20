package com.bmos.mes.service.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.AdminUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.TimeUnitEnum;
import com.bmos.mes.common.enums.audit.FlowToDoTypeEnum;
import com.bmos.mes.common.enums.plan.ProductPlanStartEnum;
import com.bmos.mes.common.enums.plan.ProductTaskStatusEnum;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.ProcessStateEnum;
import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.config.minio.MinioProperties;
import com.bmos.mes.service.execute.mapper.ExecuteSubsidiaryRecordMapper;
import com.bmos.mes.service.execute.model.ExecuteSubsidiaryRecord;
import com.bmos.mes.service.plan.info.dto.PlanPageDTO;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.info.vo.PlanPageVO;
import com.bmos.mes.service.plan.info.vo.PlanStartVO;
import com.bmos.mes.service.plan.production.service.ProductionPlanItemService;
import com.bmos.mes.service.plan.production.vo.ProcedureDetailVO;
import com.bmos.mes.service.plan.team.model.InstructionTeam;
import com.bmos.mes.service.plan.team.service.ProductPlanTeamService;
import com.bmos.mes.service.plan.template.dto.PlanTemplateProcedureConfigDTO;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.convert.Task.ProcessTaskConverter;
import com.bmos.mes.service.process.mapper.ProcedureModelMapper;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstance;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstanceHistory;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.task.ProcedureConditionInstanceService;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceHistoryService;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceService;
import com.bmos.mes.service.utils.ChangeTeamUtils;
import com.bmos.mes.service.workflow.change.dto.ChangeTeamDTO;
import com.bmos.mes.service.workflow.change.dto.TeamListDTO;
import com.bmos.mes.service.workflow.change.execute.ChangeTeamContext;
import com.bmos.mes.service.workflow.change.execute.ChangeTeamFactory;
import com.bmos.mes.service.workflow.change.service.ProductChangeTeamService;
import com.bmos.mes.service.workflow.change.vo.TeamListVO;
import com.bmos.mes.service.workflow.convert.WorkflowConverter;
import com.bmos.mes.service.workflow.dto.AppPlanHistoryDTO;
import com.bmos.mes.service.workflow.dto.BindDeploymentDTO;
import com.bmos.mes.service.workflow.dto.PlanProgressDTO;
import com.bmos.mes.service.workflow.dto.PlanStepDetailQueryDTO;
import com.bmos.mes.service.workflow.dto.query.*;
import com.bmos.mes.service.workflow.enums.WorkflowType;
import com.bmos.mes.service.workflow.service.WorkflowService;
import com.bmos.mes.service.workflow.vo.*;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.orchestrator.engine.core.command.*;
import com.bmos.orchestrator.engine.core.element.enums.ElementTypeEnum;
import com.bmos.orchestrator.engine.core.model.Deployment;
import com.bmos.orchestrator.engine.core.model.ExecutionInstance;
import com.bmos.orchestrator.engine.core.model.ProcessInstance;
import com.bmos.orchestrator.engine.core.model.TaskInstance;
import com.bmos.orchestrator.engine.core.query.cmd.*;
import com.bmos.orchestrator.engine.core.query.resp.MultiTaskQueryResp;
import com.bmos.orchestrator.engine.core.query.service.DeploymentQueryService;
import com.bmos.orchestrator.engine.core.query.service.ExecutionQueryService;
import com.bmos.orchestrator.engine.core.query.service.TaskQueryService;
import com.bmos.orchestrator.engine.core.service.DeploymentService;
import com.bmos.orchestrator.engine.core.service.ExecutionService;
import com.bmos.orchestrator.engine.core.service.ProcessInstanceService;
import com.bmos.orchestrator.engine.core.service.TaskInstanceService;
import com.bmos.orchestrator.engine.core.state.ProcessState;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryLineDetailFeignVO;
import com.bmos.platform.facade.system.role.feign.RoleFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private DeploymentService deploymentService;

    @Autowired
    private ProcessInstanceService processInstanceService;

    @Autowired
    private DeploymentQueryService deploymentQueryService;

    @Autowired
    private ExecutionQueryService executionQueryService;

    @Autowired
    private TaskQueryService taskQueryService;

    @Autowired
    private ExecutionService executionService;

    @Autowired
    @Lazy
    private PlanService planService;

    @Autowired
    private ProductPlanTeamService productPlanTeamService;

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private ProcedureModelMapper procedureModelMapper;


    @Autowired
    private ProcedureTaskInstanceService taskInstanceService;

    @Autowired
    private ProcedureTaskInstanceHistoryService taskInstanceHistoryService;

    @Resource
    private ProductChangeTeamService changeTeamService;

    @Resource
    private ITaskConditionCalculator taskConditionCalculator;

    @Resource
    private ExecuteSubsidiaryRecordMapper executeSubsidiaryRecordMapper;

    @Resource
    private ProductionPlanItemService itemService;

    @Autowired
    private MinioProperties minioProperties;

    @Resource
    private WorkflowExecutorImpl workflowExecutor;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private TaskInstanceService instanceService;

    @Autowired
    @Lazy
    private ProcessService processService;

    @Resource
    private RoleFeign roleFeign;

    private static final Executor todoQueryExecutor=new ThreadPoolExecutor(10, 40, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(300),
            new ThreadFactoryBuilder().setNamePrefix("todo-query-thread").build(), new ThreadPoolExecutor.AbortPolicy());

    private static final Executor todoWorkFlowQueryExecutor=new ThreadPoolExecutor(10, 40, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(300),
            new ThreadFactoryBuilder().setNamePrefix("todo-work-query-thread").build(), new ThreadPoolExecutor.AbortPolicy());

    private static final Executor taskInstanceQueryExecutor=new ThreadPoolExecutor(10, 40, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(300),
            new ThreadFactoryBuilder().setNamePrefix("todo-task-query-thread").build(), new ThreadPoolExecutor.AbortPolicy());




    @Resource
    private ProcedureConditionInstanceService conditionInstanceService;


    @Override
    public String createDeployment(CreateDeploymentCmd cmd) {
        return deploymentService.createDeployment(cmd, false);
    }

    @Override
    public String getProcessModel(String processModelId) {
        Deployment deployment = deploymentQueryService.findByDeploymentId(processModelId);
        return ObjectUtil.isEmpty(deployment) ? JsonUtils.toJsonString(Collections.emptyList()) : deployment.getMetaInfo();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindDeployment(BindDeploymentDTO dto) {
        BindCallActivityDeploymentCmd cmd = new BindCallActivityDeploymentCmd();
        cmd.setDeploymentId(dto.getSuperDeploymentId());
        cmd.setSubProcessDeploymentId(dto.getCurrentDeploymentId());
        cmd.setElementKey(dto.getNodeId());
        deploymentService.bindCallActivityDeployment(cmd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindBatchDeployment(String deploymentId, Map<String, String> bindings) {
        BindCallActivitiesDeploymentCmd cmd = new BindCallActivitiesDeploymentCmd();
        cmd.setDeploymentId(deploymentId);
        cmd.setBindings(bindings);
        deploymentService.bindCallActivityDeployment(cmd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String startProcessInstance(StartProcessInstanceCmd cmd) {
        return processInstanceService.startProcessInstance(cmd);
    }

    @Override
    public CommonPage<WorkflowPlanManagePageVO> getPlanManagePage(WorkflowPlanManagePageDTO dto) {
        PlanPageDTO pageDTO = dto.convert2PlanPageDTO();
        //处理班组权限
        BasePage page = new BasePage();
        page.setPageNum(dto.getPageNum());
        page.setPageSize(dto.getPageSize());
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<InstructionTeam> teamList = ChangeTeamUtils.getInstructionDetailByUserTeamId();
            if (CollUtil.isEmpty(teamList)){
                return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
            }
            pageDTO.setTeamPlanIdList(CollectionUtils.convertSet(teamList,InstructionTeam::getProductPlanId));
        }
        return this.getPlanData(pageDTO,page);
    }

    /**
     * 查询生产进度以及生产管理数据
     * @param pageDTO
     * @param page
     * @return
     */
    private CommonPage<WorkflowPlanManagePageVO> getPlanData(PlanPageDTO pageDTO,BasePage page){
        List<Plan> plans = planService.productManagePage(pageDTO);
        if (CollUtil.isEmpty(plans)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
        }
        this.savePlanLinedName(plans);
        ActiveExecutionQueryCmd cmd = new ActiveExecutionQueryCmd();
        cmd.setBusinessKeys(CollectionUtils.convertList( plans,e -> String.valueOf(e.getId())));
        cmd.setCategory(WorkflowType.PROCEDURE.name());
        cmd.setElementType(ElementTypeEnum.CALL_ACTIVITY_TASK.getType());
        List<ExecutionInstance> executions = executionQueryService.findByActiveExecutionQueryCmd(cmd);
        Map<String, String> executionName = executions.stream()
                .collect(Collectors.groupingBy(ExecutionInstance::getProcessInstanceId,
                        Collectors.mapping(ExecutionInstance::getElementName, Collectors.joining(","))));
        return CommonPage.convertPage(plans, e -> WorkflowConverter.INSTANCE.convertPage(e, executionName));
    }

    @Override
    public List<WorkflowNodeVO> getWorkflowProcedures(String processInstanceId, Long processVersionId) {
        ExecutionQueryCmd cmd = new ExecutionQueryCmd();
        cmd.setProcessInstanceId(processInstanceId);
        cmd.setElementType(ElementTypeEnum.CALL_ACTIVITY_TASK.getType());
        //查询工作流流程节点数据
        List<ExecutionInstance> executions = executionQueryService.findByExecutionQueryCmd(cmd);
        Plan plan = planService.selectByExecuteProcessInstanceId(processInstanceId);
        //找到当前所有工序节点
        List<ProcedureModel> modelList = procedureModelMapper.selectByProcessIdAndVersion(plan.getProcessId(), plan.getProcessVersion());
        //构建工作流未流转到节点的工序信息
        List<ExecutionInstance> executionInstances = addNotActiveProcedure(executions, modelList, plan.getId());
        if (CollUtil.isEmpty(executionInstances)) {
            return null;
        }
        return WorkflowConverter.INSTANCE.convertProcedureVO(executionInstances, plan.getId(), modelList);
    }

    /**
     * 处理运行中的工序节点
     *
     * @param executions      总流程
     * @param activeExecution 运行中的流程
     * @param modelList       工序模型信息
     */
    public void handleActiveProcedure(List<ExecutionInstance> executions, List<ExecutionInstance> activeExecution, List<ProcedureModel> modelList) {
        ExecutionInstance executionInstance = CollectionUtils.getFirst(activeExecution);
        Integer processChangeNumber = executionInstance.getProcessChangeNumber();
        Integer procedureChangeNumber = executionInstance.getProcedureChangeNumber();
        String changeType = executionInstance.getChangeType();
        List<String> nodeIdList = CollectionUtils.convertList(executions, ExecutionInstance::getElementKey, item -> item.getProcessChangeNumber().equals(processChangeNumber));
        //未激活的工序信息
        List<ProcedureModel> notActiveProcedure = CollectionUtils.filterList(modelList, model -> !nodeIdList.contains(model.getNodeId()));
        //处理未激活的工序
        if (CollUtil.isNotEmpty(notActiveProcedure)) {
            ExecutionInstance instance = CollectionUtils.getFirst(executions);
            List<ExecutionInstance> notActiveExecutionList = notActiveProcedure.stream().map(item -> {
                ExecutionInstance notActiveExecution = WorkflowConverter.INSTANCE.convertToExecution(item);
                notActiveExecution.setState(ProcessState.INACTIVE.getState());
                notActiveExecution.setProcessChangeNumber(StrUtil.isBlank(changeType) || changeType.equals(ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue())
                        ? 0 : processChangeNumber);
                notActiveExecution.setProcedureChangeNumber(StrUtil.isBlank(changeType) || changeType.equals(ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue())
                        ? 0 : procedureChangeNumber);
                notActiveExecution.setChangeType(changeType);
                notActiveExecution.setProcessInstanceId(instance.getProcessInstanceId());
                return notActiveExecution;
            }).collect(Collectors.toList());
            executions.addAll(notActiveExecutionList);
        }
    }

    /**
     * @Author: Ren Jin Guang
     * @Description: 处理工序节点信息以及数据权限
     * @param: executions 工作流节点信息
     * @param: modelList 工序模型信息
     * @param: planId 生产计划id
     * @Date: 2024-08-12 15:21:19
     */
    public List<ExecutionInstance> addNotActiveProcedure(List<ExecutionInstance> executions, List<ProcedureModel> modelList, Long planId) {
        if (CollUtil.isEmpty(executions)) {
            return Collections.emptyList();
        }
        //找到当前运行节点的工艺换班次数
        List<ExecutionInstance> activeExecution = CollectionUtils.filterList(executions, item -> item.getState().equals(ProcessState.ACTIVE.getState()));
        //生产管理处理工序数据
        Map<String, Map<Integer, List<ExecutionInstance>>> collect = executions.stream().collect(Collectors.groupingBy(ExecutionInstance::getElementKey,
                Collectors.groupingBy(ExecutionInstance::getProcessChangeNumber)));
        List<ProcedureTaskInstance> taskInstances = new ArrayList<>();
        if (CollUtil.isNotEmpty(activeExecution)) {
            handleActiveProcedure(executions, activeExecution, modelList);
            //找到当前完成的任务
            taskInstances.addAll(taskInstanceService.queryCompleteTaskByPlanId(planId));
        } else {
            //生产历史
            List<ProcedureTaskInstanceHistory> taskInstanceHistories = taskInstanceHistoryService.queryListPlanIdAndCompleteState(planId,ProductTaskStatusEnum.COMPLETE.getValue());
            taskInstances.addAll(BeanUtil.copyToList(taskInstanceHistories, ProcedureTaskInstance.class));
        }
        if (CollUtil.isNotEmpty(taskInstances)) {
            taskInstances.forEach(task -> {
                //完成的任务属于当前运行的任务
                if (CollUtil.isNotEmpty(activeExecution) && CollectionUtils.getFirst(activeExecution).getProcessChangeNumber().equals(task.getProcessChangeNumber())) {
                    return;
                }
                Map<Integer, List<ExecutionInstance>> map = collect.get(task.getNodeId());
                //处理任务已完成未激活的任务
                if ((CollUtil.isEmpty(map) || CollUtil.isEmpty(map.get(task.getProcessChangeNumber())))) {
                    ExecutionInstance instance = new ExecutionInstance();
                    instance.setEndTime(task.getCompleteTime());
                    instance.setState(ObjectUtil.isNotEmpty(activeExecution) ? task.getState() : ProcessStateEnum.IS_END.getValue());
                    instance.setElementName(task.getProcedureName());
                    instance.setElementKey(task.getNodeId());
                    instance.setProcedureChangeNumber(task.getProcedureChangeNumber());
                    instance.setProcessChangeNumber(task.getProcessChangeNumber());
                    instance.setExecutionId(String.valueOf(task.getId()));
                    instance.setChangeType(task.getType());
                    executions.add(instance);
                }
            });
        }
        //处理数据权限
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<TeamListVO> team = ChangeTeamUtils.getHistoryChangeTeam(planId);
            if (CollUtil.isEmpty(team)) {
                return Collections.emptyList();
            }
            return WorkflowConverter.INSTANCE.convertToFreshExecution(team, executions);
        }
        return executions;
    }

    @Override
    public WorkflowStepVO getWorkflowProcedureSteps(WorkFlowProcedureStepDTO stepDTO) {
        List<TeamListVO> historyChangeTeam = ChangeTeamUtils.getHistoryChangeTeam(stepDTO.getPlanId());
        if (CollUtil.isEmpty(historyChangeTeam)) {
            return null;
        }
        WorkflowStepVO vo = new WorkflowStepVO();
        vo.setPauseFlag(false);
        List<ExecutionInstance> executionInstances = null;
        if (StrUtil.isNotBlank(stepDTO.getExecutionId())) {
            List<ExecutionInstance> executions = executionQueryService.findSubProcessInstanceExecutions(buildQueryStepParam(stepDTO));
            //处理权限
            executionInstances = handelProcedureStep(executions, historyChangeTeam, stepDTO.getNodeFunction());
            this.handleCoerceCompleteShow(executionInstances,stepDTO,vo);
        }
        Plan plan = planService.getById(stepDTO.getPlanId());
        List<ProcedureStepModel> stepModels = procedureStepModelService.getStepModelByProcessIdAndVersionAndNodeIdList(plan.getProcessId(),
                plan.getProcessVersion());
        List<ProcedureTaskInstance> taskInstances = taskInstanceService.selectChangeListByPlanIdAndStepModeId(stepDTO, stepModels);
        if (CollUtil.isNotEmpty(taskInstances)) {
            //过滤数据权限
            taskInstances = handelProcedureTask(taskInstances, historyChangeTeam, stepDTO.getNodeFunction());
        }
        List<WorkflowNodeVO> workflowNodeVOList = WorkflowConverter.INSTANCE.convertProcedureStepVO(executionInstances, stepModels, taskInstances, plan);
        //根据条件判断更改状态展示
        estimateExecuteCondition(workflowNodeVOList);
        if (CollUtil.isEmpty(workflowNodeVOList)){
            if (stepDTO.getState().equals(ProcessStateEnum.INACTIVE.getValue())) {
                throw new BmosException(MesResponseCode.STEP_NOT_ACTIVE_ERROR);
            }
            throw new BmosException(MesResponseCode.STEP_NOT_JURISDICTION_ERROR);
        }
        List<WorkflowNodeVO> nodeList = workflowNodeVOList.stream().sorted(Comparator.comparing(WorkflowNodeVO::getSort)).collect(Collectors.toList());
        vo.setNodeList(nodeList);
        return vo;
    }

    /**
     * 是否存在暂停的节点，存在是否满足是否开启
     * @param executions
     * @param stepDTO
     * @param vo
     */
    private void handleCoerceCompleteShow(List<ExecutionInstance> executions,WorkFlowProcedureStepDTO stepDTO,WorkflowStepVO vo){
        if (!ProcessStateEnum.ACTIVE.getValue().equals(stepDTO.getState())){
            return;
        }
        List<ExecutionInstance> pauseExecution = CollectionUtils.filterList(executions, item -> StrUtil.isNotBlank(item.getPauseTag()));
        if (CollUtil.isEmpty(pauseExecution)){
            return;
        }
        Pair<Boolean, List<ProcedureConditionInstance>> pair = taskConditionCalculator.calculateProcedureModelExpression(stepDTO.getPlanId(), stepDTO.getProcedureModelId(),
                stepDTO.getProcedureChangeNumber(), stepDTO.getProcessChangeNumber());
        if (!pair.getLeft()){
            vo.setPauseFlag(true);
            List<ProcedureConditionInstance> condition = pair.getRight();
            vo.setConditionString(String.join(",", CollectionUtils.convertList(condition,ProcedureConditionInstance::getName)));
        }
    }

    /**
     * 判断执行条件改变工步以及任务状态
     * @param workflowNodeVOList 工步/任务数据
     */
    private void estimateExecuteCondition(List<WorkflowNodeVO> workflowNodeVOList){
        if (CollUtil.isEmpty(workflowNodeVOList)){
            return;
        }
        workflowNodeVOList.forEach(item->{
            if (item.getState().equals(ProcessStateEnum.ACTIVE.getValue()) && BooleanUtil.isFalse(item.getActiveState())){
                Pair<Boolean, List<ProcedureConditionInstance>> calculateTaskOrStepExpression =
                        taskConditionCalculator.calculateTaskOrStepExpression(item.getPlanId(), item.getProcedureStepModelId(),
                                ExpressionTypeEnum.EXECUTE_CONDITION);
                if (calculateTaskOrStepExpression.getLeft()) {
                    item.setState(ProcessStateEnum.IS_ACTIVE.getValue());
                    return;
                }
                item.setState(ProcessStateEnum.INACTIVE.getValue());
            }

        });
    }

    /**
     * 处理工步权限
     *
     * @param executions
     * @param team
     * @param changeType
     * @return
     */
    private List<ExecutionInstance> handelProcedureStep(List<ExecutionInstance> executions, List<TeamListVO> team, String changeType) {
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<ExecutionInstance> freshExecution = new ArrayList<>();
            Map<Integer, Map<String, List<String>>> teamMap = team.stream().collect(
                    (Collectors.groupingBy(TeamListVO::getChangeTeamNumber,
                            Collectors.groupingBy(TeamListVO::getChangeTeamType,
                                    Collectors.mapping(TeamListVO::getNodeStepId, Collectors.toList())))));
            executions.forEach(execution -> {
                List<String> nodeStepId;
                //使用默认班组权限
                if (execution.getProcessChangeNumber() == 0 && execution.getProcedureChangeNumber() == 0) {
                    nodeStepId = teamMap.get(execution.getProcessChangeNumber()).get(ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue());
                } else {
                    //存在换班，优先判断工序换班
                    nodeStepId = teamMap.get(changeType.equals(ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue()) ?
                            execution.getProcedureChangeNumber() : execution.getProcessChangeNumber()).get(changeType);
                }
                if (nodeStepId.contains(execution.getElementKey())) {
                    freshExecution.add(execution);
                }
            });
            return freshExecution;
        }
        return executions;
    }

    /**
     * 过滤任务数据权限
     *
     * @param taskInstances
     * @param team
     * @param changeType
     * @return
     */
    private List<ProcedureTaskInstance> handelProcedureTask(List<ProcedureTaskInstance> taskInstances, List<TeamListVO> team, String changeType) {
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<ProcedureTaskInstance> freshTaskInstance = new ArrayList<>();
            Map<Integer, Map<String, List<Long>>> teamMap = team.stream().collect(
                    (Collectors.groupingBy(TeamListVO::getChangeTeamNumber,
                            Collectors.groupingBy(TeamListVO::getChangeTeamType,
                                    Collectors.mapping(TeamListVO::getProcedureStepModelId, Collectors.toList())))));
            taskInstances.forEach(taskInstance -> {
                List<Long> stepModelId;
                //使用默认班组权限
                if (taskInstance.getProcessChangeNumber() == 0 && taskInstance.getProcedureChangeNumber() == 0) {
                    stepModelId = teamMap.get(taskInstance.getProcessChangeNumber()).get(ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue());
                } else {
                    //存在换班，优先判断工序换班
                    stepModelId = teamMap.get(changeType.equals(ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue()) ?
                            taskInstance.getProcedureChangeNumber() : taskInstance.getProcessChangeNumber()).get(changeType);
                }
                if (stepModelId.contains(taskInstance.getProcedureStepModelId())) {
                    freshTaskInstance.add(taskInstance);
                }
            });
            return freshTaskInstance;
        }
        return taskInstances;
    }

    @Override
    public void validateDeployment(String processModelId) {
        GraphCascadeValidateCmd cmd = new GraphCascadeValidateCmd();
        cmd.setDeploymentId(processModelId);
        deploymentService.cascadeValidateGraph(cmd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deployBatch(List<String> processModelIds) {
        DeployBatchDeploymentCmd cmd = new DeployBatchDeploymentCmd();
        cmd.setDeploymentIds(processModelIds);
        cmd.setDeployBy(SysUserHolder.getUser().getUserId());
        deploymentService.deployBatch(cmd);
    }

    @Override
    public CommonPage<WorkflowPlanHistoryPageVO> getPlanHistoryPage(AppPlanHistoryDTO dto) {
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<InstructionTeam> teamList = ChangeTeamUtils.getInstructionDetailByUserTeamId();
            if (CollUtil.isEmpty(teamList)){
                return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
            }
            dto.setTeamPlanIdList(CollectionUtils.convertSet(teamList,InstructionTeam::getProductPlanId));
        }
        List<Plan> plans = planService.productManagePageHistory(dto);
        CommonPage<Plan> planCommonPage = CommonPage.convertPage(plans);
        if (CollUtil.isEmpty(plans)) {
            return CommonPage.CommonPage(Collections.emptyList(), 0L, dto);
        }
        this.savePlanLinedName(plans);
        return WorkflowConverter.INSTANCE.convertHistoryPageVO(planCommonPage, dto);
    }


    /**
     * 计划信息添加产线名称
     * @param plans
     */
    private void savePlanLinedName(List<Plan> plans){
        List<FactoryLineDetailFeignVO> lineDateList = factoryFeign.queryLineDetailListByLineIds(CollectionUtils.convertList(plans, Plan::getProductionLineId)).getData();
        Map<Long, FactoryLineDetailFeignVO> lineMap = CollectionUtils.convertMap(lineDateList, FactoryLineDetailFeignVO::getId);
        plans.forEach(item->{
            FactoryLineDetailFeignVO vo = lineMap.get(item.getProductionLineId());
            item.setLineName(ObjectUtil.isEmpty(vo) ? "" : vo.getCode() + StrUtil.DASHED + vo.getName());
        });

    }
    @Override
    public List<WorkflowNodeVO> getWorkflowHistoryProcedures(String processInstanceId, Long processVersionId) {
        ExecutionQueryCmd cmd = new ExecutionQueryCmd();
        cmd.setProcessInstanceId(processInstanceId);
        cmd.setElementType(ElementTypeEnum.CALL_ACTIVITY_TASK.getType());
        List<ExecutionInstance> executions = executionQueryService.findHistoryByExecutionQueryCmd(cmd);
        Plan plan = planService.selectByExecuteProcessInstanceId(processInstanceId);
        List<ProcedureModel> modelList = procedureModelMapper.selectByProcessIdAndVersion(plan.getProcessId(), plan.getProcessVersion());
        //查询工作流未流中未流转到节点的工序信息
        List<ExecutionInstance> executionInstances = addNotActiveProcedure(executions, modelList, plan.getId());
        if (CollUtil.isEmpty(executionInstances)) {
            return null;
        }
        return WorkflowConverter.INSTANCE.convertProcedureVO(executionInstances, plan.getId(), modelList);
    }

    @Override
    public List<WorkflowNodeVO> getWorkflowHistoryProcedureSteps(WorkFlowProcedureStepDTO stepDTO) {
        List<TeamListVO> historyChangeTeam = ChangeTeamUtils.getHistoryChangeTeam(stepDTO.getPlanId());
        if (CollUtil.isEmpty(historyChangeTeam)) {
            return null;
        }
        List<ExecutionInstance> executions =
                executionQueryService.findHistorySubProcessInstanceExecutions(buildQueryStepParam(stepDTO));
        Plan plan = planService.getById(stepDTO.getPlanId());
        List<ExecutionInstance> executionInstances = handelProcedureStep(executions, historyChangeTeam, stepDTO.getNodeFunction());
        List<ProcedureStepModel> stepModels = procedureStepModelService.getStepModelByProcessIdAndVersionAndNodeIdList(plan.getProcessId(),
                plan.getProcessVersion());
        List<ProcedureTaskInstanceHistory> procedureTaskInstanceHistoryList = taskInstanceHistoryService.selectHistoryTask(stepDTO);
        List<ProcedureTaskInstance> taskInstances = ProcessTaskConverter.INSTANCE.convertToTask(procedureTaskInstanceHistoryList);
        if (CollUtil.isNotEmpty(taskInstances)) {
            taskInstances = handelProcedureTask(taskInstances, historyChangeTeam, stepDTO.getNodeFunction());
        }
        List<WorkflowNodeVO> vos = WorkflowConverter.INSTANCE.convertProcedureStepVO(executionInstances, stepModels, taskInstances, plan);
        return CollUtil.isEmpty(vos) ? vos : vos.stream().sorted(Comparator.comparing(WorkflowNodeVO::getSort)).collect(Collectors.toList());
    }

    /**
     * 构建查询工序步骤参数
     *
     * @param stepDTO 传递参数
     */
    public UserTaskExecutionQueryCmd buildQueryStepParam(WorkFlowProcedureStepDTO stepDTO) {
        UserTaskExecutionQueryCmd cmd = new UserTaskExecutionQueryCmd();
        cmd.setElementType(ElementTypeEnum.USER_TASK.getType());
        cmd.setExecutionId(stepDTO.getExecutionId());
        cmd.setState(null);
        cmd.setProcedureChangeNumber(stepDTO.getProcedureChangeNumber());
        cmd.setProcessChangeNumber(stepDTO.getProcessChangeNumber());
        return cmd;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#executionId")
    public void activeStep(ActivateStepDTO dto) {
        ActiveTaskCmd taskCmd = new ActiveTaskCmd();
        taskCmd.setExecutionId(dto.getExecutionId());
        taskCmd.setUserId(SysUserHolder.getUser().getUserId());
        executionService.activeExecutionTask(taskCmd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#teamDTO.planId")
    public void changeTeam(ChangeTeamDTO teamDTO) {
        teamDTO.validatedChangeTeamList();
        //校验流程状态
        checkoutFlowChangeAndBuildDto(teamDTO);
        //校验完成条件
        if (!BooleanUtil.isTrue(teamDTO.getIsCoerceComplete())){
            workflowExecutor.validateCompleteCondition(teamDTO.getPlanId(), teamDTO.getProcedureStepModelId());
        }
        if (teamDTO.getNodeFunction().equals(ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue())) {
            teamDTO.setProcedureChangeNumber(teamDTO.getProcedureChangeNumber() + 1);
        }
        if (teamDTO.getNodeFunction().equals(ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue())) {
            teamDTO.setProcessChangeNumber(teamDTO.getProcessChangeNumber() + 1);
        }
        //添加换班班次信息
        changeTeamService.saveChangeTeam(teamDTO.getChangeTeamList(),
                teamDTO.getNodeFunction().equals(ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue()) ? teamDTO.getProcessChangeNumber() : teamDTO.getProcedureChangeNumber(),
                teamDTO.getNodeFunction());
        //判断工步节点是否完成
        ChangeTeamContext changeTeam = BeanUtil.toBean(teamDTO, ChangeTeamContext.class);
        changeTeam.setPlan(planService.getById(teamDTO.getPlanId()));
        ChangeRestartByProcessInstanceIdCmd cmd = new ChangeRestartByProcessInstanceIdCmd();
        cmd.setChangeRestartBy(SysUserHolder.getUser().getUserId());
        cmd.setProcedureChangeRestartNumber(teamDTO.getProcedureChangeNumber());
        cmd.setProcessChangeRestartNumber(teamDTO.getProcessChangeNumber());
        cmd.setChangeRestartType(teamDTO.getNodeFunction());
        cmd.setProcessInstanceId(teamDTO.getProcessInstanceId());
        cmd.setChangeTeamId(CollectionUtils.convertMap(teamDTO.getChangeTeamList(), TeamListDTO::getProductInstructionTeamId, TeamListDTO::getTeamIds));
        processInstanceService.changeRestart(cmd);
        //根据换班类型处理任务数据
        SpringUtil.getBean(ChangeTeamFactory.class).getChangeTeam(teamDTO.getNodeFunction()).changeTeam(changeTeam);

    }

    /**
     * 针对任务节点构建参数并校验流程参数
     *
     * @param teamDTO
     */
    public void checkoutFlowChangeAndBuildDto(ChangeTeamDTO teamDTO) {
        if (!ProcedureStepNodeFunctionEnum.changeTeamFlag(teamDTO.getNodeFunction())) {
            throw new BmosException(MesResponseCode.STEP_MODEL_NOT_TEAM);
        }
        //任务点击换班构建工序流程实例id
        if (StrUtil.isBlank(teamDTO.getProcessInstanceId()) || StrUtil.isBlank(teamDTO.getExecutionId())) {
            //找当当前工序的nodeId
            ProcedureModel model = procedureModelMapper.selectById(teamDTO.getProcedureModelId());
            //找到当前工序的运行实例
            ExecutionChangeQueryCmd cmd = new ExecutionChangeQueryCmd();
            cmd.setBusinessKey(String.valueOf(teamDTO.getPlanId()));
            cmd.setElementKey(model.getNodeId());
            cmd.setProcedureChangeNumber(teamDTO.getProcedureChangeNumber());
            cmd.setProcessChangeNumber(teamDTO.getProcessChangeNumber());
            ExecutionInstance superExecutionInstance = executionQueryService.findExecutionByElementTypeAndBusinessKey(cmd);
            if (ObjectUtil.isEmpty(superExecutionInstance)){
                throw new BmosException(MesResponseCode.PROCEDURE_NOT_CHANGE_ERROR);
            }
            if (superExecutionInstance.getState().equals(ProcessState.COMPLETE.getState())) {
                throw new BmosException(MesResponseCode.PROCEDURE_COMPLETE_ERROR);
            }
            //判断当前任务是否已经完成
            List<ProcedureTaskInstance> taskInstance = taskInstanceService.selectTask(teamDTO);
            List<ProcedureTaskInstance> notCompleteTaskInstance = CollectionUtils.filterList(taskInstance, item ->
                    !StrUtil.equals(item.getFlowState(), ProductTaskStatusEnum.COMPLETE.getValue()));
            if (CollUtil.isEmpty(notCompleteTaskInstance)) {
                throw new BmosException(MesResponseCode.STEP_MODLE_COMPLETE_ERROR);
            }
            teamDTO.setProcessInstanceId(superExecutionInstance.getExecutionId());
            return;
        }
        //判断工序节点是否完成
        ProcessInstance processInstance = processInstanceService.findProcessInstanceByInstanceId(teamDTO.getProcessInstanceId());
        if (ObjectUtil.isEmpty(processInstance)){
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_CHANGE_ERROR);
        }
        if (processInstance.getProcessState().equals(ProcessState.COMPLETE.getState())) {
            throw new BmosException(MesResponseCode.PROCEDURE_COMPLETE_ERROR);
        }
        ExecutionInstance executionInstance = executionQueryService.findByExecutionIdAndState(teamDTO.getExecutionId(), ProcessState.COMPLETE);
        if (ObjectUtil.isNotEmpty(executionInstance)) {
            throw new BmosException(MesResponseCode.STEP_MODLE_COMPLETE_ERROR);
        }
    }

    /**
     * @Author: Ren Jin Guang
     * @Description: 强制激活工步接口
     * @param: stepDto 强制激活参数
     * @return: null
     * @Date: 2024-08-17 11:15:48
     */
    @Override
    public void coerceActiveStep(CoerceActivateStepDTO stepDTO) {
        ActiveTaskCmd taskCmd = new ActiveTaskCmd();
        taskCmd.setExecutionId(stepDTO.getExecutionId());
        taskCmd.setUserId(stepDTO.getUserId());
        executionService.activeExecutionTask(taskCmd);
    }

    @Override
    public WorkFlowToDoVO getTodoPageFresh(WorkflowTodoPageDTO dto) {
            //分页查询
            PlanPageDTO pageDTO = dto.convert2PlanPageDTO();
            //查询全量的待办数据
            List<WorkflowTodoPageVO> stepTodoPageFresh = getStepTodoPageFresh(pageDTO);
            WorkFlowToDoVO toDoVO = new WorkFlowToDoVO();
            //生产前确定数据
            PlanStartVO startVO =  this.getTodoPlanStart(dto);
            toDoVO.setPlanStartList(dto.getTodoType().equals(FlowToDoTypeEnum.PRESENT_TODO.getValue()) ?
                    startVO.getPresentPlanStartVo() : startVO.getFuturePlanStartVo());
            this.savePlanStartLineData(toDoVO.getPlanStartList());
            Integer presentPlanStartCount = CollUtil.isEmpty(startVO.getPresentPlanStartVo()) ? 0 : startVO.getPresentPlanStartVo().size();
            Integer futurePlanStartCount = CollUtil.isEmpty(startVO.getFuturePlanStartVo()) ? 0 : startVO.getFuturePlanStartVo().size();
            if (CollUtil.isEmpty(stepTodoPageFresh)){
                toDoVO.setFreshTodoVo(CommonPage.CommonPage(Collections.emptyList(), 0L, dto));
                toDoVO.setFutureTodoCount(futurePlanStartCount);
                toDoVO.setPresentTodoCount(presentPlanStartCount);
                return toDoVO;
            }
            //区分当前待办以及计划待办
            Map<Boolean, List<WorkflowTodoPageVO>> todoVo = stepTodoPageFresh
                    .stream()
                    .collect(Collectors.partitioningBy(e ->
                            ObjectUtil.isNotEmpty(e.getProcedureStartTime()) &&
                                    e.getProcedureStartTime().compareTo(LocalDate.now()) > 0));
            //当前待办
            List<WorkflowTodoPageVO> presentTodo = todoVo.get(false);
            //计划待办
            List<WorkflowTodoPageVO> futureTodo = todoVo.get(true);
            Set<Long> presentTodoPlanId = CollectionUtils.convertSet(presentTodo, WorkflowTodoPageVO::getPlanId);
            Set<Long> futureTodoPlanId = CollectionUtils.convertSet(futureTodo, WorkflowTodoPageVO::getPlanId);
            toDoVO.setPresentTodoCount(CollUtil.isEmpty(presentTodoPlanId) ? 0 : presentTodoPlanId.size() + presentPlanStartCount);
            toDoVO.setFutureTodoCount(CollUtil.isEmpty(futureTodoPlanId) ? 0 : futureTodoPlanId.size() + futurePlanStartCount);
            if (dto.getTodoType().equals(FlowToDoTypeEnum.PRESENT_TODO.getValue()) && CollUtil.isEmpty(presentTodo)){
                toDoVO.setFreshTodoVo(CommonPage.CommonPage(Collections.emptyList(), 0L, dto));
                return toDoVO;
            }
            if(dto.getTodoType().equals(FlowToDoTypeEnum.FUTURE_TODO.getValue()) && CollUtil.isEmpty(futureTodo)){
                toDoVO.setFreshTodoVo(CommonPage.CommonPage(Collections.emptyList(), 0L, dto));
                return toDoVO;
            }
            pageDTO.setIds(CollectionUtils.convertList(dto.getTodoType().equals(FlowToDoTypeEnum.PRESENT_TODO.getValue()) ?
                    presentTodo : futureTodo, WorkflowTodoPageVO::getPlanId));
            List<Plan> plans = planService.productManagePage(pageDTO);
            if (CollUtil.isEmpty(plans)) {
                toDoVO.setFreshTodoVo(CommonPage.CommonPage(Collections.emptyList(), 0L, dto));
                return toDoVO;
            }
            List<FactoryLineDetailFeignVO> lineList = factoryFeign.queryLineDetailListByLineIds(CollectionUtils.convertList(plans, Plan::getProductionLineId)).getData();
            Map<Long, FactoryLineDetailFeignVO> lineMap = CollectionUtils.convertMap(lineList, FactoryLineDetailFeignVO::getId);
            Map<Long, List<WorkflowTodoPageVO>> pageMap = CollectionUtils.convertMultiMap(dto.getTodoType().equals(FlowToDoTypeEnum.PRESENT_TODO.getValue()) ?
                    presentTodo : futureTodo, WorkflowTodoPageVO::getPlanId);
            plans.forEach(plan->{
                List<WorkflowTodoPageVO> voList = pageMap.get(plan.getId());
                FactoryLineDetailFeignVO vo = lineMap.get(plan.getProductionLineId());
                plan.setLineName(ObjectUtil.isEmpty(vo) ? "" : vo.getCode() + StrUtil.DASHED + vo.getName());
                plan.setTodoPageVOList(CollUtil.isEmpty(voList) ?
                        Collections.emptyList() : voList.stream().sorted(Comparator.comparing(WorkflowTodoPageVO::getStartTime)).collect(Collectors.toList()));
            });
            CommonPage<Plan> planCommonPage = CommonPage.convertPage(plans);
            toDoVO.setFreshTodoVo(WorkflowConverter.INSTANCE.convertToFreshTodo(planCommonPage));
            return toDoVO;
    }

    private Integer todoCount(PlanPageDTO dto,List<WorkflowTodoPageVO> list){
        if (CollUtil.isEmpty(list)){
            return 0;
        }
        dto.setIds(CollectionUtils.convertList(list,WorkflowTodoPageVO::getPlanId));
        return planService.productManagePageCount(dto);
    }

    /**
     * 查询待办数据
     * @return
     */
    private List<WorkflowTodoPageVO> getStepTodoPageFresh(PlanPageDTO dto) {
        List<Long> teamIds = productPlanTeamService.getListByUserId(SysUserHolder.getUser().getUserId());
        if (CollUtil.isEmpty(teamIds)) {
            return new ArrayList<>();
        }
        List<Plan> plan = planMapper.selectList(new LambdaQueryWrapperX<Plan>()
                .eq(Plan::getStart,ProductPlanStartEnum.STARTING.getValue()));
        if (CollUtil.isEmpty(plan)){
            return new ArrayList<>();
        }
        List<Long> planIds = plan.stream().map(Plan::getId).collect(Collectors.toList());
        //获取执行中计划的所有条件
        CompletableFuture<List<ProcedureExpression>> conditionList = CompletableFuture.supplyAsync(()->{
            return conditionInstanceService.startPlanConditionList(planIds);
        },todoQueryExecutor);
        //待办
        String userId = SysUserHolder.getUser().getUserId();
        CompletableFuture<List<WorkflowTodoPageVO>> stepTodoFreshS = CompletableFuture.supplyAsync(()->{
            return getStepTodoFresh(teamIds,plan);
        },todoWorkFlowQueryExecutor);
        CompletableFuture<List<WorkflowTodoPageVO>> taskTodoList = CompletableFuture.supplyAsync(()->{
            return taskInstanceService.queryTodoFresh(plan,userId);
        },taskInstanceQueryExecutor);
        CompletableFuture.allOf(conditionList,stepTodoFreshS,taskTodoList).join();
        List<WorkflowTodoPageVO> stepTodoFresh = stepTodoFreshS.join();
        stepTodoFresh.addAll(taskTodoList.join());
        if (CollUtil.isEmpty(stepTodoFresh)) {
            return Collections.emptyList();
        }
        //根据计划id获取工序执行时长
        Set<Long> productionPlanItemId = CollectionUtils.convertSet(stepTodoFresh, WorkflowTodoPageVO::getProductionPlanItemId);
        List<ProcedureTimeVO> procedureTime = itemService.selectProcedureConfigByPlanIds(productionPlanItemId);
        //添加工序执行时长判断执行条件
        return addProcedureDurations(stepTodoFresh, procedureTime, conditionList.join());
    }

    @Override
    public PlanProcedureStepDetailVO getProductionProcedureStepDetailInfo(PlanStepDetailQueryDTO dto) {
        Plan plan = planService.getById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(dto.getProcedureStepModelId());
        if (procedureStepModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        ProcedureModel procedureModel = procedureModelMapper.selectById(procedureStepModel.getProcedureModelId());
        if (procedureModel == null) {
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_EXIST);
        }

        return WorkflowConverter.INSTANCE.convert2PlanProcedureStepDetailVO(plan, procedureModel, procedureStepModel);
    }

    @Override
    public CommonPage<WorkflowPlanManagePageVO> getPlanProgressPage(PlanProgressDTO dto) {
        PlanPageDTO pageDTO = dto.convert2PlanPageDTO();
        BasePage page = new BasePage();
        page.setPageNum(dto.getPageNum());
        page.setPageSize(dto.getPageSize());
        //查询数据权限，使用工艺数据权限控制(管理员可以看到所有)
        if(!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())) {
            List<Long> deptIds = platformApiAdaptor.deptIds();
            List<Plan> planList = planMapper.getAuditBusinessKey(deptIds);
            if (CollUtil.isEmpty(planList)) {
                return CommonPage.CommonPage(Collections.emptyList(), 0L, page);
            }
            pageDTO.setIds(CollectionUtils.convertList(planList,Plan::getId));
        }
        return this.getPlanData(pageDTO,page);
    }

    @Override
    public void coerceProcedureComplete(WorkFlowProcedureStepDTO stepDTO) {
        if (!stepDTO.getState().equals(ProcessStateEnum.ACTIVE.getValue())){
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_ACTIVE);
        }
        List<ExecutionInstance> executions = executionQueryService.findSubProcessInstanceExecutions(buildQueryStepParam(stepDTO));
        List<ExecutionInstance> pauseExecution = CollectionUtils.filterList(executions, executionInstance ->
                StrUtil.isNotBlank(executionInstance.getPauseTag()));
        if (CollUtil.isEmpty(pauseExecution)){
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_PAUSE);
        }
        ExecutionInstance executionInstance = CollectionUtils.getFirst(pauseExecution);
        instanceService.coerceCompletePauseTask(executionInstance.getExecutionId());
    }

    @Override
    public List<TaskInstance> findByExecutionIdAndProcessInstanceId(String executionId, String processInstanceId) {
        return taskQueryService.findByExecutionIdAndProcessInstanceId(executionId,processInstanceId);
    }

    /**
     * 添加工序执行时长
     *
     * @param executableTodoList
     * @param procedureTime
     */
    private List<WorkflowTodoPageVO> addProcedureDurations(List<WorkflowTodoPageVO> executableTodoList, List<ProcedureTimeVO> procedureTime,
                                                           List<ProcedureExpression> list) {
        if (CollUtil.isEmpty(executableTodoList)) {
            return executableTodoList;
        }
        List<WorkflowTodoPageVO> pageVOS = new ArrayList<>();
        Map<Long, Map<Long, Map<String, List<Boolean>>>> planMap = new HashMap<>();
        if(CollUtil.isNotEmpty(list)){
            planMap =  list.stream().collect(
                    (Collectors.groupingBy(ProcedureExpression::getPlanId,
                            Collectors.groupingBy(ProcedureExpression::getProcedureStepModelId,
                                    Collectors.groupingBy(ProcedureExpression::getNodeId,
                                            Collectors.mapping(ProcedureExpression::getResults, Collectors.toList()))))));
        }
        Map<Long, ProcedureTimeVO> voMap = CollectionUtils.convertMap(procedureTime, ProcedureTimeVO::getPlanItemId);
        for (WorkflowTodoPageVO item : executableTodoList) {
            if (ObjectUtil.isNull(item.getActiveState()) || BooleanUtil.isFalse(item.getActiveState())){
                Map<Long, Map<String, List<Boolean>>> longMapMap = planMap.get(item.getPlanId());
                if (CollUtil.isNotEmpty(longMapMap) && CollUtil.isNotEmpty(longMapMap.get(item.getProcedureStepModelId())) &&
                        CollUtil.isNotEmpty(longMapMap.get(item.getProcedureStepModelId()).get(item.getNodeId())) &&
                        longMapMap.get(item.getProcedureStepModelId()).get(item.getNodeId()).contains(false)){
                    continue;
                }
            }
            ProcedureTimeVO procedureTimeVO = voMap.get(item.getProductionPlanItemId());
            if (ObjectUtil.isEmpty(procedureTimeVO) || StrUtil.isBlank(procedureTimeVO.getProcedureConfig())){
                pageVOS.add(item);
                continue;
            }
            Map<Long, PlanTemplateProcedureConfigDTO> procedureMap = CollectionUtils.convertMap(
                    JsonUtils.parseArray(procedureTimeVO.getProcedureConfig(), PlanTemplateProcedureConfigDTO.class),
                    PlanTemplateProcedureConfigDTO::getProcedureId);
            Map<Long, ProcedureDetailVO> procedurePlanMap = CollectionUtils.convertMap(
                    JsonUtils.parseArray(procedureTimeVO.getProcedureList(), ProcedureDetailVO.class),
                    ProcedureDetailVO::getProcedureId);
            PlanTemplateProcedureConfigDTO configDTO = procedureMap.get(item.getProcedureId());
            ProcedureDetailVO procedurePlan = procedurePlanMap.get(item.getProcedureId());
            if (ObjectUtil.isEmpty(configDTO)) {
                pageVOS.add(item);
                continue;
            }
            item.setProcedureDuration(Optional.ofNullable(Long.valueOf(configDTO.getExecutionDuration())).orElse(0L));
            item.setProcedureTimeUnit(TimeUnitEnum.DAY.getName());
            item.setProcedureStartTime(procedurePlan.getStartTime());
            pageVOS.add(item);
        }
        return pageVOS;
    }

    /**
     * 添加工序执行时长
     *
     * @param executableTodoList
     * @param procedureTime
     */
    private void addProcedureDuration(List<WorkflowTodoPageVO> executableTodoList, List<ProcedureTimeVO> procedureTime) {
        if (ObjectUtil.isEmpty(procedureTime)) {
            return;
        }
        Map<Long, ProcedureTimeVO> voMap = CollectionUtils.convertMap(procedureTime, ProcedureTimeVO::getPlanItemId);
        executableTodoList.forEach(item -> {
            ProcedureTimeVO procedureTimeVO = voMap.get(item.getProductionPlanItemId());
            if (ObjectUtil.isEmpty(procedureTimeVO) || StrUtil.isBlank(procedureTimeVO.getProcedureConfig())){
                return;
            }
            Map<Long, PlanTemplateProcedureConfigDTO> procedureMap = CollectionUtils.convertMap(
                    JsonUtils.parseArray(procedureTimeVO.getProcedureConfig(), PlanTemplateProcedureConfigDTO.class),
                    PlanTemplateProcedureConfigDTO::getProcedureId);
            Map<Long, ProcedureDetailVO> procedurePlanMap = CollectionUtils.convertMap(
                    JsonUtils.parseArray(procedureTimeVO.getProcedureList(), ProcedureDetailVO.class),
                    ProcedureDetailVO::getProcedureId);
            PlanTemplateProcedureConfigDTO configDTO = procedureMap.get(item.getProcedureId());
            ProcedureDetailVO procedurePlan = procedurePlanMap.get(item.getProcedureId());
            if (ObjectUtil.isEmpty(configDTO)) {
                return;
            }
            item.setProcedureDuration(Optional.ofNullable(Long.valueOf(configDTO.getExecutionDuration())).orElse(0L));
            item.setProcedureTimeUnit(TimeUnitEnum.DAY.getName());
            item.setProcedureStartTime(procedurePlan.getStartTime());
        });
    }

    @Override
    public List<ProcedureProgressVO> procedureProgress(String processInstanceId) {
        ExecutionQueryCmd cmd = new ExecutionQueryCmd();
        cmd.setProcessInstanceId(processInstanceId);
        cmd.setElementType(ElementTypeEnum.CALL_ACTIVITY_TASK.getType());
        Plan plan = planService.selectByExecuteProcessInstanceId(processInstanceId);
        //查询工作流工序节点数据
        List<ExecutionInstance> executions = executionQueryService.findByExecutionQueryCmd(cmd);
        List<ProcedureModel> modelList = procedureModelMapper.selectByProcessIdAndVersion(plan.getProcessId(), plan.getProcessVersion());
        List<ProcedureProgressVO> procedureProgressVOS = WorkflowConverter.INSTANCE.convertToProcedure(executions, plan.getId(), modelList);
        return procedureProgressVOS;
    }

    @Override
    public CommonPage<PlanSubRecordVO> queryPlanSubRecordList(PlanSubRecordQueryDTO dto) {
        Plan plan = planService.getById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<ExecuteSubsidiaryRecord> list = executeSubsidiaryRecordMapper.selectPageList(dto);
        CommonPage<ExecuteSubsidiaryRecord> commonPage = CommonPage.convertPage(list);
        CommonPage<PlanSubRecordVO> result = WorkflowConverter.INSTANCE.convertSubRecordVOPage(commonPage);
        result.getList().forEach(e -> e.setArchiveUrl(StrUtil.isEmpty(e.getArchiveUrl()) ? null :
                minioProperties.getBuckets().getArchive() + StrUtil.SLASH + e.getArchiveUrl()));
        return result;
    }

    @Override
    public List<StepChangeTeamListVO> listChangeTeam(StepChangeTeamDTO dto) {
        if (dto.getType().equals(StepTaskTypeEnum.TASK.getValue())) {
            if (ObjectUtil.isNull(dto.getProcedureStepModelId())) {
                throw new BmosException(MesResponseCode.FLOW_PAYLOAD_ERROR);
            }
            return taskInstanceService.queryChangeTeamListByStepModelIdAndPlanId(dto.getProcedureStepModelId(), dto.getPlanId());
        }
        if (CollUtil.isEmpty(dto.getExecutionIdList())) {
            return Collections.emptyList();
        }
        List<ExecutionInstance> executionInstances = executionQueryService.findChangeTeamByExecutionIdSAndElementKey(dto.getExecutionIdList(), dto.getNodeId());
        if (CollUtil.isEmpty(executionInstances)) {
            return Collections.emptyList();
        }
        executionInstances.forEach(item->{
            item.setProcedureChangeNumber(item.getProcedureChangeNumber()+1);
            item.setProcessChangeNumber(item.getProcessChangeNumber()+1);
            if (item.getState().equals(ProcessStateEnum.ACTIVE.getValue()) && !item.getActiveState()){
                item.setState(ProcessStateEnum.IS_ACTIVE.getValue());
            }
            item.setStartTime(item.getActiveTime());
        });
        return BeanUtil.copyToList(executionInstances, StepChangeTeamListVO.class);
    }

    @Override
    public WorkFlowStepProgressVO listStepProgress(WorkFlowStepProgressDTO dto) {
        WorkFlowStepProgressVO vo = new WorkFlowStepProgressVO();
        vo.setExecutionIdList(dto.getExecutionIdList());
        vo.setPlanId(dto.getPlanId());
        vo.setProcedureModelId(dto.getProcedureModelId());
        //查询任务数据
        List<TaskProgressVO> taskProgress = taskInstanceService.listTaskProgress(dto);
        vo.setTaskProgressList(taskProgress);
        //工序未激活
        if (dto.getState().equals(ProcessState.INACTIVE.getState())) {
            return vo;
        }
        if (ObjectUtil.isNull(dto.getFreshExecutionId())) {
            throw new BmosException(MesResponseCode.FLOW_PAYLOAD_ERROR);
        }
        //查询工步任务详情
        WorkFlowProcedureStepDTO stepDTO = new WorkFlowProcedureStepDTO();
        stepDTO.setExecutionId(dto.getFreshExecutionId());
        stepDTO.setProcedureChangeNumber(dto.getProcedureChangeNumber());
        stepDTO.setProcessChangeNumber(dto.getProcessChangeNumber());
        List<ExecutionInstance> executions =
                executionQueryService.findSubProcessInstanceExecutions(buildQueryStepParam(stepDTO));
        //查询流状态未流转到的节点
        Plan plan = planService.getById(dto.getPlanId());

        List<ProcedureStepModel> stepModelList = procedureStepModelService.queryListByProcessIdAndVersionAndModelId(plan.getProcessId(),
                plan.getProcessVersion(), dto.getProcedureModelId());
        if (CollUtil.isNotEmpty(executions)) {
            vo.setStepProgressList(getStepProgressVo(stepModelList, executions, plan.getId()));
        }
        return vo;
    }

    /**
     * 生产进度组件工步进度数据
     *
     * @param stepModelList 节点数据
     * @param executions    流程数据
     * @param planId        计划id
     * @return
     */
    private List<StepProgressVO> getStepProgressVo(List<ProcedureStepModel> stepModelList, List<ExecutionInstance> executions, Long planId) {
        List<String> nodeId = CollectionUtils.convertList(executions, ExecutionInstance::getElementKey);
        List<ProcedureStepModel> inactiveStepModelS = CollectionUtils.filterList(stepModelList, step ->
                !nodeId.contains(step.getNodeId()) && step.getStepType().equals(StepTaskTypeEnum.STEP));
        Map<String, ProcedureStepModel> stepModelMap = CollectionUtils.convertMap(stepModelList, ProcedureStepModel::getNodeId);
        //未激活的
        List<StepProgressVO> stepProgressVOS = new ArrayList<>();
        if (CollUtil.isNotEmpty(inactiveStepModelS)) {
            inactiveStepModelS.forEach(item -> {
                StepProgressVO vo = new StepProgressVO();
                vo.setNodeId(item.getNodeId());
                vo.setState(ProcessStateEnum.INACTIVE.getValue());
                vo.setType(StepTaskTypeEnum.STEP.getValue());
                vo.setProcedureStepModelId(item.getId());
                stepProgressVOS.add(vo);
            });
        }
        //构建数据
        List<StepProgressVO> stepProgressVO = WorkflowConverter.INSTANCE.convertToStepProgressVO(executions, stepModelMap);
        if (CollUtil.isEmpty(stepProgressVO)){
            return stepProgressVOS;
        }
        stepProgressVO.forEach(item ->{
            item.setType(StepTaskTypeEnum.STEP.getValue());
            if(BooleanUtil.isTrue(item.getActiveState())){
                return;
            }
            //执行条件是否满足,满足条件步骤可激活
            Pair<Boolean, List<ProcedureConditionInstance>> calculateTaskOrStepExpression =
                    taskConditionCalculator.calculateTaskOrStepExpression(planId, stepModelMap.get(item.getNodeId()).getId(),
                            ExpressionTypeEnum.EXECUTE_CONDITION);
            //满足执行条件
            if (calculateTaskOrStepExpression.getLeft()) {
                item.setState(ProcessStateEnum.IS_ACTIVE.getValue());
                return;
            }
            item.setState(ProcessStateEnum.INACTIVE.getValue());
        });
        stepProgressVOS.addAll(stepProgressVO);
        return stepProgressVOS;
    }

    /**
     * 查询工作流待办
     * @param teamIds 班组权限
     * @return
     */
    public List<WorkflowTodoPageVO> getStepTodoFresh(List<Long> teamIds,List<Plan> plan) {
        //查询工作流待办
        MultiTaskPageQueryCmd cmd = new MultiTaskPageQueryCmd();
        cmd.setAssignees(teamIds.stream().map(String::valueOf).collect(Collectors.toList()));
        cmd.setBusinessKeys(CollectionUtils.convertList(plan,Plan::getId).stream().map(String::valueOf).collect(Collectors.toList()));
        //获取数据
        List<MultiTaskQueryResp> taskQuery = taskQueryService.queryTodoFresh(cmd);
        if (CollUtil.isEmpty(taskQuery)) {
            return new ArrayList<>();
        }
        //获取计划id
        List<Long> planIdList = CollectionUtils.convertSet(taskQuery, MultiTaskQueryResp::getBusinessKey)
                .stream().map(Long::valueOf).collect(Collectors.toList());
        List<Plan> plans = CollectionUtils.filterList(plan,item->planIdList.contains(item.getId()));
        List<WorkflowTodoPageVO> stepTodoList = WorkflowConverter.INSTANCE.convertList(taskQuery, plans);
        List<String> procedureStepNodeIdList = CollectionUtils.convertList(stepTodoList, WorkflowTodoPageVO::getNodeId);
        List<ProcedureStepDurationVO> durationList = procedureStepModelService.getProcedureAndStepDurationByNodeIds(procedureStepNodeIdList);
        Map<Long, Map<String, Map<String, List<ProcedureStepDurationVO>>>> durationMap = durationList.stream().collect(Collectors.groupingBy(ProcedureStepDurationVO::getProcessId,
                Collectors.groupingBy(ProcedureStepDurationVO::getProcessVersion,
                        Collectors.groupingBy(ProcedureStepDurationVO::getProcedureStepNodeId))));
        if (CollUtil.isEmpty(stepTodoList)) {
            return new ArrayList<>();
        }
        //组装参数
        stepTodoList.forEach(e -> {
            Map<String, List<ProcedureStepDurationVO>> map = durationMap.get(e.getProcessId()).get(e.getProcessVersion());
            if (CollUtil.isEmpty(map)){
                return;
            }
            List<ProcedureStepDurationVO> vos = map.get(e.getNodeId());
            ProcedureStepDurationVO duration = CollUtil.getFirst(vos);
            e.setProcedureStepDuration(duration.getProcedureStepDuration());
            e.setProcedureStepTimeUnit(duration.getProcedureStepTimeUnit());
            e.setProcedureStepId(duration.getProcedureStepId());
            e.setProcedureStepModelId(duration.getProcedureStepModelId());
            e.setProcedureModelId(duration.getProcedureModelId());
            e.setNodeFunction(ProcedureStepNodeFunctionEnum.getEnumByValue(duration.getNodeFunction()));
            e.setSort(duration.getSort());
            e.setProcedureId(duration.getProcedureId());
        });
        return stepTodoList;
    }

    /**
     * 处理流程待办,判断执行条件是否满足
     *
     * @param taskTodoList 待办数据
     * @return
     */
    public List<WorkflowTodoPageVO> checkExecuteCondition(List<WorkflowTodoPageVO> taskTodoList) {
        Map<Boolean, List<WorkflowTodoPageVO>> listMap = CollectionUtils.convertMultiMap(taskTodoList, WorkflowTodoPageVO::getActiveState);
        //已经激活的任务
        List<WorkflowTodoPageVO> activeList = new ArrayList<>();
        if (CollUtil.isNotEmpty(listMap.get(true))) {
            activeList.addAll(listMap.get(true));
        }
        //未激活的任务
        List<WorkflowTodoPageVO> notActiveList = listMap.get(false);
        if (CollUtil.isNotEmpty(notActiveList)) {
            //判断执行条件
            notActiveList.forEach(item -> {
                try{
                    Pair<Boolean, List<ProcedureConditionInstance>> calculateTaskOrStepExpression =
                            taskConditionCalculator.calculateTaskOrStepExpression(item.getPlanId(), item.getProcedureStepModelId(),
                                    ExpressionTypeEnum.EXECUTE_CONDITION);
                    if (calculateTaskOrStepExpression.getLeft()) {
                        activeList.add(item);
                    }
                }catch (Exception e){
                    return;
                }
            });
        }
        return activeList;
    }

    /**
     * 查询待办生产前确定数据
     * @param dto 参数
     * @return
     */
    private PlanStartVO getTodoPlanStart(WorkflowTodoPageDTO dto){
        PlanStartVO startVo = new PlanStartVO();
        List<Long> processIds = new ArrayList<>();
        if (!AdminUtil.isAdminUser(SysUserHolder.getUser().getUserId())){
            List<Long> processIdList = processService.getIdListByDeptIds();
            ResponseInfo<List<FeignUserVO>> responseInfo = FeignUtils.handleRequest(data -> roleFeign.authUserList(data), dto.getMenuCode());
            List<String> userIds = CollectionUtils.convertList(responseInfo.getData(), FeignUserVO::getUserId);
            if (CollUtil.isEmpty(processIdList) || !userIds.contains(SysUserHolder.getUser().getUserId())){
                return new PlanStartVO();
            }
            processIds.addAll(processIdList);
        }
        CompletableFuture<List<PlanPageVO>> presentTodoPlanStart = CompletableFuture.supplyAsync(()->{
            return planService.getTodoPlanStart(dto,processIds,FlowToDoTypeEnum.PRESENT_TODO.getValue());
        },todoQueryExecutor);
        CompletableFuture<List<PlanPageVO>> futureTodoPlanStart = CompletableFuture.supplyAsync(()->{
            return planService.getTodoPlanStart(dto,processIds,FlowToDoTypeEnum.FUTURE_TODO.getValue());
        },todoQueryExecutor);
        CompletableFuture.allOf(presentTodoPlanStart,futureTodoPlanStart).join();
        startVo.setFuturePlanStartVo(futureTodoPlanStart.join());
        startVo.setPresentPlanStartVo(presentTodoPlanStart.join());
        return startVo;
    }

    /**
     * 生产前确定包装产线信息
     * @param todoPlanStart
     */
    private void savePlanStartLineData(List<PlanPageVO> todoPlanStart){
        if (CollUtil.isEmpty(todoPlanStart)){
            return;
        }
        List<FactoryLineDetailFeignVO> lineList = factoryFeign.queryLineDetailListByLineIds(CollectionUtils.convertList(todoPlanStart, PlanPageVO::getProductionLineId)).getData();
        Map<Long, FactoryLineDetailFeignVO> lineMap = CollectionUtils.convertMap(lineList, FactoryLineDetailFeignVO::getId);
        todoPlanStart.forEach(item->{
            FactoryLineDetailFeignVO vo = lineMap.get(item.getProductionLineId());
            item.setCode(ObjectUtil.isEmpty(vo) ? null : vo.getCode());
            item.setName(ObjectUtil.isEmpty(vo) ? null : vo.getName());
        });
    }
}
