package com.bmos.lims2.web.report;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.report.dto.ReportApprovalPageQueryDTO;
import com.bmos.lims2.server.report.dto.ReportApprovalPendingItemDTO;
import com.bmos.lims2.server.report.dto.ReportConfirmDTO;
import com.bmos.lims2.server.report.dto.ReportGeneratedDTO;
import com.bmos.lims2.server.report.dto.ReportGeneratedPageQueryDTO;
import com.bmos.lims2.server.report.dto.ReportGeneratedListDTO;
import com.bmos.lims2.server.report.dto.ReportValidationTaskDTO;
import com.bmos.lims2.server.report.service.ReportApprovalService;
import com.bmos.lims2.server.report.service.ReportTemplateService;
import com.bmos.lims2.server.report.service.StorageService;
import com.bmos.lims2.server.config.minio.MinioProperties;
import com.bmos.lims2.web.report.vo.req.ReportGenerateStartReqVO;
import com.bmos.lims2.web.report.vo.req.ReportConfirmReqVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

@RestController
@Api(tags = "报告-接口")
@Validated
public class ReportController {

    @Autowired
    private ReportTemplateService reportTemplateService;
    @Autowired
    private ReportApprovalService reportApprovalService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private MinioProperties minioProperties;

    // ===================== 报告生成 =====================

    @PostMapping("/report-generate/start")
    @ApiOperation("提交报告生成任务")
    public ResponseInfo<Long> startGenerate(@RequestBody @Validated ReportGenerateStartReqVO vo) {
        Long taskId = reportTemplateService.startGenerate(BeanUtil.copyProperties(vo, com.bmos.lims2.server.report.dto.ReportGenerateStartDTO.class));
        return ResponseInfo.success(taskId);
    }

    @PostMapping("/report-generate/confirm/{taskId}")
    @ApiOperation("确认报告（待确认 → 待审批），填写检验结论")
    public ResponseInfo<Void> confirmReport(@PathVariable("taskId") @NotNull Long taskId,
                                            @RequestBody ReportConfirmReqVO vo) {
        reportTemplateService.confirmReport(taskId, BeanUtil.copyProperties(vo, ReportConfirmDTO.class));
        return ResponseInfo.success();
    }

    @GetMapping("/report-generate/status/{taskId}")
    @ApiOperation("查询报告生成任务状态")
    public ResponseInfo<ReportValidationTaskDTO> generateStatus(@PathVariable("taskId") @NotNull Long taskId) {
        return ResponseInfo.success(reportTemplateService.getGenerateStatus(taskId));
    }

    @GetMapping("/report-generate/page")
    @ApiOperation("分页查询已生成报告列表（按生成时间倒序）")
    public ResponseInfo<CommonPage<ReportGeneratedDTO>> pageGenerated(@Validated ReportGeneratedPageQueryDTO queryDTO) {
        return ResponseInfo.success(reportTemplateService.pageGeneratedReports(queryDTO));
    }

    @GetMapping("/report-generate/list-by-order")
    @ApiOperation("已生成报告列表（按生成时间倒序）- 分开返回检验单信息与报告列表，列表不分页")
    public ResponseInfo<ReportGeneratedListDTO> listGeneratedByOrder(
            @ApiParam("检验单ID") @RequestParam("inspectionOrderId") @NotNull Long inspectionOrderId,
            @ApiParam("报告模板ID") @RequestParam("templateId") @NotNull Long templateId) {
        return ResponseInfo.success(reportTemplateService.listGeneratedReports(inspectionOrderId, templateId));
    }

    @PostMapping("/report-generate/void/{taskId}")
    @ApiOperation("作废报告（待审批、生效状态允许）")
    public ResponseInfo<Void> voidReport(@PathVariable("taskId") @NotNull Long taskId,
                                         @ApiParam("作废原因") @RequestParam(value = "reason", required = false) String reason) {
        reportTemplateService.voidReport(taskId, reason);
        return ResponseInfo.success();
    }

    @GetMapping("/report-generate/history")
    @ApiOperation("查询已生成报告的操作历史（仅业务）")
    public ResponseInfo<java.util.List<com.bmos.lims2.server.audit.operationlog.vo.ListLogVO>> getReportOperationHistory(
            @ApiParam("报告生成任务ID") @RequestParam("taskId") @NotNull Long taskId) {
        return ResponseInfo.success(reportTemplateService.listReportOperationHistory(taskId));
    }

    // ===================== 报告审批 =====================

