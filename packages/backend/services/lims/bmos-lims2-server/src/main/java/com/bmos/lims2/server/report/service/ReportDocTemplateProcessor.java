package com.bmos.lims2.server.report.service;

/**
 * 报告文档模板处理器：统一渲染生成/验证的DOCX内容。
 *
 * 渲染分两阶段：
 *  1. renderDocxSnapshot - 首次生成时调用，填充所有稳定字段（数据点/基础信息/自定义字段/报告编号），
 *     保留可变占位符（结论/报告人/时间/审批节点）供后续重渲染使用，结果存为DOCX快照。
 *  2. renderPdfFromDocxSnapshot - 后续重渲染（确认/审批）时从DOCX快照加载，
 *     仅替换可变占位符，不再查询数据库，避免数据变更导致不一致。
 */
public interface ReportDocTemplateProcessor {

    /**
     * 渲染DOCX内容（用于模板下载/预览）。
     */
    byte[] renderDocx(Long templateVersionId, Long inspectionOrderId) throws Exception;

    /**
     * 一次性渲染PDF（用于模板验证预览），从DB读取所有数据并应用ctx。
     */
    byte[] renderPdfWithContext(Long templateVersionId, Long inspectionOrderId, ReportRenderContext ctx) throws Exception;

    /**
     * 渲染DOCX快照：填充稳定字段（数据点/基础信息/自定义字段），
     * 报告编号从ctx.reportNo取值，
     * 可变字段（结论/报告人/生成时间/审批节点）保留为占位符文本，不做替换。
     */
    byte[] renderDocxSnapshot(Long templateVersionId, Long inspectionOrderId, ReportRenderContext ctx) throws Exception;

    /**
     * 从DOCX快照渲染PDF：仅替换可变上下文占位符，不重新查询数据库。
     * @param docxSnapshot renderDocxSnapshot返回的DOCX字节数组
     * @param ctx          包含结论/报告人/时间/审批节点的渲染上下文
     */
    byte[] renderPdfFromDocxSnapshot(byte[] docxSnapshot, ReportRenderContext ctx) throws Exception;
}

