package com.bmos.mes.service.audit.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.listener.InfiniteEvent;
import com.bmos.audit.engine.core.listener.InfiniteEventListener;
import com.bmos.audit.engine.core.listener.InfiniteEventType;
import com.bmos.audit.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.audit.engine.core.model.AuditProcessInstance;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.mes.common.enums.audit.AuditCategoryCodeEnum;
import com.bmos.mes.common.enums.operate.OperateRuleVersionStateEnum;
import com.bmos.mes.common.exception.MesResponseCode;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 整个流程结束任务
 */
@Component
public class AuditFlowProcessEndListener implements InfiniteEventListener {

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

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
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.PROCESS_END, this);
    }

    @Override
    public void notified(InfiniteEvent event) {
        Map<String, Object> payload = (Map<String, Object>) event.getPayload();
        AuditProcessInstance processInstance = (AuditProcessInstance) payload.get("context");
        String comment = (String) payload.get("comment");
        String remark = (String) payload.get("remark");
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PROCESS.getCode())) {
            processService.auditProcessSuccessCallBack(processInstance.getProcessInstanceId(),
                    comment, SysUserHolder.getUser().getUserId());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PRODUCT_PLAN.getCode())) {
            planService.auditSuccess(processInstance.getProcessInstanceId());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.RECODE.getCode())) {
            versionService.auditRecordSuccessCallBack(processInstance.getProcessInstanceId(),
                    comment, SysUserHolder.getUser().getUserId());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.PRODUCT_FORMULA.getCode())) {
            productFormulaConfigureService.auditSuccess(processInstance.getProcessInstanceId(),
                    comment, SysUserHolder.getUser().getUserId());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode())) {
            OperateRuleVersion version = ruleVersionService.selectById(Long.valueOf(processInstance.getBusinessKey()));
            version.setState(OperateRuleVersionStateEnum.INVALID.getCode());
            ruleVersionService.flowUpdateVersion(version);
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_START.getCode())) {
            try {
                OperateRuleVersion version = ruleVersionService.selectById(Long.valueOf(processInstance.getBusinessKey()));
                //如果生效时间是-是立即生效
                if (StrUtil.equals(version.getEffectDate(), StrUtil.DASHED) || FORMATTER.parse(version.getEffectDate()).compareTo(FORMATTER.parse(FORMATTER.format(new Date()))) <= 0) {
                    Map<String, OperateRuleVersion> stateMap = CollectionUtils.convertMap(ruleVersionService.selectByOperateRuleId(version.getOperateId()), OperateRuleVersion::getState);
                    OperateRuleVersion historyValidVersion = stateMap.get(OperateRuleVersionStateEnum.VALID.getCode());
                    if (ObjectUtil.isNotEmpty(historyValidVersion)) {
                        historyValidVersion.setState(OperateRuleVersionStateEnum.INVALID.getCode());
                        ruleVersionService.flowUpdateVersion(historyValidVersion);
                    }
                    version.setState(OperateRuleVersionStateEnum.VALID.getCode());
                    version.setEffectDate(FORMATTER.format(new Date()));
                } else {
                    version.setState(OperateRuleVersionStateEnum.WAIT_VALID.getCode());
                }
                ruleVersionService.flowUpdateVersion(version);
            } catch (Exception e) {
                throw new BmosException(MesResponseCode.OPERATE_VERSION_FLOW_ERROR);
            }
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.BATCH_RECORD_ARCHIVE.getCode())) {
            batchRecordArchiveService.auditCallBack(ArchiveAuditCallBackDTO.builder().archiveId(Long.valueOf(processInstance.getBusinessKey()))
                    .auditResult(true).auditOpinion(comment).elementName(processInstance.getName()).build());
        } else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.BATCH_SIGNATURE.getCode())) {
            lotReleaseService.auditCallback(Long.parseLong(processInstance.getBusinessKey()),
                    true,
                    comment,
                    SysUserHolder.getUser().getUserId());
        }
        SendMessageDTO dto = new SendMessageDTO();
        dto.setBusinessId(Long.valueOf(processInstance.getBusinessKey()));
        dto.setNodeName(I18nUtils.getCodeMessage(AuditMessageConstant.AUDIT_COMPLETE_I18N_CODE, AuditMessageConstant.AUDIT_COMPLETE, null)
                + I18nUtils.getEnumMessage(AuditCategoryServiceEnum.getEnumByCode(processInstance.getCategory())));
        dto.setAuditCategoryCode(processInstance.getCategory());
        dto.setComment(comment);
        dto.setRemark(remark);
        dto.setIsStart(Boolean.FALSE);
        AuditMessageSendUtils.sendMessage(dto);
    }
}
