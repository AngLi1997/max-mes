package com.bmos.lims2.server.report.service;

import com.bmos.lims2.server.report.dto.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ReportTemplateService {

    CommonPage<ReportTemplateDTO> page(ReportTemplatePageQueryDTO dto);

    /**
     * 分页查询模板的版本列表
     */
    CommonPage<ReportTemplateVersionDTO> pageVersions(ReportTemplateVersionPageQueryDTO queryDTO);

    /**
     * 根据模板ID查询版本下拉列表
     */
    java.util.List<ReportTemplateVersionOptionDTO> listVersionOptions(Long templateId);

    Long createTemplate(ReportTemplateSaveDTO dto);

    Long createVersion(ReportTemplateVersionSaveDTO dto);

    void savePermissions(Long templateId, List<Long> deptIds);

    List<Long> getPermissions(Long templateId);

    void bindSchemes(Long templateId, List<Long> schemeIds);

    String uploadVersionFileMultipart(MultipartFile file);

    void confirmVersion(Long versionId);

    void voidVersion(Long versionId);

    void setDefaultVersion(Long versionId);

    Long startValidation(ReportValidationStartDTO dto);

    ReportValidationTaskDTO getValidationStatus(Long taskId);

    List<ReportTemplateOperationHistoryDTO> listOperationHistory(Long templateVersionId);

    java.util.List<EligibleOrderDTO> listEligibleOrders(Long materialId, Long schemeVersionId);

    Long startGenerate(ReportGenerateStartDTO dto);

    ReportValidationTaskDTO getGenerateStatus(Long taskId);


    java.util.List<ReportTemplateDTO> listByScheme(Long schemeId);

    /**
     * 首页下列表：按方案/版本分页查询报告模板
     */
    CommonPage<ReportTemplateDTO> pageByScheme(ReportTemplateBySchemePageQueryDTO queryDTO);

    /**
     * 根据模板ID查询已绑定的检验方案列表
     */
    java.util.List<com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO> listSchemesByTemplateId(Long templateId);

    /**
     * 首页下列表：按检验单分页查询报告模板（根据单据确定方案版本）
     */
    CommonPage<ReportTemplateByOrderItemDTO> pageByOrder(ReportTemplateByOrderPageQueryDTO queryDTO);

    /**
     * 更新报告模板版本的文件信息
     */
    void updateVersionFile(Long versionId,String path ,String remark);

    /**
     * 获取报告模板版本文件信息并记录下载历史
     */
    ReportTemplateVersionDTO getVersionFile(Long versionId);

    /**
     * 分页查询某模板版本已生成的报告列表（按生成时间倒序）
     */
    com.bmos.mybatis.page.CommonPage<com.bmos.lims2.server.report.dto.ReportGeneratedDTO> pageGeneratedReports(com.bmos.lims2.server.report.dto.ReportGeneratedPageQueryDTO queryDTO);

    /**
     * 查询某检验单的已生成报告列表（按生成时间倒序，不分页），并返回检验单信息；按模板过滤
     */
    com.bmos.lims2.server.report.dto.ReportGeneratedListDTO listGeneratedReports(Long inspectionOrderId, Long templateId);

    /**
     * 作废报告：仅允许待审批和已生效状态
     */
    void voidReport(Long generateTaskId, String reason);

    /**
     * 查询报告历史：按检验单+模板返回已生成报告列表（按时间倒序，不分页）
     */
    java.util.List<com.bmos.lims2.server.report.dto.ReportGeneratedItemDTO> listReportHistory(Long inspectionOrderId, Long templateId);

    /**
     * 查询某个已生成报告的操作历史（工作流任务历史）
     */
    java.util.List<com.bmos.lims2.server.audit.vo.TaskHistoryVO> getReportOperationHistory(Long generateTaskId);

    /**
     * 查询某个已生成报告的业务操作历史（生成/审批/作废等）
     * 返回格式与方案版本历史查询接口一致
     */
    java.util.List<com.bmos.lims2.server.audit.operationlog.vo.ListLogVO> listReportOperationHistory(Long taskId);

    /**
     * 记录报告下载历史
     */
    void logReportDownload(Long taskId, String path);

    /**
     * 预览报告文件（带水印）- 按任务ID
     * @param taskId 报告生成任务ID
     * @return 带水印的PDF输入流
     */
    java.io.InputStream previewReportByTask(Long taskId);

    /**
     * 预览报告文件（带水印）- 按相对路径
     * @param path 报告文件相对路径
     * @return 带水印的PDF输入流
     */
    java.io.InputStream previewReportByPath(String path);

    /**
     * 确认报告（待确认 → 待审批），记录确认人、确认时间、检验结论，并重渲染报告文件
     */
    void confirmReport(Long taskId, ReportConfirmDTO dto);

    /**
     * 查询报告基础信息索引列表（固定字段 + 审批节点示例）
     */
    java.util.List<ReportBasicIndexDTO> listBasicInfoIndexes();

    /**
     * 查询来自平台字典的自定义字段索引列表
     */
    java.util.List<ReportCustomFieldIndexDTO> listCustomFieldIndexes();
}


