package com.bmos.lims2.web.report;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.report.dto.*;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeService;
import com.bmos.lims2.server.report.service.ReportTemplateService;
import com.bmos.lims2.server.report.service.StorageService;
import com.bmos.lims2.server.config.minio.MinioProperties;
import com.bmos.lims2.web.report.vo.req.*;
import com.bmos.logging.annotation.OperationLog;
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
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/report")
@Api(tags = "报告模板-接口")
@Validated
public class ReportTemplateController {

    @Autowired
    private ReportTemplateService reportTemplateService;
    @Autowired
    private InspectionSchemeService inspectionSchemeService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private MinioProperties minioProperties;
    @Autowired
    private com.bmos.lims2.server.material.service.MaterialService materialService;

    @PostMapping("/templates/page")
    @ApiOperation("报告模板分页查询")
    public ResponseInfo<CommonPage<ReportTemplateDTO>> page(@RequestBody @Validated ReportTemplatePageReqVO reqVO) {
        ReportTemplatePageQueryDTO dto = BeanUtil.copyProperties(reqVO, ReportTemplatePageQueryDTO.class);
        // 解析物料ID集合：优先 materialId 其次 categoryId(启用检品)
        java.util.List<Long> materialIds = dto.getMaterialIds();
        if (reqVO.getMaterialId() != null) {
            materialIds = java.util.Collections.singletonList(reqVO.getMaterialId());
        } else if (reqVO.getCategoryId() != null && (materialIds == null || materialIds.isEmpty())) {
            java.util.List<com.bmos.lims2.server.material.dto.MaterialDTO> materials = materialService.getAllByCategoryId(reqVO.getCategoryId());
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(materials)) {
                materialIds = materials.stream()
                        .filter(m -> java.lang.Boolean.TRUE.equals(m.getStatus()))
                        .map(com.bmos.mybatis.dataobject.BaseDO::getId)
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        // 若按分类未解析到任何启用检品，直接返回空分页
        if (reqVO.getMaterialId() == null && reqVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<ReportTemplateDTO> empty = new CommonPage<>();
            empty.setPageNum(reqVO.getPageNum());
            empty.setPageSize(reqVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        dto.setMaterialIds(materialIds);
        return ResponseInfo.success(reportTemplateService.page(dto));
    }

    @PostMapping("/template")
    @ApiOperation("新增报告模板（含初始版本与数据权限）")
    @OperationLog
    public ResponseInfo<Long> create(@RequestBody @Validated ReportTemplateSaveReqVO reqVO) {
        Long id = reportTemplateService.createTemplate(BeanUtil.copyProperties(reqVO, ReportTemplateSaveDTO.class));
        return ResponseInfo.success(id);
    }

    @PostMapping("/template/{id}/version")
    @ApiOperation("新增报告模板版本")
    @OperationLog
    public ResponseInfo<Long> createVersion(@PathVariable("id") @NotNull Long templateId,
                                            @RequestBody @Validated ReportTemplateVersionSaveReqVO reqVO) {
        ReportTemplateVersionSaveDTO dto = BeanUtil.copyProperties(reqVO, ReportTemplateVersionSaveDTO.class);
        dto.setTemplateId(templateId);
        Long versionId = reportTemplateService.createVersion(dto);
        return ResponseInfo.success(versionId);
    }

    @PutMapping("/template/{id}/permissions")
    @ApiOperation("保存模板数据权限（部门）")
    @OperationLog
    public ResponseInfo<Void> savePermissions(@PathVariable("id") @NotNull Long templateId, @RequestBody @Validated SavePermissionsReqVO vo) {
        reportTemplateService.savePermissions(templateId, vo.getDeptIds());
        return ResponseInfo.success();
    }

    @GetMapping("/template/{id}/permissions")
    @ApiOperation("查询模板数据权限（部门ID列表）")
    public ResponseInfo<List<Long>> getPermissions(@PathVariable("id") @NotNull Long templateId) {
        return ResponseInfo.success(reportTemplateService.getPermissions(templateId));
    }

    @PutMapping("/template/{id}/bind-schemes")
    @ApiOperation("绑定检验方案（直接绑定方案）")
    @OperationLog
    public ResponseInfo<Void> bindSchemes(@PathVariable("id") @NotNull Long templateId, @RequestBody @Validated BindSchemesReqVO vo) {
        reportTemplateService.bindSchemes(templateId, vo.getSchemeIds());
        return ResponseInfo.success();
    }

    @PostMapping("/version/upload-file")
    @ApiOperation("上传报告模板版本文件（Multipart方式，返回相对路径）")
    @OperationLog
    public ResponseInfo<String> uploadFile(@RequestPart("file") MultipartFile file) {
        String path = reportTemplateService.uploadVersionFileMultipart(file);
        return ResponseInfo.success(path);
    }

    @PostMapping("/version/{id}/confirm")
    @ApiOperation("确认报告模板版本")
    @OperationLog
    public ResponseInfo<Void> confirm(@PathVariable("id") @NotNull Long versionId) {
        reportTemplateService.confirmVersion(versionId);
        return ResponseInfo.success();
    }

    @PostMapping("/version/{id}/void")
    @ApiOperation("作废报告模板版本")
    @OperationLog
    public ResponseInfo<Void> voidVersion(@PathVariable("id") @NotNull Long versionId) {
        reportTemplateService.voidVersion(versionId);
        return ResponseInfo.success();
    }

    @PostMapping("/version/{id}/default")
    @ApiOperation("设置为默认报告模板版本")
    @OperationLog
    public ResponseInfo<Void> setDefault(@PathVariable("id") @NotNull Long versionId) {
        reportTemplateService.setDefaultVersion(versionId);
        return ResponseInfo.success();
    }

    @PostMapping("/version/validate")
    @ApiOperation("提交报告验证任务")
    @OperationLog
    public ResponseInfo<Long> startValidation(@RequestBody @Validated ValidationStartReqVO vo) {
        Long taskId = reportTemplateService.startValidation(BeanUtil.copyProperties(vo, ReportValidationStartDTO.class));
        return ResponseInfo.success(taskId);
    }

    @GetMapping("/validation/{taskId}")
    @ApiOperation("查询报告验证任务状态")
    public ResponseInfo<ReportValidationTaskDTO> validationStatus(@PathVariable("taskId") @NotNull Long taskId) {
        return ResponseInfo.success(reportTemplateService.getValidationStatus(taskId));
    }

    @GetMapping("/version/{versionId}/history")
    @ApiOperation("查询报告模板版本操作历史")
    public ResponseInfo<List<ReportTemplateOperationHistoryDTO>> history(@PathVariable("versionId") @NotNull Long versionId) {
        return ResponseInfo.success(reportTemplateService.listOperationHistory(versionId));
    }

    @GetMapping("/version/download")
    @ApiOperation("按版本ID下载模板文件（记录下载历史）")
    public ResponseEntity<InputStreamResource> download(@ApiParam("模板版本ID") @RequestParam("versionId") @NotNull Long versionId) throws UnsupportedEncodingException {
        ReportTemplateVersionDTO dto = reportTemplateService.getVersionFile(versionId);
        String bucket = minioProperties.getBuckets().getReportTemplate();
        String objectKey = dto.getPath();
        java.io.InputStream is = storageService.getObject(bucket, objectKey);
        String encoded = objectKey;
        encoded = java.net.URLEncoder.encode(encoded, "UTF-8");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + encoded)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(is));
    }

    @GetMapping("/version/download-by-path")
    @ApiOperation("按相对路径下载模板文件")
    public ResponseEntity<InputStreamResource> downloadByPath(@ApiParam("模板文件相对路径") @RequestParam("path") @NotNull String path) throws UnsupportedEncodingException {
        String bucket = minioProperties.getBuckets().getReportTemplate();
        String objectKey = path;
        java.io.InputStream is = storageService.getObject(bucket, objectKey);
        String filename = objectKey;
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

    @GetMapping("/schemes/dropdown/{materialId}")
    @ApiOperation("根据检品ID获取可绑定的检验方案下拉列表")
    public ResponseInfo<List<InspectionSchemeDropdownDTO>> schemesByMaterial(@PathVariable("materialId") @NotNull Long materialId) {
        return ResponseInfo.success(inspectionSchemeService.getInspectionSchemeDropdownByMaterialId(materialId));
    }

    @GetMapping("/validation/eligible-orders")
    @ApiOperation("按检品与方案版本查询可用于验证的检验单（样品审核已通过）")
    public ResponseInfo<java.util.List<EligibleOrderDTO>> listEligibleOrders(@ApiParam("检品ID") @RequestParam Long materialId,
                                                                             @ApiParam("方案版本ID") @RequestParam Long schemeVersionId) {
        return ResponseInfo.success(reportTemplateService.listEligibleOrders(materialId, schemeVersionId));
    }

    @GetMapping("/templates/by-scheme/{schemeId}")
    @ApiOperation("按检验方案列出绑定的报告模板及其版本")
    public ResponseInfo<java.util.List<ReportTemplateDTO>> listByScheme(@PathVariable("schemeId") Long schemeId) {
        return ResponseInfo.success(reportTemplateService.listByScheme(schemeId));
    }

    @GetMapping("/template/{id}/schemes")
    @ApiOperation("根据模板ID查询已绑定的检验方案列表")
    public ResponseInfo<java.util.List<InspectionSchemeDropdownDTO>> listSchemesByTemplate(@PathVariable("id") @NotNull Long templateId) {
        return ResponseInfo.success(reportTemplateService.listSchemesByTemplateId(templateId));
    }

    

    @PutMapping("/version/{id}/file")
    @ApiOperation("更新报告模板版本文件信息")
    @OperationLog
    public ResponseInfo<Void> updateVersionFile(@PathVariable("id") @NotNull Long versionId,
                                                @RequestParam @Validated String path, String remark) {
        reportTemplateService.updateVersionFile(versionId, path,remark);
        return ResponseInfo.success();
    }

    @GetMapping("/template/{id}/versions/page")
    @ApiOperation("根据模板ID分页查询版本列表（默认排序）")
    public ResponseInfo<CommonPage<ReportTemplateVersionDTO>> pageVersions(@PathVariable("id") @NotNull Long templateId,
                                                                           @Validated ReportTemplateVersionPageReqVO vo) {
        ReportTemplateVersionPageQueryDTO dto = BeanUtil.copyProperties(vo, ReportTemplateVersionPageQueryDTO.class);
        dto.setTemplateId(templateId);
        return ResponseInfo.success(reportTemplateService.pageVersions(dto));
    }

    @GetMapping("/template/{id}/versions/options")
    @ApiOperation("根据模板ID查询版本下拉列表")
    public ResponseInfo<java.util.List<com.bmos.lims2.server.report.dto.ReportTemplateVersionOptionDTO>> listVersionOptions(@PathVariable("id") @NotNull Long templateId) {
        return ResponseInfo.success(reportTemplateService.listVersionOptions(templateId));
    }

    @PostMapping("/templates/by-scheme/page")
    @ApiOperation("首页下列表：按方案/版本分页查询模板")
    public ResponseInfo<CommonPage<ReportTemplateDTO>> pageByScheme(@RequestBody @Validated com.bmos.lims2.web.report.vo.req.ReportTemplateBySchemePageReqVO vo) {
        ReportTemplateBySchemePageQueryDTO dto = BeanUtil.copyProperties(vo, ReportTemplateBySchemePageQueryDTO.class);
        return ResponseInfo.success(reportTemplateService.pageByScheme(dto));
    }

    @PostMapping("/templates/by-order/page")
    @ApiOperation("首页下列表：按检验单分页查询模板（根据单据取方案版本）")
    public ResponseInfo<CommonPage<ReportTemplateByOrderItemDTO>> pageByOrder(@RequestBody @Validated com.bmos.lims2.web.report.vo.req.ReportTemplateByOrderPageReqVO vo) {
        ReportTemplateByOrderPageQueryDTO dto = BeanUtil.copyProperties(vo, ReportTemplateByOrderPageQueryDTO.class);
        return ResponseInfo.success(reportTemplateService.pageByOrder(dto));
    }

    @GetMapping("/indexes/basic-info")
    @ApiOperation("查询报告基础信息索引列表（固定字段 + 审批节点示例，不分页）")
    public ResponseInfo<java.util.List<com.bmos.lims2.server.report.dto.ReportBasicIndexDTO>> listBasicInfoIndexes() {
        return ResponseInfo.success(reportTemplateService.listBasicInfoIndexes());
    }

    @GetMapping("/indexes/custom-fields")
    @ApiOperation("查询来自平台字典的自定义字段索引列表（不分页）")
    public ResponseInfo<java.util.List<com.bmos.lims2.server.report.dto.ReportCustomFieldIndexDTO>> listCustomFieldIndexes() {
        return ResponseInfo.success(reportTemplateService.listCustomFieldIndexes());
    }
}


