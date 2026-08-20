package com.bmos.mes.service.workflow.listener;

import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.process.mapper.ProcessConfirmMapper;
import com.bmos.mes.service.process.model.ProcessConfirm;
import com.bmos.mes.service.process.service.impl.task.ProcedureTaskInstanceServiceImpl;
import com.bmos.orchestrator.engine.core.listener.InfiniteEvent;
import com.bmos.orchestrator.engine.core.listener.InfiniteEventListener;
import com.bmos.orchestrator.engine.core.listener.InfiniteEventType;
import com.bmos.orchestrator.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.orchestrator.engine.core.model.ProcessInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * 整个工艺结束
 */
@Component
@Slf4j
public class WorkflowProcessEndEventListener implements InfiniteEventListener {

    @Autowired
    private PlanService planService;

    @Autowired
    private ProcessConfirmMapper mapper;

    @Autowired
    private ProcedureTaskInstanceServiceImpl taskInstanceService;

    @PostConstruct
    public void addListener() {
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.PROCESS_END, this);
    }

    @Override
    public void notified(InfiniteEvent event) {
        ProcessInstance processInstance = (ProcessInstance) event.getPayload();
        log.info("收到流程正常结束回调：[{}]", processInstance.getProcessInstanceId());
        planService.executeCallBackSuccess(processInstance.getProcessInstanceId());
        ProcessConfirm processConfirm = mapper.queryProcessConfirmByInstanceId(processInstance.getProcessInstanceId());
        processConfirm.setEndTime(LocalDateTime.now());
        mapper.saveProcessConfirm(processConfirm);
        taskInstanceService.transferCompletedIntoHistory(Long.valueOf(processInstance.getBusinessKey()));
    }
}
