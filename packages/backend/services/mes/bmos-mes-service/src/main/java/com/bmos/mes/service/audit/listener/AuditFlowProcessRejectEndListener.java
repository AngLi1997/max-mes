package com.bmos.mes.service.audit.listener;

import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.context.RuntimeContext;
import com.bmos.audit.engine.core.listener.InfiniteEvent;
import com.bmos.audit.engine.core.listener.InfiniteEventListener;
import com.bmos.audit.engine.core.listener.InfiniteEventType;
import com.bmos.audit.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.audit.engine.core.model.AuditProcessInstance;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.service.audit.builder.AuditCategoryServiceEnum;
import com.bmos.mes.service.audit.constant.AuditMessageConstant;
import com.bmos.mes.service.audit.dto.SendMessageDTO;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.lotrelease.manage.service.ILotReleaseService;
import com.bmos.mes.service.operate.model.OperateRuleVersion;
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
import java.util.Map;

/**
 * 审批不通过结束
 */
@Component
public class AuditFlowProcessRejectEndListener implements InfiniteEventListener {

    @Autowired
    private ProcessService processService;

    @Autowired
    private PlanService planService;

    @Autowired
    private BatchRecordVersionService versionService;

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
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.PROCESS_REJECT_END, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void notified(InfiniteEvent event) {
        Map<String, Object> payload = (Map<String, Object>) event.getPayload();
        RuntimeContext context = (RuntimeContext) payload.get("context");
        AuditProcessInstance processInstance = context.getAuditProcessInstance();
        String comment = (String) payload.get("comment");
        String remark = (String) payload.get("remark");
        String nodeName = StrUtil.isBlank(context.getCurElement().getName()) ? null : context.getCurElement().getName();
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PROCESS.getCode())) {
            processService.auditProcessRejectCallBack(processInstance.getProcessInstanceId(),
                    comment, remark, SysUserHolder.getUser().getUserId(), nodeName);
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PRODUCT_PLAN.getCode())) {
            planService.auditTermination(remark, processInstance.getProcessInstanceId(), nodeName, comment, Long.valueOf(processInstance.getBusinessKey()));
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.RECODE.getCode())) {
            versionService.auditRecordRejectCallBack(processInstance.getProcessInstanceId(),
                    comment, remark, nodeName, SysUserHolder.getUser().getUserId());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PRODUCT_FORMULA.getCode())) {
            productFormulaConfigureService.auditTermination(processInstance.getProcessInstanceId(),
                    comment, remark, SysUserHolder.getUser().getUserId(), nodeName);
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode()) ||
                StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_START.getCode())) {
            OperateRuleVersion version = ruleVersionService.selectById(Long.valueOf(processInstance.getBusinessKey()));
            version.setState(version.getHistoryState());
            ruleVersionService.flowUpdateVersion(version);
            ruleVersionService.rejectOperateRuleHistoryLog(processInstance.getBusinessKey(),
                    comment, remark, SysUserHolder.getUser().getUserId(), nodeName);
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.BATCH_RECORD_ARCHIVE.getCode())) {
            batchRecordArchiveService.auditCallBack(ArchiveAuditCallBackDTO.builder()
                    .archiveId(Long.valueOf(processInstance.getBusinessKey()))
                    .auditResult(false)
                    .auditOpinion(comment)
                    .elementName(nodeName)
                    .build());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.BATCH_SIGNATURE.getCode())) {
            lotReleaseService.auditCallback(Long.parseLong(processInstance.getBusinessKey()),
                    false,
                    comment,
                    SysUserHolder.getUser().getUserId());
        }
        SendMessageDTO dto = new SendMessageDTO();
        dto.setBusinessId(Long.valueOf(processInstance.getBusinessKey()));
        dto.setNodeName(I18nUtils.getCodeMessage(AuditMessageConstant.AUDIT_NOT_COMPLETE_I18N_CODE, AuditMessageConstant.AUDIT_NOT_COMPLETE, null) + I18nUtils.getEnumMessage(AuditCategoryServiceEnum.getEnumByCode(processInstance.getCategory())));
        dto.setAuditCategoryCode(processInstance.getCategory());
        dto.setComment(comment);
        dto.setRemark(remark);
        dto.setIsStart(Boolean.FALSE);
        AuditMessageSendUtils.sendMessage(dto);
    }
}
