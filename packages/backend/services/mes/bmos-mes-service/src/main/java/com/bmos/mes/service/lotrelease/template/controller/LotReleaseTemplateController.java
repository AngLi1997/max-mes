package com.bmos.mes.service.lotrelease.template.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.lotrelease.template.dto.*;
import com.bmos.mes.service.lotrelease.template.service.ILotReleaseTemplateService;
import com.bmos.mes.service.lotrelease.template.vo.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 批签发模板相关接口
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 14:02
 */
@RestController
@RequestMapping("/lotRelease/template")
@Api(tags = "批签发模板相关接口")
public class LotReleaseTemplateController {

    @Resource
    private ILotReleaseTemplateService lotReleaseTemplateService;

    @GetMapping("/queryPage")
    @ApiOperation("分页查询批签发模板数据")
    public ResponseInfo<CommonPage<LotReleaseTemplatePageVO>> queryPage(@Validated LotReleaseTemplatePageQuery pageQuery){
        return ResponseInfo.success(lotReleaseTemplateService.queryPage(pageQuery));
    }

    @GetMapping("/queryVersionPage")
    @ApiOperation("分页查询批签发模板版本数据")
    public ResponseInfo<CommonPage<LotReleaseTemplateVersionPageVO>> queryVersionPage(@Validated LotReleaseTemplateVersionPageQuery pageQuery){
        return ResponseInfo.success(lotReleaseTemplateService.queryVersionPage(pageQuery));
    }

    @PutMapping("/bindProcess")
    @ApiOperation("绑定工艺")
    @OperationLog
    public ResponseInfo<Void> bindProcess(@RequestBody @Validated LotReleaseTemplateBindProcessDTO dto){
        lotReleaseTemplateService.bindProcess(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/uploadTemplate")
    @ApiOperation("上传批签发文件模板")
    public ResponseInfo<String> uploadTemplate(@RequestParam("file") MultipartFile file) throws Exception{
        return ResponseInfo.success(lotReleaseTemplateService.uploadTemplate(file));
    }

    @PostMapping("/downloadTemplate")
    @ApiOperation("下载批签发文件模板")
    @OperationLog
    public ResponseInfo<Void> downloadTemplate(HttpServletResponse response, @RequestParam Long id) throws Exception{
        lotReleaseTemplateService.downloadTemplate(response, id);
        return ResponseInfo.success();
    }

    @PostMapping("/createTemplate")
    @ApiOperation("新增批签发模板")
    @OperationLog
    public ResponseInfo<Void> createTemplate(@RequestBody @Validated LotReleaseTemplateCreateDTO dto){
        lotReleaseTemplateService.createTemplate(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/updateTemplateFile")
    @ApiOperation("新增批签发模板")
    @OperationLog
    public ResponseInfo<Void> updateTemplateFile(@RequestBody @Validated LotReleaseTemplateEditDTO dto){
        lotReleaseTemplateService.updateTemplateFile(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/createTemplateVersion")
    @ApiOperation("新增批签发模板版本")
    @OperationLog
    public ResponseInfo<Void> createTemplateVersion(@RequestBody @Validated LotReleaseTemplateVersionCreateDTO dto){
        lotReleaseTemplateService.createTemplateVersion(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/makeSure")
    @ApiOperation("确认")
    @ApiImplicitParam(name = "id", value = "批签发模板id", required = true)
    @OperationLog
    public ResponseInfo<CommonPage<Void>> makeSure(@RequestParam Long id){
        lotReleaseTemplateService.makeSure(id);
        return ResponseInfo.success();
    }

    @PutMapping("/makeDefault")
    @ApiOperation("设为默认")
    @ApiImplicitParam(name = "id", value = "批签发模板id", required = true)
    @OperationLog
    public ResponseInfo<CommonPage<Void>> makeDefault(@RequestParam Long id){
        lotReleaseTemplateService.makeDefault(id);
        return ResponseInfo.success();
    }

    @PutMapping("/scrap")
    @ApiOperation("作废")
    @ApiImplicitParam(name = "id", value = "批签发模板id", required = true)
    @OperationLog
    public ResponseInfo<CommonPage<Void>> scrap(@RequestParam Long id){
        lotReleaseTemplateService.scrap(id);
        return ResponseInfo.success();
    }

    @GetMapping("/history")
    @ApiOperation("查看批签发模板版本操作历史")
    @ApiImplicitParam(name = "id", value = "批签发模板id", required = true)
    public ResponseInfo<List<LogReleaseTemplateVersionHistoryVO>> showHistory(@RequestParam Long id){
        return ResponseInfo.success(lotReleaseTemplateService.showHistory(id));
    }

    @GetMapping("/listByProcessId")
    @ApiOperation("根据工艺id查询配置的批签发模板")
    public ResponseInfo<List<LotReleaseTemplateLinkVO>> listByProcessId(@Validated @RequestParam Long processId){
        return ResponseInfo.success(lotReleaseTemplateService.listByProcessId(processId));
    }

    @GetMapping("/listProcessIdByTemplateId")
    @ApiOperation("根据模板id查询配置的批签发模板关联的工艺id")
    public ResponseInfo<List<Long>> listProcessIdByTemplateId(@Validated @RequestParam Long templateId){
        return ResponseInfo.success(lotReleaseTemplateService.listProcessIdByTemplateId(templateId));
    }

    @GetMapping("/listVersionByTemplateId")
    @ApiOperation("根据模版id查询的批签发模板版本列表")
    public ResponseInfo<List<LotReleaseTemplateVersionItemVO>> listVersionByTemplateId(@Validated @RequestParam Long templateId){
        return ResponseInfo.success(lotReleaseTemplateService.listVersionByTemplateId(templateId));
    }
}
