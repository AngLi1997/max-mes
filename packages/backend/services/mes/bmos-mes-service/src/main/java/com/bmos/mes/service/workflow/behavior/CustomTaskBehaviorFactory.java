package com.bmos.mes.service.workflow.behavior;

import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.team.service.InstructionTeamService;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.orchestrator.engine.core.behavior.TaskAssigneeBehavior;
import com.bmos.orchestrator.engine.core.behavior.TaskBehaviorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("taskBehaviorFactory")
public class CustomTaskBehaviorFactory implements TaskBehaviorFactory {

    @Autowired
    @Lazy
    private InstructionTeamService instructionTeamService;

    @Resource
    @Lazy
    private PlanService planService;

    @Resource
    @Lazy
    private ProcedureStepModelService modelService;

    @Resource
    @Lazy
    private ITaskConditionCalculator taskConditionCalculator;

    private TaskAssigneeBehavior taskAssigneeBehavior;

    @Override
    public TaskAssigneeBehavior getTaskAssigneeBehavior(String assigneeBehavior) {
        if (taskAssigneeBehavior == null) {
            taskAssigneeBehavior = new CustomTaskAssigneeBehavior(instructionTeamService,planService,modelService,taskConditionCalculator);
        }
        return taskAssigneeBehavior;
    }


}
