package com.bmos.lims2.server.audit.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.context.RuntimeContext;
import com.bmos.audit.engine.core.listener.InfiniteEvent;
import com.bmos.audit.engine.core.listener.InfiniteEventListener;
import com.bmos.audit.engine.core.listener.InfiniteEventType;
import com.bmos.audit.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.audit.engine.core.model.AuditProcessInstance;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.service.BatchRecordVersionService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeVersionService;
import com.bmos.lims2.server.operate.enums.OperateRuleVersionStateEnum;
import com.bmos.lims2.server.operate.model.OperateRuleVersion;
import com.bmos.lims2.server.operate.service.OperateRuleVersionService;
import com.bmos.lims2.server.stability.review.service.StabilityResultReviewService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 检验方案审批流程结束监听器
 * @author system
 */
@Slf4j
@Component
public class AuditFlowProcessEndListener implements InfiniteEventListener {

    @Autowired
    private InspectionSchemeVersionService inspectionSchemeVersionService;

    @Autowired
    private com.bmos.lims2.server.inspect.audit.service.SampleAuditService sampleAuditService;

    @Autowired
    private com.bmos.lims2.server.report.service.ReportApprovalService reportApprovalService;

    @Autowired
    private com.bmos.lims2.server.report.service.ReportTemplateService reportTemplateService;


    @Autowired
    private OperateRuleVersionService ruleVersionService;
    @Autowired
    private BatchRecordVersionService versionService;
    @Autowired
    private StabilitySchemeVersionService stabilitySchemeVersionService;
    @Autowired
    private StabilityResultReviewService stabilityResultReviewService;
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    @PostConstruct
    public void init() {
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.PROCESS_END, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void notified(InfiniteEvent event) {
        Map<String, Object> payload = (Map<String, Object>) event.getPayload();
        AuditProcessInstance processInstance = (AuditProcessInstance) payload.get("context");
        String comment = (String) payload.get("comment");
        String remark = (String) payload.get("remark");
        String nodeName = (String) payload.get("nodeName");

        // 只处理检验方案相关的审批
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.SCHEME_AUDIT.getCode())) {
            log.info("检验方案审批流程结束，流程实例ID: {}, 业务键: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey());

            inspectionSchemeVersionService.auditProcessSuccessCallBack(
                    processInstance.getProcessInstanceId(),
                    comment,
                    SysUserHolder.getUser().getUserId()
            );
        }
        // 稳定性方案审批流程结束（通过）
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.STABILITY_SCHEME_AUDIT.getCode())) {
            log.info("稳定性方案审批流程结束，流程实例ID: {}, 业务键: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey());
            stabilitySchemeVersionService.auditProcessSuccessCallBack(
                    processInstance.getProcessInstanceId(),
                    comment,
                    SysUserHolder.getUser().getUserId()
            );
        }
        // 样品审核流程结束（通过）
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.SAMPLE_AUDIT.getCode())) {
            log.info("样品审核流程结束，流程实例ID: {}, 业务键: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey());
            sampleAuditService.auditProcessSuccessCallBack(
                    processInstance.getProcessInstanceId(),
                    comment,
                    remark,
                    SysUserHolder.getUser().getUserId(),
                    nodeName
            );
        }
        // 稳定性结果审核流程结束（通过）
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.STABILITY_RESULT_AUDIT.getCode())) {
            log.info("稳定性结果审核流程结束，流程实例ID: {}, 业务键: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey());
            stabilityResultReviewService.auditProcessSuccessCallBack(
                    processInstance.getProcessInstanceId(),
                    comment,
                    remark,
                    SysUserHolder.getUser().getUserId(),
                    nodeName
            );
        }
        // 报告审批流程结束（通过）：报告模板版本与检验报告均使用 REPORT_AUDIT 类别
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.REPORT_AUDIT.getCode())) {
            log.info("报告审批流程结束，流程实例ID: {}, 业务键: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey());


            // 触发检验报告审批通过回调（与现有逻辑保持一致）
            reportApprovalService.auditProcessSuccessCallBack(
                    processInstance.getProcessInstanceId(),
                    comment,
                    remark,
                    SysUserHolder.getUser().getUserId()
            );
        }
        else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.METHOD_AUDIT.getCode())) {
            versionService.auditRecordSuccessCallBack(processInstance.getProcessInstanceId(),
                    comment, SysUserHolder.getUser().getUserId());
        }
        else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode())) {
            OperateRuleVersion version = ruleVersionService.selectById(Long.valueOf(processInstance.getBusinessKey()));
            version.setState(OperateRuleVersionStateEnum.INVALID.getCode());
            ruleVersionService.flowUpdateVersion(version);
        }
        else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_START.getCode())) {
            try {
                OperateRuleVersion version = ruleVersionService.selectById(Long.valueOf(processInstance.getBusinessKey()));
                Map<String, OperateRuleVersion> stateMap = CollectionUtils.convertMap(ruleVersionService.selectByOperateRuleId(version.getOperateId()), OperateRuleVersion::getState);
                OperateRuleVersion historyValidVersion = stateMap.get(OperateRuleVersionStateEnum.VALID.getCode());
                if (ObjectUtil.isNotEmpty(historyValidVersion)) {
                    historyValidVersion.setState(OperateRuleVersionStateEnum.INVALID.getCode());
                    ruleVersionService.flowUpdateVersion(historyValidVersion);
                }
                version.setState(OperateRuleVersionStateEnum.VALID.getCode());
                version.setEffectDate(FORMATTER.format(new Date()));
                ruleVersionService.flowUpdateVersion(version);
            } catch (Exception e) {
                throw new BmosException(LimsResponseCode.OPERATE_VERSION_FLOW_ERROR);
            }
        }
    }
}