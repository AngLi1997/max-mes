package com.bmos.mes.service.workflow.behavior;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.team.service.InstructionTeamService;

import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;
import com.bmos.mes.service.workflow.enums.WorkflowType;
import com.bmos.orchestrator.engine.core.behavior.TaskAssigneeBehavior;
import com.bmos.orchestrator.engine.core.constant.ExecutionConstant;
import com.bmos.orchestrator.engine.core.context.RuntimeContext;
import com.bmos.orchestrator.engine.core.element.base.BaseElement;
import com.bmos.orchestrator.engine.core.executor.param.CompleteTaskParam;
import com.bmos.orchestrator.engine.core.model.ExecutionInstance;
import com.bmos.orchestrator.engine.core.model.ProcessInstance;
import com.bmos.orchestrator.engine.core.model.TaskAssignee;
import com.bmos.orchestrator.engine.core.model.TaskInstance;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.CheckedOutputStream;

public class CustomTaskAssigneeBehavior implements TaskAssigneeBehavior {

    private final InstructionTeamService instructionTeamService;

    private final PlanService planService;

    private final ProcedureStepModelService modelService;

    private final ITaskConditionCalculator taskConditionCalculator;

    public CustomTaskAssigneeBehavior(InstructionTeamService instructionTeamService, PlanService planService,
                                      ProcedureStepModelService modelService,ITaskConditionCalculator taskConditionCalculator) {
        this.instructionTeamService = instructionTeamService;
        this.planService = planService;
        this.modelService = modelService;
        this.taskConditionCalculator = taskConditionCalculator;
    }

    @Override
    public List<TaskAssignee> findTaskAssignees(RuntimeContext context) {
        ProcessInstance processInstance = context.getProcessInstance();
        //获取班次信息组装班组信息
        List<Long> teamIds = new ArrayList<>();
        BaseElement curElement = context.getCurElement();
        Map<String, Object> changeRestartParam = context.getChangeRestartParam();
        if (CollUtil.isNotEmpty(changeRestartParam)){
            //班组工序换班优先
            Integer procedureNumber = (Integer) changeRestartParam.get(ExecutionConstant.CHANGE_PROCEDURE_NUMBER);
            Integer processNumber = (Integer) changeRestartParam.get(ExecutionConstant.CHANGE_PROCESS_NUMBER);
            String changeType = (String) changeRestartParam.get(ExecutionConstant.CHANGE_TYPE);
            Map<Long, List<Long>> changeTeamId = context.getChangeTeamId();
            teamIds = instructionTeamService.getChangeTeamIds(
                    ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue().equals(changeType) ? procedureNumber : processNumber,
                    Long.valueOf(processInstance.getBusinessKey()), curElement.getKey(),changeTeamId,changeType);
        }else {
            teamIds = instructionTeamService.getTeamIds(Long.valueOf(processInstance.getBusinessKey()), curElement.getKey());
        }
        return teamIds.stream().map(e -> new TaskAssignee(String.valueOf(e), "team")).collect(Collectors.toList());
    }

    @Override
    public CompleteTaskParam completed(RuntimeContext context) {
        CompleteTaskParam param = new CompleteTaskParam();
        param.setPause(false);
        param.setComplete(true);
        //设置任务完成人
        TaskInstance taskInstance = context.getCurTaskInstance();
        taskInstance.setCompletedBy(SysUserHolder.getUser().getUserId());
        if (BooleanUtil.isFalse(context.getIsPause())){
            return param;
        }
        //判断工序是否配置完成任务，任务条件是否已达到
        ProcessInstance processInstance = context.getProcessInstance();
        String planId = processInstance.getBusinessKey();
        Plan plan = planService.getById(planId);
        List<ProcedureStepModel> stepModelList = CollectionUtils.filterList(
                modelService.getStepModelByProcessIdAndVersionAndNodeIdList(plan.getProcessId(), plan.getProcessVersion()),
                item -> item.getNodeId().equals(taskInstance.getElementKey()));
        if (CollUtil.isEmpty(stepModelList)){
            throw new BmosException(MesResponseCode.FLOW_AUDIT_COMPLETE_ERROR);
        }
        //添加换班次数
        ExecutionInstance curExecution = context.getCurExecution();
        Pair<Boolean, List<ProcedureConditionInstance>> pair = taskConditionCalculator.calculateProcedureModelExpression(plan.getId(), CollectionUtils.getFirst(stepModelList).getProcedureModelId(),
                curExecution.getProcedureChangeNumber(), curExecution.getProcessChangeNumber());
        if (!pair.getLeft()) {
            param.setPause(true);
            param.setPauseTag(WorkflowType.IS_PAUSE.name());
        }
        return param;
    }
}
