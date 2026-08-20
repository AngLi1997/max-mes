package com.bmos.mes.service.workflow.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.condition.event.StepCompleteCompleteType;
import com.bmos.orchestrator.engine.core.context.RuntimeContext;
import com.bmos.orchestrator.engine.core.listener.InfiniteEvent;
import com.bmos.orchestrator.engine.core.listener.InfiniteEventListener;
import com.bmos.orchestrator.engine.core.listener.InfiniteEventType;
import com.bmos.orchestrator.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.orchestrator.engine.core.model.ProcessInstance;
import com.bmos.orchestrator.engine.core.model.TaskInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 工步结束监听器
 */
@Component
@Slf4j
public class WorkflowProcedureStepEndEventListener implements InfiniteEventListener {


    @Autowired
    private PlanService planService;

    @Autowired
    private ITaskConditionCalculator conditionChangeHandler;

    @Autowired
    private ProcedureStepModelService modelService;


    @PostConstruct

    public void addListener() {
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.USER_TASK_COMPLETE, this);
    }

    @Override
    public void notified(InfiniteEvent event) {
        RuntimeContext runtimeContext = (RuntimeContext) event.getPayload();
        ProcessInstance instance = runtimeContext.getProcessInstance();
        TaskInstance taskInstance = runtimeContext.getCurTaskInstance();
        log.info("步骤完成发出通知:【{}】",event);
        if (ObjectUtil.isEmpty(taskInstance)) {
            return;
        }
        Plan plan = planService.selectByExecuteProcessInstanceId(instance.getSuperProcessInstanceId());
        List<ProcedureStepModel> stepModels = modelService.getStepModelByProcessIdAndVersion(plan.getProcessId(),
                plan.getProcessVersion());
        String elementKey = taskInstance.getElementKey();
        ProcedureStepModel model = CollectionUtils.findFirst(stepModels, item ->StrUtil.equals(item.getNodeId(),elementKey));
        if (model!=null) {
            StepCompleteCompleteType stepCompleteCompleteType = new StepCompleteCompleteType(plan.getId(), model.getId());
            log.info("条件完成计划信息:【{}】",plan);
            conditionChangeHandler.refreshConditionResult(stepCompleteCompleteType);
        }
    }
}
