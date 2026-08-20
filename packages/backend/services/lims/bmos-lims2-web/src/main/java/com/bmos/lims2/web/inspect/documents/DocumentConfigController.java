package com.bmos.lims2.web.inspect.documents;


import cn.hutool.core.bean.BeanUtil;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.document.dto.*;
import com.bmos.lims2.server.inspect.document.service.DocumentConfigService;
import com.bmos.lims2.web.inspect.documents.vo.req.DocumentConfigBindProductVO;
import com.bmos.lims2.web.inspect.documents.vo.req.DocumentConfigPageReqVO;
import com.bmos.lims2.web.inspect.documents.vo.req.DocumentConfigSaveVO;
import com.bmos.lims2.web.inspect.documents.vo.req.DocumentConfigUpdateVO;
import com.bmos.lims2.web.inspect.documents.vo.resp.DocumentConfigVO;
import com.bmos.lims2.web.inspect.documents.vo.resp.DocumentConfigWithFieldVO;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.bmos.lims2.web.inspect.documents.vo.req.DocumentConfigPreviewReqVO;

@Api(tags = "请验单配置")
@RestController
@Validated
@RequestMapping("/document/config")
public class DocumentConfigController {

    @Resource
    private DocumentConfigService documentConfigService;
    @Resource
    private com.bmos.lims2.server.inspect.order.service.InspectionOrderService inspectionOrderService;

    @GetMapping("/page")
    @ApiOperation("请验单配置-分页")
    public ResponseInfo<CommonPage<DocumentConfigDTO>> inspectionConfigPage(@Validated DocumentConfigPageReqVO dto) {
        return ResponseInfo.success(documentConfigService.inspectionConfigPage(BeanUtil.copyProperties(dto, DocumentConfigPageReqDTO.class)));
    }

    @PostMapping("/save")
    @ApiOperation("请验单配置-新增")
    public ResponseInfo<Void> saveInspectionConfig(@RequestBody @Validated DocumentConfigSaveVO documentConfigSaveVO) {
        documentConfigSaveVO.validateParams();
        documentConfigService.saveInspectionConfig(BeanUtil.copyProperties(documentConfigSaveVO, DocumentConfigSaveDTO.class));
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("请验单配置-编辑")
    public ResponseInfo<Void> updateInspectionConfig(@RequestBody @Validated DocumentConfigUpdateVO documentConfigUpdateVO) {
        documentConfigUpdateVO.validateParams();
        documentConfigService.updateInspectionConfig(BeanUtil.copyProperties(documentConfigUpdateVO, DocumentConfigUpdateDTO.class));
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("请验单配置-删除")
    @ApiParam(name = "id", value = "请验单id", required = true)
    public ResponseInfo<Void> deleteInspectionConfig(@PathVariable @NotNull Long id) {
        documentConfigService.deleteInspectionConfig(id);
        return ResponseInfo.success();
    }

    @PutMapping("/enable/{id}")
    @ApiOperation("请验单配置-启用")
    @ApiParam(name = "id", value = "请验单id", required = true)
    public ResponseInfo<Void> enableInspectionConfig(@PathVariable @NotNull Long id) {
        documentConfigService.enableInspectionConfig(id);
        return ResponseInfo.success();
    }

    @PutMapping("/disable/{id}")
    @ApiOperation("请验单配置-停用")
    @ApiParam(name = "id", value = "请验单id", required = true)
    public ResponseInfo<Void> disableInspectionConfig(@PathVariable @NotNull Long id) {
        documentConfigService.disableInspectionConfig(id);
        return ResponseInfo.success();
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("请验单配置-详情")
    @ApiParam(name = "id", value = "请验单id", required = true)
    public ResponseInfo<DocumentConfigWithFieldVO> queryDetail(@PathVariable("id") @NotNull Long id) {
        return ResponseInfo.success(BeanUtil.copyProperties(documentConfigService.queryDetail(id), DocumentConfigWithFieldVO.class));
    }

    @PostMapping("/bind")
    @ApiOperation("请验单配置-请验单绑定检品")
    @OperationLog
    @DistributedLock(expression = "#dto.id")
    public ResponseInfo<Void> bindProduct(@RequestBody @Validated DocumentConfigBindProductVO dto) {
        documentConfigService.bindProduct(BeanUtil.copyProperties(dto, DocumentConfigBindProductDTO.class));
        return ResponseInfo.success();
    }

    @GetMapping("/material/{id}")
    @ApiOperation("请验单配置-获取绑定检品列表")
    @ApiParam(name = "id", value = "请验单id", required = true)
    public ResponseInfo<List<Long>> queryBindProductList(@PathVariable @NotNull Long id) {
        return ResponseInfo.success(documentConfigService.queryBindProductIdList(id));
    }

    @GetMapping("/list/{materialId}")
    @ApiOperation("请验单配置-根据检品id获取列表")
    @ApiParam(name="productId", value = "检品id", required = true)
    public ResponseInfo<List<DocumentConfigVO>> getDocumentConfigListByMaterialId(@PathVariable @NotNull Long materialId) {
        return ResponseInfo.success(BeanUtil.copyToList(documentConfigService.getInspectionConfigListByProductId(materialId), DocumentConfigVO.class));
    }

    @GetMapping("/enabled-list/{materialId}")
    @ApiOperation("请验单配置-根据检品id获取启用模板下拉列表")
    @ApiParam(name = "materialId", value = "检品id", required = true)
    public ResponseInfo<List<DocumentConfigVO>> getEnabledDocumentConfigListByMaterialId(@PathVariable @NotNull Long materialId) {
        return ResponseInfo.success(BeanUtil.copyToList(documentConfigService.getEnabledInspectionConfigListByProductId(materialId), DocumentConfigVO.class));
    }

    @GetMapping("/preview/{id}")
    @ApiOperation("请验单配置-打印预览PDF")
    @ApiParam(name = "id", value = "请验单id", required = true)
    public ResponseEntity<byte[]> preview(@PathVariable("id") @NotNull Long id,
                                          @RequestParam(value = "download", required = false, defaultValue = "false") boolean download) {
        // 直接复用检验单服务的PDF渲染逻辑（基于配置模拟空数据）
        byte[] pdfBytes = inspectionOrderService.previewInspectionOrderPdfByConfigId(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String disposition = download ? "attachment" : "inline";
        headers.set(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=inspection-config-preview-" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    @PostMapping("/preview")
    @ApiOperation("请验单配置-打印预览PDF（未保存字段值）")
    public ResponseEntity<byte[]> previewWithInput(@RequestBody DocumentConfigPreviewReqVO  vo,
                                                   @RequestParam(value = "download", required = false, defaultValue = "false") boolean download) {
        byte[] pdfBytes = inspectionOrderService.previewInspectionOrderPdfByConfigId(vo.getFields());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String disposition = download ? "attachment" : "inline";
        headers.set(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=inspection-config-preview"  + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
