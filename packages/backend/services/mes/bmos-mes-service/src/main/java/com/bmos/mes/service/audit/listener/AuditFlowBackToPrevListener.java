package com.bmos.mes.service.audit.listener;

import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.context.RuntimeContext;
import com.bmos.audit.engine.core.element.base.BaseElement;
import com.bmos.audit.engine.core.listener.InfiniteEvent;
import com.bmos.audit.engine.core.listener.InfiniteEventListener;
import com.bmos.audit.engine.core.listener.InfiniteEventType;
import com.bmos.audit.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.audit.engine.core.model.AuditExecutionInstance;
import com.bmos.audit.engine.core.model.AuditProcessInstance;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.audit.constant.AuditMessageConstant;
import com.bmos.mes.service.audit.dto.SendMessageDTO;
import com.bmos.mes.service.audit.service.FlowAuditService;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.utils.AuditMessageSendUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 流程回退消息通知
 */
@Component
public class AuditFlowBackToPrevListener implements InfiniteEventListener {

    @Autowired
    private FlowAuditService auditService;

    @PostConstruct
    public void init() {
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.EXECUTION_BACK_TO_PREV, this);
    }

    @Override
    public void notified(InfiniteEvent event) {
        RuntimeContext context = (RuntimeContext) event.getPayload();
        AuditExecutionInstance execution = context.getPreExecution();
        AuditProcessInstance processInstance = context.getAuditProcessInstance();
        BaseElement curElement = context.getCurElement();
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PROCESS.getCode())) {
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), BusinessModule.PROCESS.name());
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.RECODE.getCode())) {
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), BusinessModule.BATCH_RECORD.name());
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PRODUCT_PLAN.getCode())) {
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), BusinessModule.PRODUCT_PLAN.name());
        }

        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PRODUCT_FORMULA.getCode())) {
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), BusinessModule.PRODUCT_FORMULA.name());
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.BATCH_SIGNATURE.getCode())) {
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), BusinessModule.BATCH_SIGNATURE.name());
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_START.getCode()) ||
                StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode())) {
            auditService.saveAuditBackHistory(processInstance.getBusinessKey(), context.getComment(), execution.getRemark(),
                    curElement.getName(), BusinessModule.OPERATE_RULE.name());
        }
        SendMessageDTO dto = new SendMessageDTO();
        dto.setRemark(execution.getRemark());
        dto.setComment(context.getComment());
        dto.setDeploymentId(processInstance.getDeploymentId());
        dto.setAuditCategoryCode(processInstance.getCategory());
        dto.setNodeId(context.getUserTaskKey());
        dto.setNodeName(I18nUtils.getCodeMessage(AuditMessageConstant.AUDIT_BACK_I18N_CODE, AuditMessageConstant.AUDIT_BACK, null) + execution.getElementName());
        dto.setBusinessId(Long.valueOf(processInstance.getBusinessKey()));
        dto.setIsStart(Boolean.FALSE);
        AuditMessageSendUtils.sendMessage(dto);
    }
}
