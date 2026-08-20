package com.bmos.mes.service.workflow.service;

import com.bmos.mes.service.workflow.dto.CompleteTaskByExecutionDTO;
import com.bmos.mes.service.workflow.dto.CompleteTaskDTO;
import com.bmos.mes.service.workflow.dto.StartWorkflowDTO;
import com.bmos.mes.service.workflow.dto.WorkflowRestartDTO;
import org.springframework.validation.annotation.Validated;

@Validated
public interface WorkflowExecutor {

    /**
     * 发起业务流
     * @param dto {@link StartWorkflowDTO }
     * @return 流程实例id
     */
    String startWorkflow(@Validated StartWorkflowDTO dto);


    void completeTask(CompleteTaskDTO dto);

    void restart(WorkflowRestartDTO dto);

    void terminate(String processInstanceId);

    Boolean completeTaskByExecution(CompleteTaskByExecutionDTO dto);

}
