package com.bmos.mes.service.plan.document.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.plan.document.controller.vo.*;
import com.bmos.mes.service.plan.document.service.BatchTemplateLogService;
import com.bmos.mes.service.plan.document.service.BatchTemplateService;
import com.bmos.mes.service.plan.document.service.dto.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批记录模板接口（用于归档）
 */
@RestController
@RequestMapping("/plan/archive/template")
@Api(tags = "批记录模板接口（用于归档）")
public class BatchTemplateController {

    @Autowired
    BatchTemplateService batchTemplateService;

    @Autowired
    BatchTemplateLogService logService;

    @PostMapping("/fileUpload")
    @ApiOperation(value = "文件上传")
    @ApiParam(name = "file", value = "文件流", required = true)
    public ResponseInfo<String> fileUpload(MultipartFile file) {
        return ResponseInfo.success(batchTemplateService.fileUpload(file));
    }

    @ApiOperation(value = "添加批记录模板")
    @PostMapping("/save")
    @OperationLog
    public ResponseInfo<Void> saveTemplate(@Validated @RequestBody TemplateSaveDTO dto) {
        batchTemplateService.saveTemplate(dto);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "新增批记录模板版本")
    @PostMapping("/version/save")
    @OperationLog
    public ResponseInfo<Void> saveTemplateVersion(@Validated @RequestBody TemplateVersionSaveDTO dto) {
        batchTemplateService.saveTemplateVersion(dto);
        return ResponseInfo.success();
    }

    @ApiOperation("对批记录模板版本重新上传批记录模板")
    @PostMapping("/version/upload")
    @OperationLog
    public ResponseInfo<Void> uploadTemplateVersion(@Validated @RequestBody TemplateVersionUpdateDTO dto) {
        batchTemplateService.uploadTemplateVersion(dto);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "下载批记录模板")
    @PostMapping("/version/download")
    @OperationLog
    public ResponseInfo<Void> downloadTemplateVersion(HttpServletResponse response, @RequestBody TemplateVersionOperateDTO dto) {
        batchTemplateService.downloadTemplateVersion(dto, response);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "作废批记录")
    @PutMapping("/scrap")
    @OperationLog
    public ResponseInfo<Void> scrapTemplateVersion(@RequestBody TemplateVersionOperateDTO dto) {
        batchTemplateService.scrapTemplateVersion(dto);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "确认批记录模板")
    @PutMapping("/version/confirm")
    @OperationLog
    public ResponseInfo<Void> confirmTemplateVersion(@RequestBody TemplateVersionOperateDTO dto) {
        batchTemplateService.confirmTemplateVersion(dto);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "设置批记录模板的版本为默认")
    @PutMapping("/version/normal")
    @OperationLog
    public ResponseInfo<Void> normalTemplateVersion(@RequestBody TemplateVersionOperateDTO dto) {
        batchTemplateService.normalTemplateVersion(dto);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "根据模板信息id查询当前模板信息绑定的所有工艺下的所有已经开始生产计划id")
    @GetMapping("/plan/list")
    public ResponseInfo<List<PlanEasyVO>> templatePlan(@RequestParam("templateInfoId") Long templateInfoId) {
        return ResponseInfo.success(batchTemplateService.templatePlan(templateInfoId));
    }

    @ApiOperation(value = "模板信息绑定工艺")
    @PostMapping("/bind/process")
    @OperationLog
    public ResponseInfo<Void> templateInfoBindProcess(@Validated @RequestBody TemplateInfoBindDTO dto) {
        batchTemplateService.templateInfoBindProcess(dto);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "模板信息绑定数据权限")
    @PostMapping("/bind/auth")
    @OperationLog
    public ResponseInfo<Void> templateInfoBindDataAuth(@Validated @RequestBody TemplateInfoBindAuthDTO dto) {
        batchTemplateService.templateInfoBindDataAuth(dto);
        return ResponseInfo.success();
    }

    @ApiOperation(value = "分页查询批记录模板信息")
    @GetMapping("/page")
    public ResponseInfo<CommonPage<TemplateInfoPageVO>> templateInfoPage(TemplateInfoPageDTO dto) {
        return ResponseInfo.success(batchTemplateService.templateInfoPage(dto));
    }

    @ApiOperation(value = "分页查询批记录模板版本")
    @GetMapping("/version/page")
    public ResponseInfo<CommonPage<TemplateVersionPageVO>> templateVersionPage(TemplateVersionPageDTO dto) {
        return ResponseInfo.success(batchTemplateService.templateVersionPage(dto));
    }

    @ApiOperation(value = "查询批记录模板版本历史记录VO")
    @GetMapping("/history")
    public ResponseInfo<List<TemplateVersionHistoryVO>> templateVersionHistory(TemplateVersionOperateDTO dto) {
        return ResponseInfo.success(logService.templateVersionHistory(dto));
    }

    @ApiOperation(value = "查询模板下的默认模板版本")
    @GetMapping("/version/normal")
    public ResponseInfo<List<TemplateVersionEasyVO>> templateNormalVersionInfo(@RequestParam("templateInfoId") Long templateInfoId) {
        return ResponseInfo.success(batchTemplateService.templateNormalVersionInfo(templateInfoId));
    }

    @ApiOperation("根据路径下载批记录模板（在历史中使用）")
    @GetMapping("/path/download")
    public ResponseInfo<Void> downloadPath(@RequestParam("path") @NotEmpty String path, HttpServletResponse response) {
        batchTemplateService.downloadPath(path, response);
        return ResponseInfo.success();
    }

}
