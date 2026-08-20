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
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.audit.constant.AuditMessageConstant;
import com.bmos.mes.service.audit.dto.SendMessageDTO;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.lotrelease.manage.service.ILotReleaseService;
import com.bmos.mes.service.operate.service.OperateRuleVersionService;
import com.bmos.mes.service.plan.document.service.ArchiveAuditCallBackDTO;
import com.bmos.mes.service.plan.document.service.BatchRecordArchiveService;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.process.service.ProcessService;
import com.bmos.mes.service.record.service.BatchRecordVersionService;
import com.bmos.mes.service.utils.AuditMessageSendUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 审批任务节点结束
 */
@Component
public class AuditFlowExecutionEndListener implements InfiniteEventListener {

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

    @Autowired
    private BatchRecordArchiveService batchRecordArchiveService;

    @Resource
    private ILotReleaseService lotReleaseService;

    @PostConstruct
    public void init() {
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.EXECUTION_COMPLETE, this);
    }

    @Override
    public void notified(InfiniteEvent event) {
        RuntimeContext context = (RuntimeContext) event.getPayload();
        BaseElement curElement = context.getCurElement();
        if (!ElementTypeEnum.USER_TASK.getType().equals(curElement.getType())) {
            return;
        }
        AuditProcessInstance processInstance = context.getAuditProcessInstance();
        AuditTaskInstance auditTaskInstance = context.getCurAuditTaskInstance();
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PROCESS.getCode())) {
            processService.auditExecutionSuccessCallBack(processInstance.getBusinessKey(),
                    auditTaskInstance.getComment(), auditTaskInstance.getRemark(), SysUserHolder.getUser().getUserId(),
                    curElement.getName());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.RECODE.getCode())) {
            versionService.auditRecordExecutionSuccessCallBack(processInstance.getBusinessKey(), auditTaskInstance.getRemark(),
                    SysUserHolder.getUser().getUserId(), curElement.getName(), auditTaskInstance.getComment());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PRODUCT_PLAN.getCode())) {
            planService.auditPlanLog(processInstance.getBusinessKey(), auditTaskInstance.getRemark(), SysUserHolder.getUser().getUserId(),
                    curElement.getName(), auditTaskInstance.getComment());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PRODUCT_FORMULA.getCode())) {
            productFormulaConfigureService.auditNodeProductLog(processInstance.getBusinessKey(), auditTaskInstance.getRemark(),
                    SysUserHolder.getUser().getUserId(), curElement.getName(), auditTaskInstance.getComment());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_START.getCode()) ||
                StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode())) {
            ruleVersionService.auditOperateRuleNodeLog(processInstance.getBusinessKey(), auditTaskInstance.getRemark(),
                    SysUserHolder.getUser().getUserId(), curElement.getName(), auditTaskInstance.getComment());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.BATCH_RECORD_ARCHIVE.getCode())) {
            batchRecordArchiveService.auditCallBack(ArchiveAuditCallBackDTO.builder()
                    .archiveId(Long.valueOf(processInstance.getBusinessKey()))
                    .auditOpinion(auditTaskInstance.getComment())
                    .elementName(curElement.getName())
                    .build());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.BATCH_SIGNATURE.getCode())) {
            lotReleaseService.auditCallback(Long.parseLong(processInstance.getBusinessKey()),
                    null,
                    auditTaskInstance.getComment(),
                    SysUserHolder.getUser().getUserId());
        }
        //发送消息--当最后一个节点之后是任务完成时不发送消息
        if (StrUtil.isNotBlank(context.getUserTaskKey())) {
            SendMessageDTO dto = new SendMessageDTO();
            dto.setNodeName(I18nUtils.getCodeMessage(AuditMessageConstant.AUDIT_NODE_COMPLETE_I18N_CODE, AuditMessageConstant.AUDIT_NODE_COMPLETE, null) + auditTaskInstance.getElementName());
            dto.setNodeId(context.getUserTaskKey());
            dto.setDeploymentId(processInstance.getDeploymentId());
            dto.setAuditCategoryCode(processInstance.getCategory());
            dto.setBusinessId(Long.valueOf(processInstance.getBusinessKey()));
            dto.setComment(auditTaskInstance.getComment());
            dto.setRemark(auditTaskInstance.getRemark());
            dto.setIsStart(Boolean.FALSE);
            AuditMessageSendUtils.sendMessage(dto);
        }
    }
}
