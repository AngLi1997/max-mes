package com.bmos.lims2.server.audit.listener;

import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.context.RuntimeContext;
import com.bmos.audit.engine.core.element.base.BaseElement;
import com.bmos.audit.engine.core.listener.InfiniteEvent;
import com.bmos.audit.engine.core.listener.InfiniteEventListener;
import com.bmos.audit.engine.core.listener.InfiniteEventType;
import com.bmos.audit.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.audit.engine.core.model.AuditExecutionInstance;
import com.bmos.audit.engine.core.model.AuditProcessInstance;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.server.audit.FlowAuditService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 检验方案审批退回到上一级监听器
 * @author system
 */
@Slf4j
@Component
public class AuditFlowBackToPrevListener implements InfiniteEventListener {

    @Autowired
    private FlowAuditService auditService;
    @PostConstruct
    public void init() {
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.EXECUTION_BACK_TO_PREV, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void notified(InfiniteEvent event) {
        RuntimeContext context = (RuntimeContext) event.getPayload();
        AuditExecutionInstance execution = context.getPreExecution();
        AuditProcessInstance processInstance = context.getAuditProcessInstance();
        BaseElement curElement = context.getCurElement();
        String nodeName = StrUtil.isBlank(context.getCurElement().getName()) ? null : context.getCurElement().getName();
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.SCHEME_AUDIT.getCode())) {
            log.info("检验方案审批退回到上一级，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), AuditBusinessModule.INSPECT_SCHEME.name());
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.STABILITY_SCHEME_AUDIT.getCode())) {
            log.info("稳定性方案审批退回到上一级，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), AuditBusinessModule.STABILITY_SCHEME.name());
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.SAMPLE_AUDIT.getCode())) {
            log.info("检验方案审批退回到上一级，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), AuditBusinessModule.SAMPLE_AUDIT.name());
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.REPORT_AUDIT.getCode())) {
            log.info("检验方案审批退回到上一级，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), AuditBusinessModule.REPORT_AUDIT.name());
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.METHOD_AUDIT.getCode())) {
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), AuditBusinessModule.METHOD_AUDIT.name());
        } if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_START.getCode()) ||
                StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode())) {
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), AuditBusinessModule.OPERATE_RULE.name());
        }
    }
}