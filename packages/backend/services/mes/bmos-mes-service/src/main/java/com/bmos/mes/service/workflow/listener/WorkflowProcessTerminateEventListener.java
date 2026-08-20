package com.bmos.mes.service.workflow.listener;

import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.orchestrator.engine.core.listener.InfiniteEvent;
import com.bmos.orchestrator.engine.core.listener.InfiniteEventListener;
import com.bmos.orchestrator.engine.core.listener.InfiniteEventType;
import com.bmos.orchestrator.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.orchestrator.engine.core.model.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *流程终止
 */
@Component
public class WorkflowProcessTerminateEventListener implements InfiniteEventListener {

    @Autowired
    private PlanService planService;

    @PostConstruct
    public void addListener(){
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.PROCESS_TERMINATE,this);
    }
    @Override
    public void notified(InfiniteEvent event) {
        ProcessInstance processInstance = (ProcessInstance) event.getPayload();
        planService.executeCallBackTermination(processInstance.getProcessInstanceId());
    }
}