    /**
     * 分页查询待审批的报告（按任务）
     */
    @PostMapping("/report-approval/pending")
    @ApiOperation("分页查询待审批报告")
    public ResponseInfo<CommonPage<ReportApprovalPendingItemDTO>> pagePending(@RequestBody ReportApprovalPageQueryDTO queryDTO) {
        return ResponseInfo.success(reportApprovalService.pagePending(queryDTO));
    }

    /**
     * 按任务发起报告审批流程
     */
    @PostMapping("/report-approval/start/{taskId}")
    @ApiOperation("发起报告审批流程")
    public ResponseInfo<String> startApproval(@PathVariable("taskId") Long taskId) {
        return ResponseInfo.success(reportApprovalService.startReportApproval(taskId));
    }

    @GetMapping("/report/download-by-task")
    @ApiOperation("按任务ID下载报告文件")
    public ResponseEntity<InputStreamResource> downloadByTask(@ApiParam("报告生成任务ID") @RequestParam("taskId") @NotNull Long taskId) throws UnsupportedEncodingException {
        ReportValidationTaskDTO dto = reportTemplateService.getGenerateStatus(taskId);
        String path = dto.getPath();
        // 路径以 .docx 结尾时，说明正式 PDF 尚未生成，不允许下载
        if (path == null || path.isEmpty() || path.endsWith(".docx")) {
            throw new RuntimeException("报告审批完成前暂不支持下载");
        }
        // 记录下载历史
        reportTemplateService.logReportDownload(taskId, path);
        String bucket = minioProperties.getBuckets().getReportVersion();
        java.io.InputStream is = storageService.getObject(bucket, path);
        String filename = path;
        int slash = filename.lastIndexOf('/');
        if (slash >= 0 && slash < filename.length() - 1) {
            filename = filename.substring(slash + 1);
        }
        String encoded = java.net.URLEncoder.encode(filename, "UTF-8");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encoded)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(is));
    }

    @GetMapping("/report/download-by-path")
    @ApiOperation("按相对路径下载报告文件")
    public ResponseEntity<InputStreamResource> downloadByPath(@ApiParam("报告文件相对路径") @RequestParam("path") @NotNull String path) throws UnsupportedEncodingException {
        String bucket = minioProperties.getBuckets().getReportVersion();
        java.io.InputStream is = storageService.getObject(bucket, path);
        // 尝试解析任务ID用于记录下载历史（路径规则：report-version/{taskId}/xxx.pdf）
        try {
            String[] parts = path.split("/");
            if (parts.length >= 2) {
                Long taskId = Long.valueOf(parts[1]);
                reportTemplateService.logReportDownload(taskId, path);
            }
        } catch (Exception ignore) {}
        String filename = path;
        int slash = filename.lastIndexOf('/');
        if (slash >= 0 && slash < filename.length() - 1) {
            filename = filename.substring(slash + 1);
        }
        String encoded = java.net.URLEncoder.encode(filename, "UTF-8");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encoded)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(is));
    }

    @GetMapping("/report/preview-by-task")
    @ApiOperation("按任务ID预览报告文件（带水印）")
    public ResponseEntity<InputStreamResource> previewByTask(@ApiParam("报告生成任务ID") @RequestParam("taskId") @NotNull Long taskId) throws UnsupportedEncodingException {
        java.io.InputStream is = reportTemplateService.previewReportByTask(taskId);
        ReportValidationTaskDTO dto = reportTemplateService.getGenerateStatus(taskId);
        String path = dto.getPath();
        String filename = path != null ? path : "report.pdf";
        int slash = filename.lastIndexOf('/');
        if (slash >= 0 && slash < filename.length() - 1) {
            filename = filename.substring(slash + 1);
        }
        String encoded = java.net.URLEncoder.encode(filename, "UTF-8");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + encoded)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(is));
    }

    @GetMapping("/report/preview-by-path")
    @ApiOperation("按相对路径预览报告文件（带水印）")
    public ResponseEntity<InputStreamResource> previewByPath(@ApiParam("报告文件相对路径") @RequestParam("path") @NotNull String path) throws UnsupportedEncodingException {
        java.io.InputStream is = reportTemplateService.previewReportByPath(path);
        String filename = path;
        int slash = filename.lastIndexOf('/');
        if (slash >= 0 && slash < filename.length() - 1) {
            filename = filename.substring(slash + 1);
        }
        String encoded = java.net.URLEncoder.encode(filename, "UTF-8");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + encoded)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(is));
    }
}


