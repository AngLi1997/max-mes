package com.bmos.lims2.server.report.service.impl;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FindReplaceOptions;
import com.bmos.lims2.common.constants.DictCodeConstant;
import com.bmos.lims2.server.config.minio.MinioProperties;
import com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO;
import com.bmos.lims2.server.inspect.entry.mapper.InspectionEntryRecordMapper;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionSamplingDTO;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderCustomFieldMapper;
import com.bmos.lims2.server.material.entity.MaterialField;
import com.bmos.lims2.server.material.mapper.MaterialFieldMapper;
import com.bmos.lims2.server.inspect.order.mapper.InspectionSamplingMapper;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.report.entity.ReportTemplateVersion;
import com.bmos.lims2.server.report.mapper.ReportTemplateVersionMapper;
import com.bmos.lims2.server.report.service.ReportDocTemplateProcessor;
import com.bmos.lims2.server.report.service.ReportRenderContext;
import com.bmos.lims2.server.report.service.StorageService;
import com.bmos.lims2.server.platform.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportDocTemplateProcessorImpl implements ReportDocTemplateProcessor {

    private final ReportTemplateVersionMapper versionMapper;
    private final StorageService storageService;
    private final InspectionEntryRecordMapper entryRecordMapper;
    private final InspectionOrderMapper inspectionOrderMapper;
    private final InspectionOrderCustomFieldMapper inspectionOrderCustomFieldMapper;
    private final InspectionSamplingMapper inspectionSamplingMapper;
    private final SampleMapper sampleMapper;
    private final MinioProperties minioProperties;
    private final MaterialFieldMapper materialFieldMapper;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 构建冻结文档：从模板+DB填充所有稳定字段。
     * 可变字段（结论/报告人/生成时间/审批节点）不加入替换表，保留为占位符文本，供后续 applyContext 使用。
     * reportNo 属于稳定字段，从 ctx 中取值一并填入。
     */
    private Document buildFrozenDocument(Long templateVersionId, Long inspectionOrderId, ReportRenderContext ctx) throws Exception {
        ReportTemplateVersion version = versionMapper.selectById(templateVersionId);
        String inputObject = version.getPath();

        try (InputStream templateStream = storageService.getObject(minioProperties.getBuckets().getReportTemplate(), inputObject)) {
            Document doc = new Document(templateStream);
            DocumentBuilder builder = new DocumentBuilder(doc);

            Map<String, String> placeholders = new HashMap<>();

            // ========== 数据点占位符 ==========
            List<InspectionEntryRecordDTO> records = entryRecordMapper.selectByInspectionOrderId(inspectionOrderId);
            if (records != null) {
                for (InspectionEntryRecordDTO r : records) {
                    String key = "${ITEM:" + r.getInspectItemCode() + "|PARAM:" + r.getParameterCode() + "|POINT:" + r.getDataPointName() + "}";
                    String val = r.getValueText() != null ? r.getValueText() : (r.getValueNumber() == null ? "" : r.getValueNumber());
                    placeholders.put(key, val);
                }
            }

            // ========== 检验单基础信息 ==========
            InspectionOrderDTO order = inspectionOrderMapper.selectByIdWithRelation(inspectionOrderId);
            if (order != null) {
                // 加载自定义字段（selectByIdWithRelation 不包含，需单独查询）
                java.util.List<com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField> customFieldEntities =
                        inspectionOrderCustomFieldMapper.selectByInspectionOrderId(inspectionOrderId);
                if (customFieldEntities != null && !customFieldEntities.isEmpty()) {
                    java.util.List<com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO> customFields = new java.util.ArrayList<>();
                    for (com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField entity : customFieldEntities) {
                        com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO dto = new com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO();
                        dto.setFieldCode(entity.getFieldCode());
                        dto.setFieldName(entity.getFieldName());
                        dto.setFieldValue(entity.getFieldValue());
                        dto.setDictCode(entity.getDictCode());
                        dto.setRequired(entity.getRequired());
                        customFields.add(dto);
                    }
                    order.setCustomFields(customFields);
                }
                // ORDER: 系列（向后兼容）
                placeholders.put("${ORDER:NO}", nvl(order.getOrderNo()));
                placeholders.put("${ORDER:BATCH}", nvl(order.getBatchNo()));
                placeholders.put("${ORDER:MATERIAL_NAME}", nvl(order.getMaterialName()));
                placeholders.put("${ORDER:MATERIAL_CODE}", nvl(order.getMaterialCode()));
                placeholders.put("${ORDER:SCHEME_NAME}", nvl(order.getSchemeName()));
                placeholders.put("${ORDER:SCHEME_VERSION}", nvl(order.getSchemeVersion()));
                placeholders.put("${ORDER:REQUEST_TIME}", order.getRequestTime() == null ? "" : DTF.format(order.getRequestTime()));
                placeholders.put("${ORDER:PRODUCTION_DATE}", order.getProductionDate() == null ? "" : DTF.format(order.getProductionDate()));

                // INFO: 基础信息
                placeholders.put("${INFO:MATERIAL_NAME}", nvl(order.getMaterialName()));
                placeholders.put("${INFO:MATERIAL_CODE}", nvl(order.getMaterialCode()));
                placeholders.put("${INFO:MATERIAL_SPEC}", nvl(order.getMaterialSpec()));
                placeholders.put("${INFO:BATCH_NO}", nvl(order.getBatchNo()));
                placeholders.put("${INFO:SCHEME_NAME}", nvl(order.getSchemeName()));
                placeholders.put("${INFO:SCHEME_CODE}", nvl(order.getSchemeVersion()));

                // 检验项目与分析项名称
                if (records != null && !records.isEmpty()) {
                    String inspectItems = records.stream()
                            .map(InspectionEntryRecordDTO::getInspectItemName)
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.joining("、"));
                    placeholders.put("${INFO:INSPECT_ITEMS}", inspectItems);

                    String parameters = records.stream()
                            .map(InspectionEntryRecordDTO::getParameterName)
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.joining("、"));
                    placeholders.put("${INFO:PARAMETERS}", parameters);
                } else {
                    placeholders.put("${INFO:INSPECT_ITEMS}", "");
                    placeholders.put("${INFO:PARAMETERS}", "");
                }

                // 报告编号（生成时已知，属于稳定字段）
                placeholders.put("${INFO:REPORT_NO}", nvl(ctx != null ? ctx.getReportNo() : null));
                // 检验依据（生成时由前端选择，已格式化为快照字符串）
                placeholders.put("${INFO:INSPECT_BASIS}", nvl(ctx != null ? ctx.getInspectBasis() : null));

                // CUSTOM: 自定义字段（检验单）
                if (order.getCustomFields() != null) {
                    for (com.bmos.lims2.server.inspect.order.dto.CustomFieldValueDTO cf : order.getCustomFields()) {
                        if (cf.getFieldCode() != null) {
                            placeholders.put("${CUSTOM:" + DictCodeConstant.INSPECTION_DOCUMENT_CUSTOM_FIELDS + ":" + cf.getFieldCode() + "}", nvl(cf.getFieldValue()));
                        }
                    }
                }

                // CUSTOM: 物料自定义字段
                if (order.getMaterialId() != null) {
                    List<MaterialField> matFields = materialFieldMapper.selectByMaterialId(order.getMaterialId());
                    if (matFields != null) {
                        for (MaterialField mf : matFields) {
                            if (mf.getField() != null) {
                                placeholders.put("${CUSTOM:" + DictCodeConstant.MATERIAL_CUSTOM_FIELDS + ":" + mf.getField() + "}", nvl(mf.getFieldValue()));
                            }
                        }
                    }
                }
            }

            // ========== 样品信息 ==========
            List<Sample> samples = sampleMapper.selectByInspectionOrderId(inspectionOrderId);
            if (samples != null && !samples.isEmpty()) {
                String samplingBy = samples.stream()
                        .map(Sample::getSamplerName)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.joining("、"));
                placeholders.put("${INFO:SAMPLING_BY}", samplingBy);

                samples.stream()
                        .map(Sample::getSamplingTime)
                        .filter(Objects::nonNull)
                        .max(Comparator.naturalOrder())
                        .ifPresent(t -> placeholders.put("${INFO:SAMPLING_TIME}", DTF.format(t)));
                placeholders.putIfAbsent("${INFO:SAMPLING_TIME}", "");

                samples.stream()
                        .map(Sample::getReceiveTime)
                        .filter(Objects::nonNull)
                        .max(Comparator.naturalOrder())
                        .ifPresent(t -> placeholders.put("${INFO:RECEIVE_TIME}", DTF.format(t)));
                placeholders.putIfAbsent("${INFO:RECEIVE_TIME}", "");
            } else {
                placeholders.put("${INFO:SAMPLING_BY}", "");
                placeholders.put("${INFO:SAMPLING_TIME}", "");
                placeholders.put("${INFO:RECEIVE_TIME}", "");
            }

            // 取样数量
            List<InspectionSamplingDTO> samplings = inspectionSamplingMapper.selectByInspectionOrderIdWithRelation(inspectionOrderId);
            if (samplings != null && !samplings.isEmpty()) {
                String samplingCount = samplings.stream()
                        .filter(s -> s.getPlannedQuantity() != null)
                        .map(s -> s.getPlannedQuantity() + (s.getUnitName() != null ? s.getUnitName() : ""))
                        .collect(Collectors.joining("、"));
                placeholders.put("${INFO:SAMPLING_COUNT}", samplingCount);
            } else {
                placeholders.put("${INFO:SAMPLING_COUNT}", "");
            }

            // 注意：${INFO:INSPECT_CONCLUSION}、${INFO:REPORT_GENERATE_TIME}、${INFO:REPORTER}、${Node_*}
            // 均为可变字段，此处不替换，保留为占位符文本，由 applyContextToDocument 在输出PDF时填入。

            FindReplaceOptions opts = new FindReplaceOptions();
            opts.setMatchCase(false);
            opts.setFindWholeWordsOnly(false);
            for (Map.Entry<String, String> e : placeholders.entrySet()) {
                builder.getDocument().getRange().replace(e.getKey(), e.getValue(), opts);
            }
            return doc;
        }
    }

    /**
     * 将可变上下文字段应用到已有 Document 上（不查 DB）。
     * 可变字段：检验结论、报告生成时间、报告人、审批节点。
     */
    private void applyContextToDocument(Document doc, ReportRenderContext ctx) throws Exception {
        Map<String, String> placeholders = new HashMap<>();

        placeholders.put("${INFO:INSPECT_CONCLUSION}", nvl(ctx.getInspectionConclusion()));
        placeholders.put("${INFO:REPORT_GENERATE_TIME}",
                ctx.getConfirmTime() != null ? DTF.format(ctx.getConfirmTime()) : "");

        String reporterName = ctx.getConfirmByName();
        if (reporterName == null && ctx.getConfirmBy() != null) {
            com.bmos.mybatis.dataobject.BaseUserDO user = UserUtils.getUser(ctx.getConfirmBy());
            reporterName = user != null ? user.getUserName() : ctx.getConfirmBy();
        }
        placeholders.put("${INFO:REPORTER}", nvl(reporterName));

        List<ReportRenderContext.ApprovalNodeContext> nodes = ctx.getApprovalNodes();
        if (nodes != null) {
            for (int i = 0; i < nodes.size(); i++) {
                int seq = i + 1;
                ReportRenderContext.ApprovalNodeContext node = nodes.get(i);
                placeholders.put("${Node_" + seq + "_USER}", nvl(node.getUserName()));
                placeholders.put("${Node_" + seq + "_TIME}",
                        node.getOperationTime() != null ? DTF.format(node.getOperationTime()) : "");
            }
        }

        FindReplaceOptions opts = new FindReplaceOptions();
        opts.setMatchCase(false);
        opts.setFindWholeWordsOnly(false);
        for (Map.Entry<String, String> e : placeholders.entrySet()) {
            doc.getRange().replace(e.getKey(), e.getValue(), opts);
        }
    }

    /** null 转空字符串 */
    private static String nvl(String s) {
        return s == null ? "" : s;
    }

    @Override
    public byte[] renderDocx(Long templateVersionId, Long inspectionOrderId) throws Exception {
        Document doc = buildFrozenDocument(templateVersionId, inspectionOrderId, new ReportRenderContext());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out, com.aspose.words.SaveFormat.DOCX);
        return out.toByteArray();
    }

    @Override
    public byte[] renderPdfWithContext(Long templateVersionId, Long inspectionOrderId, ReportRenderContext ctx) throws Exception {
        // 用于验证预览：一次性从DB读取所有数据并应用ctx，不保存快照
        Document doc = buildFrozenDocument(templateVersionId, inspectionOrderId, ctx);
        applyContextToDocument(doc, ctx);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out, com.aspose.words.SaveFormat.PDF);
        return out.toByteArray();
    }

    @Override
    public byte[] renderDocxSnapshot(Long templateVersionId, Long inspectionOrderId, ReportRenderContext ctx) throws Exception {
        // 首次生成：冻结所有稳定字段，可变占位符保留为文本
        Document doc = buildFrozenDocument(templateVersionId, inspectionOrderId, ctx);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out, com.aspose.words.SaveFormat.DOCX);
        return out.toByteArray();
    }

    @Override
    public byte[] renderPdfFromDocxSnapshot(byte[] docxSnapshot, ReportRenderContext ctx) throws Exception {
        // 重渲染：从冻结快照加载，仅替换可变占位符，不查 DB
        try (InputStream in = new ByteArrayInputStream(docxSnapshot)) {
            Document doc = new Document(in);
            applyContextToDocument(doc, ctx);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.save(out, com.aspose.words.SaveFormat.PDF);
            return out.toByteArray();
        }
    }
}
