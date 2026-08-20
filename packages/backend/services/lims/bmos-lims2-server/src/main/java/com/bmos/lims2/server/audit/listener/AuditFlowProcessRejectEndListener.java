package com.bmos.lims2.server.audit.listener;

import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.context.RuntimeContext;
import com.bmos.audit.engine.core.listener.InfiniteEvent;
import com.bmos.audit.engine.core.listener.InfiniteEventListener;
import com.bmos.audit.engine.core.listener.InfiniteEventType;
import com.bmos.audit.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.audit.engine.core.model.AuditProcessInstance;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.server.eln.record.service.BatchRecordVersionService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeVersionService;
import com.bmos.lims2.server.operate.model.OperateRuleVersion;
import com.bmos.lims2.server.operate.service.OperateRuleVersionService;
import com.bmos.lims2.server.stability.review.service.StabilityResultReviewService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 检验方案审批流程拒绝结束监听器
 * @author system
 */
@Slf4j
@Component
public class AuditFlowProcessRejectEndListener implements InfiniteEventListener {

    @Autowired
    private InspectionSchemeVersionService inspectionSchemeVersionService;

    @Autowired
    private com.bmos.lims2.server.report.service.ReportApprovalService reportApprovalService;

    @Autowired
    private com.bmos.lims2.server.report.service.ReportTemplateService reportTemplateService;

    @Autowired
    private com.bmos.lims2.server.inspect.audit.service.SampleAuditService sampleAuditService;

    @Autowired
    private BatchRecordVersionService versionService;

    @Autowired
    private OperateRuleVersionService ruleVersionService;

    @Autowired
    private StabilitySchemeVersionService stabilitySchemeVersionService;

    @Autowired
    private StabilityResultReviewService stabilityResultReviewService;

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

        // 样品审核拒绝结束
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.SAMPLE_AUDIT.getCode())) {
            log.info("样品审核流程拒绝结束，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            sampleAuditService.auditProcessRejectCallBack(
                    processInstance.getProcessInstanceId(),
                    comment,
                    remark,
                    SysUserHolder.getUser().getUserId(),
                    nodeName
            );
        }

        // 只处理检验方案相关的审批
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.SCHEME_AUDIT.getCode())) {
            log.info("检验方案审批流程拒绝结束，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);

            inspectionSchemeVersionService.auditProcessRejectCallBack(
                    processInstance.getProcessInstanceId(),
                    comment,
                    remark,
                    SysUserHolder.getUser().getUserId(),
                    nodeName
            );
        }
        // 稳定性方案审批流程拒绝结束
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.STABILITY_SCHEME_AUDIT.getCode())) {
            log.info("稳定性方案审批流程拒绝结束，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            stabilitySchemeVersionService.auditProcessRejectCallBack(
                    processInstance.getProcessInstanceId(),
                    comment,
                    remark,
                    SysUserHolder.getUser().getUserId(),
                    nodeName
            );
        }
        // 稳定性结果审核拒绝结束
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.STABILITY_RESULT_AUDIT.getCode())) {
            log.info("稳定性结果审核流程拒绝结束，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            stabilityResultReviewService.auditProcessRejectCallBack(
                    processInstance.getProcessInstanceId(),
                    comment,
                    remark,
                    SysUserHolder.getUser().getUserId(),
                    nodeName
            );
        }
        // 报告审批拒绝结束：报告模板版本与检验报告均使用 REPORT_AUDIT 类别
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.REPORT_AUDIT.getCode())) {
            log.info("报告审批流程拒绝结束，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            // 触发检验报告审批拒绝回调（与现有逻辑保持一致）
            reportApprovalService.auditProcessRejectCallBack(
                    processInstance.getProcessInstanceId(),
                    comment,
                    remark,
                    SysUserHolder.getUser().getUserId(),
                    nodeName
            );
        }
        else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.METHOD_AUDIT.getCode())) {
            versionService.auditRecordRejectCallBack(processInstance.getProcessInstanceId(),
                    comment, remark, nodeName, SysUserHolder.getUser().getUserId());
        }
        else if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode()) ||
                StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.OPERATE_RULE_START.getCode())) {
            OperateRuleVersion version = ruleVersionService.selectById(Long.valueOf(processInstance.getBusinessKey()));
            version.setState(version.getHistoryState());
            ruleVersionService.flowUpdateVersion(version);
            ruleVersionService.rejectOperateRuleHistoryLog(processInstance.getBusinessKey(),
                    comment, remark, SysUserHolder.getUser().getUserId(), nodeName);
        }
    }
}