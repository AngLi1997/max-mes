package com.bmos.mes.service.audit.Behavior;

import com.bmos.audit.engine.core.behavior.AuditDefaultTaskBehaviorFactory;
import com.bmos.audit.engine.core.behavior.TaskAssigneeBehavior;
import com.bmos.audit.engine.core.constant.TaskConstant;
import com.bmos.audit.engine.core.exception.InfiniteEngineException;
import org.springframework.stereotype.Component;

@Component("auditDefaultTaskBehaviorFactory")
public class FlowAuditTaskBehaviorFactory extends AuditDefaultTaskBehaviorFactory {
    private final FlowAuditTaskAssigneeBehavior flowAuditTaskAssigneeBehavior;

    public FlowAuditTaskBehaviorFactory() {
        this.flowAuditTaskAssigneeBehavior = new FlowAuditTaskAssigneeBehavior();
    }

    @Override
    public TaskAssigneeBehavior getTaskAssigneeBehavior(String assigneeBehavior) {
        if (null == assigneeBehavior || TaskConstant.DEFAULT_TASK_ASSIGNEE_BEHAVIOR.equals(assigneeBehavior)) {
            return this.flowAuditTaskAssigneeBehavior;
        }
        throw new InfiniteEngineException("未找到 [%s] 对应的 TaskAssigneeBehavior",assigneeBehavior);
    }
}

