package com.bmos.mes.service.lotrelease.manage.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.lotrelease.manage.dto.*;
import com.bmos.mes.service.lotrelease.manage.service.ILotReleaseService;
import com.bmos.mes.service.lotrelease.manage.vo.*;
import com.bmos.mes.service.product.vo.ProductCategoryTreeNodeVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批签发管理相关接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 11:22
 */
@RestController
@RequestMapping("/lotRelease/manage")
@Api(tags = "批签发管理相关接口")
public class LotReleaseController {

    @Resource
    private ILotReleaseService lotReleaseService;

    @GetMapping("/productTree")
    @ApiOperation("获取批记录产品树")
    public ResponseInfo<List<ProductCategoryTreeNodeVO>> getLotReleaseProductTree(@RequestParam("categoryType") @NotNull Integer categoryType,
                                                                                  @RequestParam(value = "templateId", required = false) Long templateId) {
        return ResponseInfo.success(lotReleaseService.getLotReleaseProductTreeByTemplateId(categoryType, templateId));
    }

    @GetMapping("/queryPlanPage")
    @ApiOperation("分页查询批签发生产计划数据")
    public ResponseInfo<CommonPage<LotReleasePlanVO>> queryPlanPage(@Validated LotReleasePlanPageQuery pageQuery) {
        return ResponseInfo.success(lotReleaseService.queryPlanPage(pageQuery));
    }

    @GetMapping("/queryPage")
    @ApiOperation("分页查询批签发数据")
    public ResponseInfo<CommonPage<LotReleasePageVO>> queryPage(@Validated LotReleasePageQuery pageQuery) {
        return ResponseInfo.success(lotReleaseService.queryPage(pageQuery));
    }

    @GetMapping("/queryVersionPage")
    @ApiOperation("分页查询批签发版本数据")
    public ResponseInfo<CommonPage<LotReleaseVersionPageVO>> queryVersionPage(@Validated LotReleaseVersionPageQuery pageQuery) {
        return ResponseInfo.success(lotReleaseService.queryVersionPage(pageQuery));
    }

    @GetMapping("/queryAuditPage")
    @ApiOperation("分页查询批签发待审核数据")
    public ResponseInfo<CommonPage<LotReleaseAuditPageVO>> queryAuditPage(@Validated LotReleaseAuditPageQuery pageQuery) {
        return ResponseInfo.success(lotReleaseService.queryAuditPage(pageQuery));
    }

    @PostMapping("/generate")
    @ApiOperation("生成批签发")
    @OperationLog
    public ResponseInfo<String> generate(@RequestBody LotReleaseGenerateDTO dto) {
        return ResponseInfo.success(lotReleaseService.generate(dto));
    }

    @PostMapping("/getDynamicReportItem")
    @ApiOperation("查询生成批签发需要动态填报的数据")
    public ResponseInfo<List<LotReleaseDynamicReportItemVO>> getDynamicReportItem(@RequestBody LotReleaseQueryDynamicReportDTO dto) {
        return ResponseInfo.success(lotReleaseService.getDynamicReportItem(dto));
    }

    @GetMapping("/getGeneratePreviewList")
    @ApiOperation("批签发查询批次引用列表")
    public ResponseInfo<List<LotReleaseGeneratePreviewVO>> getGeneratePreviewList(@RequestParam Long processId) {
        return ResponseInfo.success(lotReleaseService.getGeneratePreviewList(processId));
    }

    @PostMapping("/uploadExcel")
    @ApiOperation("上传批签发文件")
    public ResponseInfo<String> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        return ResponseInfo.success(lotReleaseService.uploadExcel(file));
    }

    @PostMapping("/updateExcelFile")
    @ApiOperation("更新批签发文件")
    @OperationLog
    public ResponseInfo<Void> updateExcelFile(@RequestBody @Validated LotReleaseUpdateExcelFileDTO dto) {
        lotReleaseService.updateExcelFile(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/submit")
    @ApiOperation("提交审批")
    @ApiImplicitParam(name = "id", value = "批签发id", required = true)
    @OperationLog
    public ResponseInfo<Void> submit(@RequestParam Long id) {
        lotReleaseService.submit(id);
        return ResponseInfo.success();
    }

    @PutMapping("/scrap")
    @ApiOperation("作废")
    @ApiImplicitParam(name = "id", value = "批签发id", required = true)
    @OperationLog
    public ResponseInfo<Void> scrap(@RequestParam Long id) {
        lotReleaseService.scrap(id);
        return ResponseInfo.success();
    }

    @GetMapping("/history")
    @ApiOperation("查看历史")
    @ApiImplicitParam(name = "id", value = "批签发id", required = true)
    public ResponseInfo<List<LogReleaseHistoryVO>> showHistory(@RequestParam Long id) {
        return ResponseInfo.success(lotReleaseService.showHistory(id));
    }

    @PostMapping("/download")
    @ApiOperation("下载批签发文件")
    @OperationLog
    public ResponseInfo<Void> downloadExcel(HttpServletResponse response, @RequestParam Long id) throws Exception {
        lotReleaseService.downloadExcel(response, id);
        return ResponseInfo.success();
    }

    @PostMapping("/downloadByUrl")
    @ApiOperation("根据地址下载批签发文件")
    @OperationLog
    public ResponseInfo<Void> downloadByUrl(HttpServletResponse response, @RequestParam String url) throws Exception {
        lotReleaseService.downloadByUrl(response, url);
        return ResponseInfo.success();
    }
}
