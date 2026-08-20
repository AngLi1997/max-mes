package com.bmos.mes.service.formula.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.formula.dto.*;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.formula.vo.*;
import com.bmos.mes.service.process.service.ProcessVersionService;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/product/formula")
@Api(tags = "配方配置")
public class ProductFormulaConfigureController {

    @Autowired
    private ProductFormulaConfigureService productFormulaConfigureService;

    @PostMapping("/save")
    @ApiOperation("新增配方")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> saveProductFormula(@RequestBody @Validated ProductFormulaSaveDTO dto) {
        productFormulaConfigureService.saveProductFormula(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("配方分页")
    public ResponseInfo<CommonPage<ProductFormulaPageVO>> getProductFormulaPage(@Validated ProductFormulaPageQueryDTO dto) {
        return ResponseInfo.success(productFormulaConfigureService.getProductFormulaPage(dto));
    }

    @PostMapping("/version/save")
    @ApiOperation("新增配方版本")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> saveProductFormulaVersion(@Validated @RequestBody ProductFormulaSaveVersionDTO dto) {
        productFormulaConfigureService.saveProductFormulaVersion(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/version/detail")
    @ApiOperation("版本详情")
    public ResponseInfo<ProductFormulaVersionDetailVO> getProductFormulaVersionDetail(@NotNull Long versionId) {
        return ResponseInfo.success(productFormulaConfigureService.getProductFormulaVersionDetail(versionId));
    }

    @GetMapping("/version/detailByProcess")
    @ApiOperation("版本详情:根据工艺版本查询")
    public ResponseInfo<ProductFormulaVersionDetailVO> getProductFormulaVersionDetailByProcess(@Validated FormulaVersionDetailByProcessDTO detailByProcessDTO) {
        return ResponseInfo.success(productFormulaConfigureService.getProductFormulaVersionDetailByProcess(detailByProcessDTO));
    }


    @GetMapping("/version/page")
    @ApiOperation("版本分页")
    public ResponseInfo<CommonPage<ProductFormulaVersionPageVO>> getProductFormulaVersionPage(@Validated ProductFormulaVersionPageQueryDTO dto) {
        return ResponseInfo.success(productFormulaConfigureService.getProductFormulaVersionPage(dto));
    }

    @PostMapping("/version/edit")
    @ApiOperation("版本编辑")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> editProductFormulaVersion(@RequestBody @Validated ProductFormulaVersionEditDTO dto) {
        productFormulaConfigureService.editProductFormulaVersion(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/version/changeState")
    @ApiOperation("修改启停状态")
    @OperationLog
    public ResponseInfo<Void> changeProductFormulaVersionStatus(@Validated @RequestBody ProductFormulaVersionChangeStateDTO dto) {
        productFormulaConfigureService.changeProductFormulaVersionState(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/audit/submit")
    @ApiOperation("提交审核")
    @OperationLog
    public ResponseInfo<Void> auditProductFormulaVersion(@RequestBody @Validated ProductFormulaVersionAuditDTO dto) {
        productFormulaConfigureService.auditProductFormulaVersion(dto.getVersionId());
        return ResponseInfo.success();
    }

    @GetMapping("/audit/page")
    @ApiOperation("配方审批分页")
    public ResponseInfo<CommonPage<ProductFormulaAuditPageVO>> getProductFormulaAuditPage(@Validated ProductFormulaAuditPageQueryDTO dto) {
        return ResponseInfo.success(productFormulaConfigureService.getProductFormulaAuditPage(dto));
    }

    @GetMapping("/enableList")
    @ApiOperation("启用的产品配方列表")
    public ResponseInfo<List<ProductFormulaListVO>> getEnableProductFormulaList(@ApiParam(value = "产品id", name = "productId", required = true)@NotNull Long productId){
        return ResponseInfo.success(productFormulaConfigureService.getEnableProductFormulaList(productId));
    }

    @GetMapping("/process/enableList")
    @ApiOperation("启用的产品配方列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "产品id", name = "productId", required = true),
            @ApiImplicitParam(value = "工艺版本id", name = "processVersionId"),
    })
    public ResponseInfo<List<ProductFormulaListVO>> getProcessEnableProductFormulaList(@NotNull Long productId,Long processVersionId){
        return ResponseInfo.success(productFormulaConfigureService.getProcessEnableProductFormulaList(productId,processVersionId));
    }

    @GetMapping("/material/list")
    @ApiOperation("产品配方物料列表")
    public ResponseInfo<List<ProductFormulaMaterialListVO>> getProductFormulaMaterialList(@ApiParam(value = "配方版本id", name = "versionId", required = true)@NotNull Long versionId){
        return ResponseInfo.success(productFormulaConfigureService.getProductFormulaMaterialPullDownList(versionId));
    }

    @GetMapping("/model/material/list")
    @ApiOperation("产品配方物料列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "配方版本id", name = "versionId", required = true),
            @ApiImplicitParam(value = "工序步骤id", name = "procedureModelId"),
    })
    public ResponseInfo<List<ProductFormulaMaterialListVO>> getModelMaterialList(@NotNull Long versionId,Long procedureModelId){
        return ResponseInfo.success(productFormulaConfigureService.getModelMaterialList(versionId,procedureModelId));
    }

    @GetMapping("/material/listByProcedureId")
    @ApiOperation("获取工序绑定物料列表")
    public ResponseInfo<List<ProductFormulaMaterialListVO>> getProductFormulaMaterialListByProcedureId(@Validated ListProcedureMaterialDTO dto){
        return ResponseInfo.success(productFormulaConfigureService.getFormulaMaterialVOListByProcedureModelId(dto));
    }

    @GetMapping("/material/listByProcess")
    @ApiOperation("根据工艺版本id获取配方物料列表")
    public ResponseInfo<List<ProductFormulaMaterialListVO>> getProductFormulaMaterialListByProcess(@Validated ListProcessMaterialDTO dto){
        return ResponseInfo.success(productFormulaConfigureService.getFormulaMaterialListByProcessVersionId(dto));
    }


}
