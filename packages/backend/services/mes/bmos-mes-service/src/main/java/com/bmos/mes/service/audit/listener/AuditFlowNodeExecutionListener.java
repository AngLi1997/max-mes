package com.bmos.mes.service.audit.listener;

import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.context.RuntimeContext;
import com.bmos.audit.engine.core.element.base.BaseElement;
import com.bmos.audit.engine.core.element.enums.ElementTypeEnum;
import com.bmos.audit.engine.core.listener.InfiniteEvent;
import com.bmos.audit.engine.core.listener.InfiniteEventListener;
import com.bmos.audit.engine.core.listener.InfiniteEventType;
import com.bmos.audit.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.audit.engine.core.model.AuditProcessInstance;
import com.bmos.audit.engine.core.model.AuditTaskInstance;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.operate.service.OperateRuleVersionService;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.record.service.BatchRecordVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 会签节点审批动作
 */
@Component
public class AuditFlowNodeExecutionListener implements InfiniteEventListener {

    @Autowired
    private ProcessService processService;

    @Autowired
    private BatchRecordVersionService versionService;

    @Autowired
    private PlanService planService;

    @Autowired
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Autowired
    private OperateRuleVersionService ruleVersionService;

    @PostConstruct
    public void init() {
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.EXECUTION_NODE_COMPLETE, this);
    }

    @Override
    public void notified(InfiniteEvent event) {
        RuntimeContext context =(RuntimeContext) event.getPayload();
        BaseElement curElement = context.getCurElement();
        if (!ElementTypeEnum.USER_TASK.getType().equals(curElement.getType())) {
            return;
        }
        AuditProcessInstance processInstance = context.getAuditProcessInstance();
        AuditTaskInstance auditTaskInstance = context.getCurAuditTaskInstance();
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PROCESS.getCode())) {
            processService.auditExecutionSuccessCallBack(processInstance.getBusinessKey(),
                    auditTaskInstance.getComment(),auditTaskInstance.getRemark(), SysUserHolder.getUser().getUserId(),
                    curElement.getName());
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.RECODE.getCode())) {
            versionService.auditRecordExecutionSuccessCallBack(processInstance.getBusinessKey(), auditTaskInstance.getRemark(),
                    SysUserHolder.getUser().getUserId(),curElement.getName(),auditTaskInstance.getComment());
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PRODUCT_PLAN.getCode())) {
            planService.auditPlanLog(processInstance.getBusinessKey(),auditTaskInstance.getRemark(),SysUserHolder.getUser().getUserId(),
                    curElement.getName(),auditTaskInstance.getComment());
        }

        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PRODUCT_FORMULA.getCode())) {
            productFormulaConfigureService.auditNodeProductLog(processInstance.getBusinessKey(),auditTaskInstance.getRemark(),
                    SysUserHolder.getUser().getUserId(),curElement.getName(),auditTaskInstance.getComment());
        }
        if (StrUtil.equals(processInstance.getCategory(),AuditCategoryCodeEnum.OPERATE_RULE_START.getCode()) ||
                StrUtil.equals(processInstance.getCategory(),AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode())){
            ruleVersionService.auditOperateRuleNodeLog(processInstance.getBusinessKey(),auditTaskInstance.getRemark(),
                    SysUserHolder.getUser().getUserId(),curElement.getName(),auditTaskInstance.getComment());
        }
    }
}
