package com.bmos.lims2.server.audit.listener;

import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.context.RuntimeContext;
import com.bmos.audit.engine.core.element.base.BaseElement;
import com.bmos.audit.engine.core.listener.InfiniteEvent;
import com.bmos.audit.engine.core.listener.InfiniteEventListener;
import com.bmos.audit.engine.core.listener.InfiniteEventType;
import com.bmos.audit.engine.core.listener.InfiniteProcessEngineListenerHelper;
import com.bmos.audit.engine.core.model.AuditProcessInstance;
import com.bmos.audit.engine.core.model.AuditTaskInstance;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.AuditCategoryCodeEnum;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.eln.record.service.BatchRecordVersionService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeVersionService;
import com.bmos.lims2.server.operate.service.OperateRuleVersionService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 检验方案审批节点执行监听器
 * @author system
 */
@Slf4j
@Component
public class AuditFlowNodeExecutionListener implements InfiniteEventListener {

    @Autowired
    private InspectionSchemeVersionService inspectionSchemeVersionService;

    @Autowired
    private AuditOperationLogService auditOperationLogService;

    @Autowired
    private BatchRecordVersionService versionService;

    @Autowired
    private OperateRuleVersionService ruleVersionService;

    @Autowired
    private StabilitySchemeVersionService stabilitySchemeVersionService;


    @PostConstruct
    public void init() {
        InfiniteProcessEngineListenerHelper.addListener(InfiniteEventType.EXECUTION_NODE_COMPLETE, this);
    }

    @Override
    public void notified(InfiniteEvent event) {
        RuntimeContext context =(RuntimeContext) event.getPayload();
        AuditProcessInstance processInstance = context.getAuditProcessInstance();
        AuditTaskInstance auditTaskInstance = context.getCurAuditTaskInstance();
        BaseElement curElement = context.getCurElement();
        String nodeName = StrUtil.isBlank(context.getCurElement().getName()) ? null : context.getCurElement().getName();

        // 只处理检验方案相关的审批
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.SCHEME_AUDIT.getCode())) {
            log.info("检验方案审批节点执行完成，流程实例ID: {}, 业务键: {}, 节点: {}", 
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            
            inspectionSchemeVersionService.auditExecutionSuccessCallBack(
                    processInstance.getBusinessKey(),
                    auditTaskInstance.getComment(),
                    auditTaskInstance.getRemark(),
                    SysUserHolder.getUser().getUserId(),
                    nodeName
            );
        }

        // 样品审核：节点执行完成，记录操作日志
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.SAMPLE_AUDIT.getCode())) {
            log.info("样品审核节点执行完成，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            AuditOperationLogEntity entity = AuditOperationLogEntity.builder()
                    .module(AuditBusinessModule.SAMPLE_AUDIT.name())
                    .businessId(Long.valueOf(processInstance.getBusinessKey()))
                    .operationType(OperationType.APPROVE_AUDIT.getValue())
                    .remark(auditTaskInstance.getRemark())
                    .nodeName(nodeName)
                    .comment(auditTaskInstance.getComment())
                    .createBy(SysUserHolder.getUser().getUserId())
                    .build();
            auditOperationLogService.save(entity);
        }

        // 报告审批：节点执行完成，记录操作日志并重渲染报告
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.REPORT_AUDIT.getCode())) {
            log.info("报告审批节点执行完成，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            AuditOperationLogEntity entity = AuditOperationLogEntity.builder()
                    .module(AuditBusinessModule.REPORT_AUDIT.name())
                    .businessId(Long.valueOf(processInstance.getBusinessKey()))
                    .operationType(OperationType.APPROVE_AUDIT.getValue())
                    .remark(auditTaskInstance.getRemark())
                    .nodeName(nodeName)
                    .comment(auditTaskInstance.getComment())
                    .createBy(SysUserHolder.getUser().getUserId())
                    .build();
            auditOperationLogService.save(entity);
            // 节点完成后重渲染报告（填充已完成的审批节点信息）
            try {
                com.bmos.lims2.server.report.service.ReportApprovalService reportApprovalService =
                        cn.hutool.extra.spring.SpringUtil.getBean(com.bmos.lims2.server.report.service.ReportApprovalService.class);
                reportApprovalService.auditNodeCompleteCallback(
                        processInstance.getProcessInstanceId(),
                        processInstance.getBusinessKey(),
                        nodeName,
                        SysUserHolder.getUser().getUserId());
            } catch (Exception e) {
                log.warn("报告审批节点完成后重渲染失败", e);
            }
        }
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.METHOD_AUDIT.getCode())) {
            versionService.auditRecordExecutionSuccessCallBack(processInstance.getBusinessKey(), auditTaskInstance.getRemark(),
                    SysUserHolder.getUser().getUserId(),curElement.getName(),auditTaskInstance.getComment());
        }
        if (StrUtil.equals(processInstance.getCategory(),AuditCategoryCodeEnum.OPERATE_RULE_START.getCode()) ||
                StrUtil.equals(processInstance.getCategory(),AuditCategoryCodeEnum.OPERATE_RULE_BLOCK.getCode())){
            ruleVersionService.auditOperateRuleNodeLog(processInstance.getBusinessKey(),auditTaskInstance.getRemark(),
                    SysUserHolder.getUser().getUserId(),curElement.getName(),auditTaskInstance.getComment());
        }

        // 稳定性方案审核：节点执行完成，记录操作日志
        if (StrUtil.equals(processInstance.getCategory(), AuditCategoryCodeEnum.STABILITY_SCHEME_AUDIT.getCode())) {
            log.info("稳定性方案审核节点执行完成，流程实例ID: {}, 业务键: {}, 节点: {}",
                    processInstance.getProcessInstanceId(), processInstance.getBusinessKey(), nodeName);
            stabilitySchemeVersionService.auditExecutionSuccessCallBack(
                    processInstance.getBusinessKey(),
                    auditTaskInstance.getComment(),
                    auditTaskInstance.getRemark(),
                    SysUserHolder.getUser().getUserId(),
                    nodeName
            );
        }
    }
}