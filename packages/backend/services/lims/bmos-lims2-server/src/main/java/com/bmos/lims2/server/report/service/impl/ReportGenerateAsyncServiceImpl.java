package com.bmos.lims2.server.report.service.impl;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.enums.ReportGenerateStatusEnum;
import com.bmos.lims2.common.enums.ReportOperationTypeEnum;
import com.bmos.lims2.server.config.minio.MinioProperties;
import com.bmos.lims2.server.report.entity.ReportGenerateTask;
import com.bmos.lims2.server.report.entity.ReportTemplateOperationHistory;
import com.bmos.lims2.server.report.mapper.ReportGenerateTaskMapper;
import com.bmos.lims2.server.report.mapper.ReportTemplateOperationHistoryMapper;
import com.bmos.lims2.server.report.service.ReportGenerateAsyncService;
import com.bmos.lims2.server.report.service.ReportRenderContext;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.server.report.service.ReportDocTemplateProcessor;
import com.bmos.lims2.server.report.service.StorageService;
import com.bmos.lims2.server.report.entity.ReportValidationTask;
import com.bmos.lims2.server.report.mapper.ReportValidationTaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PreDestroy;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportGenerateAsyncServiceImpl implements ReportGenerateAsyncService {

    private final ReportGenerateTaskMapper generateTaskMapper;
    private final ReportTemplateOperationHistoryMapper historyMapper;
    private final ReportValidationTaskMapper validationTaskMapper;
    private final StorageService storageService;
    private final AuditOperationLogService auditOperationLogService;
    private final ReportDocTemplateProcessor reportDocTemplateProcessor;
    private final MinioProperties minioProperties;
    private final com.bmos.lims2.server.operate.mapper.OperateRuleVersionMapper operateRuleVersionMapper;

    private final ThreadPoolExecutor validationExecutor = new ThreadPoolExecutor(
            4,
            8,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(200),
            new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("report-validate-" + threadIndex.getAndIncrement());
                    t.setDaemon(true);
                    return t;
                }
            },
            new AbortPolicy()
    );

    private final ThreadPoolExecutor generateExecutor = new ThreadPoolExecutor(
            4,
            8,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(200),
            new ThreadFactory() {
                private final AtomicInteger threadIndex = new AtomicInteger(1);
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setName("report-generate-" + threadIndex.getAndIncrement());
                    t.setDaemon(true);
                    return t;
                }
            },
            new AbortPolicy()
    );

    @Override
    public void executeValidation(Long validationTaskId) {
        SysUser user = SysUserHolder.getUser();
        validationExecutor.execute(() -> {
            SysUserHolder.setUser(user);
            ReportValidationTask task = validationTaskMapper.selectById(validationTaskId);
            if (task == null) {
                return;
            }

            ReportValidationTask running = new ReportValidationTask();
            running.setId(validationTaskId);
            running.setStatus(ReportGenerateStatusEnum.RUNNING);
            validationTaskMapper.updateById(running);

            try {
                ReportRenderContext ctx = new ReportRenderContext();

                // 通过存储的操作规程版本ID查询规程信息，构建检验依据渲染字符串
                if (cn.hutool.core.util.StrUtil.isNotBlank(task.getOperateVersionIds())) {
                    java.util.List<Long> ids = java.util.Arrays.stream(task.getOperateVersionIds().split(";"))
                            .filter(cn.hutool.core.util.StrUtil::isNotBlank)
                            .map(Long::valueOf)
                            .collect(java.util.stream.Collectors.toList());
                    if (!ids.isEmpty()) {
                        java.util.List<com.bmos.lims2.server.operate.vo.OperateRuleVersionDetailsVO> rules =
                                operateRuleVersionMapper.selectBriefByIds(ids);
                        if (rules != null && !rules.isEmpty()) {
                            String basis = rules.stream()
                                    .map(r -> cn.hutool.core.util.StrUtil.nullToDefault(r.getName(), "") + "-"
                                            + cn.hutool.core.util.StrUtil.nullToDefault(r.getCode(), "") + "-"
                                            + cn.hutool.core.util.StrUtil.nullToDefault(r.getVersion(), ""))
                                    .collect(java.util.stream.Collectors.joining(";"));
                            ctx.setInspectBasis(basis);
                        }
                    }
                }

                byte[] bytes = reportDocTemplateProcessor.renderPdfWithContext(task.getTemplateVersionId(), task.getInspectionOrderId(), ctx);

                String fileName = "validation-" + validationTaskId + ".pdf";
                String bucket = minioProperties.getBuckets().getReportVersion();
                String outputObject = "report-validation/" + validationTaskId + "/" + fileName;
                storageService.upload(bucket, outputObject, new ByteArrayInputStream(bytes), bytes.length, "application/pdf");

                ReportValidationTask success = new ReportValidationTask();
                success.setId(validationTaskId);
                success.setStatus(ReportGenerateStatusEnum.SUCCESS);
                success.setPath(outputObject);
                success.setEndTime(LocalDateTime.now());
                validationTaskMapper.updateById(success);

                ReportTemplateOperationHistory h = new ReportTemplateOperationHistory();
                h.setTemplateVersionId(task.getTemplateVersionId());
                h.setOperationType(ReportOperationTypeEnum.VALIDATE);
                if (user != null) {
                    h.setOperatorId(user.getUserId());
                    h.setOperatorName(StrUtil.emptyToDefault(user.getUserName(), user.getLoginName()));
                }
                h.setOperateTime(LocalDateTime.now());
                h.setPath(outputObject);
                historyMapper.insert(h);
                log.info("报告验证成功: " + outputObject);

            } catch (Exception e) {
                log.error("报告验证失败: " + e.getMessage(), e);
                ReportValidationTask failed = new ReportValidationTask();
                failed.setId(validationTaskId);
                failed.setStatus(ReportGenerateStatusEnum.FAILED);
                failed.setEndTime(LocalDateTime.now());
                failed.setMessage(e.getMessage());
                validationTaskMapper.updateById(failed);

                ReportTemplateOperationHistory h = new ReportTemplateOperationHistory();
                h.setTemplateVersionId(task.getTemplateVersionId());
                h.setOperationType(ReportOperationTypeEnum.VALIDATE);
                if (user != null) {
                    h.setOperatorId(user.getUserId());
                    h.setOperatorName(StrUtil.emptyToDefault(user.getUserName(), user.getLoginName()));
                }
                h.setOperateTime(LocalDateTime.now());
                historyMapper.insert(h);
            }
        });
    }

    @Override
    public void executeGenerate(Long generateTaskId) {
        SysUser user = SysUserHolder.getUser();
        generateExecutor.execute(() -> {
            SysUserHolder.setUser(user);
            ReportGenerateTask task = generateTaskMapper.selectById(generateTaskId);
            if (task == null) {
                return;
            }

            ReportGenerateTask running = new ReportGenerateTask();
            running.setId(generateTaskId);
            running.setStatus(ReportGenerateStatusEnum.RUNNING);
            generateTaskMapper.updateById(running);

            try {
                ReportRenderContext ctx = new ReportRenderContext();
                ctx.setReportNo(task.getReportNo());

                // 通过存储的操作规程版本ID查询规程信息，构建检验依据渲染字符串
                if (cn.hutool.core.util.StrUtil.isNotBlank(task.getOperateVersionIds())) {
                    java.util.List<Long> ids = java.util.Arrays.stream(task.getOperateVersionIds().split(";"))
                            .filter(cn.hutool.core.util.StrUtil::isNotBlank)
                            .map(Long::valueOf)
                            .collect(java.util.stream.Collectors.toList());
                    if (!ids.isEmpty()) {
                        java.util.List<com.bmos.lims2.server.operate.vo.OperateRuleVersionDetailsVO> rules =
                                operateRuleVersionMapper.selectBriefByIds(ids);
                        if (rules != null && !rules.isEmpty()) {
                            String basis = rules.stream()
                                    .map(r -> cn.hutool.core.util.StrUtil.nullToDefault(r.getName(), "") + "-"
                                            + cn.hutool.core.util.StrUtil.nullToDefault(r.getCode(), "") + "-"
                                            + cn.hutool.core.util.StrUtil.nullToDefault(r.getVersion(), ""))
                                    .collect(java.util.stream.Collectors.joining(";"));
                            ctx.setInspectBasis(basis);
                        }
                    }
                }

                // 1. 渲染DOCX快照（冻结数据点/基础信息/自定义字段，可变占位符保留为文本）
                byte[] docxBytes = reportDocTemplateProcessor.renderDocxSnapshot(task.getTemplateVersionId(), task.getInspectionOrderId(), ctx);

                // 2. 保存DOCX快照（后续重渲染从此加载）
                String bucket = minioProperties.getBuckets().getReportVersion();
                String snapshotObject = "report-version/" + generateTaskId + "/snapshot.docx";
                storageService.upload(bucket, snapshotObject, new ByteArrayInputStream(docxBytes), docxBytes.length,
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

                // 3. path 先指向快照（真实存在的文件），正式 PDF 在审批完成时生成并更新 path
                ReportGenerateTask success = new ReportGenerateTask();
                success.setId(generateTaskId);
                success.setStatus(ReportGenerateStatusEnum.SUCCESS);
                success.setPath(snapshotObject);          // 快照路径，以 .docx 结尾可作区分
                success.setDocxSnapshotPath(snapshotObject);  // 供重渲染始终引用
                success.setEndTime(LocalDateTime.now());
                generateTaskMapper.updateById(success);
//                h.setRemark("报告生成成功");
                log.info("报告生成成功，快照路径: {}", snapshotObject);
                // 记录报告生成操作历史（参照方案版本历史实现）
                try {
                    String userId = SysUserHolder.getUser() != null ? SysUserHolder.getUser().getUserId() : null;
                    auditOperationLogService.save(AuditOperationLogEntity.builder()
                            .businessId(task.getId())
                            .module(AuditBusinessModule.REPORT_AUDIT.name())
                            .operationType(OperationType.SAVE.getValue())
                            .createBy(userId)
                            .build());
                } catch (Exception e) {
                    log.warn("记录报告生成成功历史失败", e);
                }

            } catch (Exception e) {
                log.error("报告生成失败: " + e.getMessage(), e);
                ReportGenerateTask failed = new ReportGenerateTask();
                failed.setId(generateTaskId);
                failed.setStatus(ReportGenerateStatusEnum.FAILED);
                failed.setEndTime(LocalDateTime.now());
                generateTaskMapper.updateById(failed);
                // 记录报告生成操作历史（参照方案版本历史实现）
                try {
                    String userId = SysUserHolder.getUser() != null ? SysUserHolder.getUser().getUserId() : null;
                    auditOperationLogService.save(AuditOperationLogEntity.builder()
                            .businessId(task.getId())
                            .module(AuditBusinessModule.REPORT_AUDIT.name())
                            .operationType(OperationType.SAVE.getValue())
                            .remark("报告生成失败: " + (e.getMessage() != null ? e.getMessage() : ""))
                            .createBy(userId)
                            .build());
                } catch (Exception ex) {
                    log.warn("记录报告生成失败历史失败", ex);
                }
            }
        });
    }

    @PreDestroy
    public void shutdownValidationExecutor() {
        validationExecutor.shutdown();
    }

    @PreDestroy
    public void shutdownGenerateExecutor() {
        generateExecutor.shutdown();
    }
}


