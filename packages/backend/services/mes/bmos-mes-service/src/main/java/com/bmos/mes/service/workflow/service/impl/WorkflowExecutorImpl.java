package com.bmos.mes.service.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.process.ProcessStateEnum;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.execute.dto.CopiesQueryDTO;
import com.bmos.mes.service.execute.dto.SubRecordNodeQueryDTO;
import com.bmos.mes.service.execute.mapper.ExecuteFormDataMapper;
import com.bmos.mes.service.execute.mapper.ExecuteSubsidiaryRecordMapper;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.execute.service.ExecuteRecordCopyService;
import com.bmos.mes.service.plan.team.service.ProductPlanTeamService;
import com.bmos.mes.service.process.dto.query.CalculateDataQueryDTO;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.ProcessVersion;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.ProcessVersionService;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.task.ProcedureTaskInstanceService;
import com.bmos.mes.service.record.base.contants.ComponentConfigFieldCodes;
import com.bmos.mes.service.workflow.convert.FlowCommandConverter;
import com.bmos.mes.service.workflow.dto.*;
import com.bmos.mes.service.workflow.service.WorkflowExecutor;
import com.bmos.mes.service.workflow.service.WorkflowService;
import com.bmos.mes.service.workflow.vo.CompleteTaskVO;
import com.bmos.orchestrator.engine.core.command.CompleteTaskByExecutionCmd;
import com.bmos.orchestrator.engine.core.command.CompleteTaskCmd;
import com.bmos.orchestrator.engine.core.command.RestartBySuperExecutionCmd;
import com.bmos.orchestrator.engine.core.command.TerminateCmd;
import com.bmos.orchestrator.engine.core.model.ProcessInstance;
import com.bmos.orchestrator.engine.core.model.TaskInstance;
import com.bmos.orchestrator.engine.core.service.ProcessInstanceService;
import com.bmos.orchestrator.engine.core.service.TaskInstanceService;
import com.bmos.orchestrator.engine.core.state.ProcessState;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkflowExecutorImpl implements WorkflowExecutor {

    @Autowired
    @Lazy
    private WorkflowService workflowService;

    @Autowired
    private ProcessVersionService processVersionService;

    @Autowired
    private TaskInstanceService taskInstanceService;

    @Autowired
    private ProductPlanTeamService productPlanTeamService;

    @Autowired
    private ProcessInstanceService processInstanceService;

    @Autowired
    private ProcedureTaskInstanceService procedureTaskInstanceService;

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private ExecuteRecordCopyService executeRecordCopyService;

    @Autowired
    private ExecuteFormDataMapper executeFormDataMapper;

    @Resource
    private ITaskConditionCalculator taskConditionCalculator;
    @Autowired
    private ExecuteSubsidiaryRecordMapper executeSubsidiaryRecordMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#dto.productPlanId")
    public String startWorkflow(StartWorkflowDTO dto) {
        ProcessVersion processVersion = processVersionService.getByProcessModel(dto.getProcessId(),dto.getProcessVersion());
        return workflowService.startProcessInstance(FlowCommandConverter.INSTANCE.convertStartCmd(dto,processVersion));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#dto.processInstanceId + #dto.taskId" )
    public void completeTask(CompleteTaskDTO dto) {
        // 校验必填组件是否填值
        this.validateRequiredComponent(dto.getProductPlanId(), dto.getProcedureStepModelId());
        if (!BooleanUtil.isTrue(dto.getIsCoerceComplete())){
            this.validateCompleteCondition(dto.getProductPlanId(),dto.getProcedureStepModelId());
        }
        //判断是否是任务是任务的话完成任务忽略工作流步骤
        CompleteTaskVO vo = procedureTaskInstanceService.completeTaskByExecution(dto.getTaskId());
        if (vo.getCompleteTask()){
            return ;
        }
        if (StrUtil.isBlank(dto.getProcessInstanceId())){
            throw new BmosException(MesResponseCode.FLOW_PAYLOAD_ERROR);
        }
        CompleteTaskCmd cmd = new CompleteTaskCmd();
        cmd.setTaskId(dto.getTaskId());
        cmd.setProcessInstanceId(dto.getProcessInstanceId());
        String userId = SysUserHolder.getUser().getUserId();
        List<Long> teams = productPlanTeamService.getListByUserId(userId);
        cmd.setCompletedBy(userId);
        cmd.setAssignees(teams.stream().map(String::valueOf).collect(Collectors.toList()));
        taskInstanceService.complete(cmd);
        executeSubsidiaryRecordMapper.completeSubRecordNode(SubRecordNodeQueryDTO.builder()
                .procedureChangeNumber(dto.getProcedureChangeNumber())
                .processChangeNumber(dto.getProcessChangeNumber())
                .procedureStepModelId(dto.getProcedureStepModelId())
                .productPlanId(dto.getProductPlanId())
                .build(), userId);
    }

    public void validateCompleteCondition(Long productPlanId, Long procedureStepModelId) {
        Pair<Boolean, List<ProcedureConditionInstance>> calculateTaskOrStepExpression =
                taskConditionCalculator.calculateTaskOrStepExpression(productPlanId,procedureStepModelId,
                        ExpressionTypeEnum.COMPLETE_CONDITION);
        if (!calculateTaskOrStepExpression.getLeft()) {
            List<ProcedureConditionInstance> procedureConditionInstances = calculateTaskOrStepExpression.getRight();
            // 筛选出来条件实例执行结果为false的
            String msg = procedureConditionInstances.stream().filter(procedureConditionInstance ->
                    !procedureConditionInstance.getTaskResult()).map(ProcedureConditionInstance::getName).collect(Collectors.joining(","));
            throw new BmosException(MesResponseCode.STEP_COMPLETE_ERROR);
        }
    }


    /**
     * 校验配置必填的组件是否必填
     * @param productPlanId  生产计划id
     * @param procedureStepModelId 工步模型id
     */
    private void validateRequiredComponent(Long productPlanId, Long procedureStepModelId) {
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(procedureStepModelId);
        List<ProcedureStepConfig> config = procedureStepConfigService.getListByProcedureStepModel(procedureStepModel);
        // 过滤出当前记录项所有需要必填的组件
        Set<Long> requiredFields = config.stream().map(e -> {
            String configInfo = e.getConfigInfo();
            JSONObject jsonObject = JSONUtil.parseObj(configInfo);
            Boolean required = jsonObject.get(ComponentConfigFieldCodes.REQUIRED, Boolean.class);
            if (BooleanUtil.isTrue(required)) {
                return e.getFieldId();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(requiredFields)) {
            // 取出当前步骤记录及复制的记录
            List<ExecuteRecordCopy> copies = executeRecordCopyService.getCurrentStepCopies(CopiesQueryDTO.builder()
                    .productPlanId(productPlanId)
                    .recordItemId(procedureStepModel.getRecordItemId())
                    .reuse(procedureStepModel.getReusable())
                    .procedureStepId(procedureStepModel.getProcedureStepId())
                    .build());
            // 取出当前步骤要求必填组件填报的所有数据
            List<ExecuteFormData> list =
                    executeFormDataMapper.selectCurrentRecordItemFieldValues(CalculateDataQueryDTO.builder()
                                    .productPlanId(productPlanId)
                                    .recordItemId(procedureStepModel.getRecordItemId())
                                    .reuse(procedureStepModel.getReusable())
                                    .procedureStepId(procedureStepModel.getProcedureStepId())
                                    .build(),
                            requiredFields);
            Map<Long, Set<Long>> dataMap = CollectionUtils.convertMultiMap2(list, ExecuteFormData::getCopyVersion,
                    ExecuteFormData::getFieldId);
            for (ExecuteRecordCopy copy : copies) {
                Long copyVersion = copy.getVersion();
                Set<Long> existsDataSet = dataMap.get(copyVersion);
                for (Long requiredField : requiredFields) {
                    if (CollUtil.isEmpty(existsDataSet) || !existsDataSet.contains(requiredField)) {
                        throw new BmosException(MesResponseCode.REQUIRED_DATA_COMPONENT_HAS_NOTHING);
                    }
                }

            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#dto.executionId")
    public void restart(WorkflowRestartDTO dto) {
        //校验工序流程是否已完成
        if (dto.getState().equals(ProcessState.COMPLETE.getState())){
            throw new BmosException(MesResponseCode.PROCEDURE_RESTART_ERROR);
        }
        if (dto.getState().equals(ProcessState.INACTIVE.getState())){
            throw new BmosException(MesResponseCode.PROCEDURE_NOT_ACTIVE_ERROR);
        }
        RestartBySuperExecutionCmd cmd = new RestartBySuperExecutionCmd();
        cmd.setSuperExecutionId(dto.getExecutionId());
        cmd.setRestartBy(SysUserHolder.getUser().getUserId());
        cmd.setProcedureChangeNumber(dto.getProcedureChangeNumber());
        cmd.setProcessChangeNumber(dto.getProcessChangeNumber());
        processInstanceService.restart(cmd);
        ProcedureRestartDTO restartDto = BeanUtil.toBean(dto, ProcedureRestartDTO.class);
        restartDto.setIsChangeTeam(false);
        procedureTaskInstanceService.restart(restartDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#processInstanceId")
    public void terminate(String processInstanceId) {
        TerminateCmd cmd = new TerminateCmd();
        cmd.setProcessInstanceId(processInstanceId);
        cmd.setUserId(SysUserHolder.getUser().getUserId());
        processInstanceService.terminate(cmd);
        //找到当前流程下的任务
        procedureTaskInstanceService.terminate(processInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(expression = "#dto.processInstanceId + #dto.executionId")
    public Boolean completeTaskByExecution(CompleteTaskByExecutionDTO dto) {
        if (dto.getState().equals(ProcessStateEnum.COMPLETE.getValue())){
           throw new BmosException(MesResponseCode.STEP_MODLE_COMPLETE_ERROR);
        }
        // 校验必填组件是否填值
        this.validateRequiredComponent(dto.getProductPlanId(), dto.getProcedureStepModelId());
        if (!BooleanUtil.isTrue(dto.getIsCoerceComplete())){
            this.validateCompleteCondition(dto.getProductPlanId(), dto.getProcedureStepModelId());
        }
        //判断是否是任务是任务的话完成任务忽略工作流步骤
        CompleteTaskVO vo = procedureTaskInstanceService.completeTaskByExecution(dto.getExecutionId());
        if (vo.getCompleteTask()){
            return vo.getStartPauseProcedure();
        }
        if (StrUtil.isBlank(dto.getProcessInstanceId())){
            throw new BmosException(MesResponseCode.FLOW_PAYLOAD_ERROR);
        }
        this.validateComplete(dto.getExecutionId(),dto.getProcessInstanceId());
        CompleteTaskByExecutionCmd cmd = new CompleteTaskByExecutionCmd();
        cmd.setProcessInstanceId(dto.getProcessInstanceId());
        cmd.setExecutionId(dto.getExecutionId());
        cmd.setCompletedBy(SysUserHolder.getUser().getUserId());
        taskInstanceService.complete(cmd);
        executeSubsidiaryRecordMapper.completeSubRecordNode(SubRecordNodeQueryDTO.builder()
                .procedureChangeNumber(dto.getProcedureChangeNumber())
                .processChangeNumber(dto.getProcessChangeNumber())
                .procedureStepModelId(dto.getProcedureStepModelId())
                .productPlanId(dto.getProductPlanId())
                .build(), SysUserHolder.getUser().getUserId());
        ProcessInstance instance = processInstanceService.findProcessInstanceByInstanceId(dto.getProcessInstanceId());
        if (ObjectUtil.isEmpty(instance)){
            return true;
        }
        return false;
    }

    /**
     * 校验流程是否完成
     * @param executionId
     * @param processInstanceId
     */
    private void validateComplete(String executionId,String processInstanceId){
        ProcessInstance processInstance = processInstanceService.findProcessInstanceByInstanceId(processInstanceId);
        if (ObjectUtil.isEmpty(processInstance) && processInstance.getProcessState().equals(ProcessStateEnum.COMPLETE.getValue())){
            throw new BmosException(MesResponseCode.PROCEDURE_END_ERROR);
        }
        List<TaskInstance> taskInstances = workflowService.findByExecutionIdAndProcessInstanceId(executionId, processInstanceId);
        if (CollUtil.isEmpty(taskInstances)){
            throw new BmosException(MesResponseCode.STEP_MODLE_COMPLETE_ERROR);
        }
    }
}
