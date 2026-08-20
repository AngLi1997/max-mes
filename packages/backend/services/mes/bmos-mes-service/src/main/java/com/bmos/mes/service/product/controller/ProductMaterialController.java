package com.bmos.mes.service.product.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.service.product.dto.*;
import com.bmos.mes.service.product.service.ProductMaterialCategoryService;
import com.bmos.mes.service.product.service.ProductMaterialService;
import com.bmos.mes.service.product.vo.*;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.material.dto.MaterialTreeNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/product/material")
@Api(tags = "生产物料相关接口")
@Validated
public class ProductMaterialController {

    @Autowired
    private ProductMaterialCategoryService materialCategoryService;

    @Autowired
    private ProductMaterialService materialService;

    @PostMapping("/category/save")
    @ApiOperation("保存生产物料分类")
    public ResponseInfo<Void> saveCategory(@RequestBody @Validated ProductMaterialCategorySaveDTO dto) {
        materialCategoryService.save(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/category/delete/{id}")
    @ApiOperation("删除生产物料分类")
    public ResponseInfo<Void> deleteCategory(@PathVariable Long id) {
        materialCategoryService.delete(id);
        return ResponseInfo.success();
    }

    @PutMapping("/category/update")
    @ApiOperation("编辑生产物料分类")
    public ResponseInfo<Void> updateCategory(@Validated @RequestBody ProductMaterialCategoryUpdateDTO dto) {
        materialCategoryService.update(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/category/tree")
    @ApiOperation("生产物料分类树")
    public ResponseInfo<List<ProductMaterialCategoryTreeNodeVO>> queryCategoryTree(@Validated @RequestBody ProductMaterialCategoryQueryDTO queryDto) {
        return ResponseInfo.success(materialCategoryService.queryCategoryTree(queryDto));
    }

    @PostMapping("/save")
    @ApiOperation("生产物料保存")
    public ResponseInfo<Void> saveMaterial(@RequestBody @Validated ProductMaterialSaveDTO dto) {
        materialService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("生产物料编辑")
    public ResponseInfo<Void> updateMaterial(@RequestBody @Validated ProductMaterialUpdateDTO dto) {
        materialService.update(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("生产物料删除")
    @ApiParam(value = "id", name = "id", required = true)
    public ResponseInfo<Void> deleteMaterial(@PathVariable Long id) {
        materialService.delete(id);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("生产物料分页")
    public ResponseInfo<CommonPage<ProductMaterialPageVO>> getPage(@Validated ProductMaterialPageQueryDTO pageVO) {
        return ResponseInfo.success(materialService.getPage(pageVO));
    }

    @GetMapping("/detail")
    @ApiOperation("生产物料详情查询")
    public ResponseInfo<ProductMaterialDetailVO> getDetail(@NotNull Long id) {
        return ResponseInfo.success(materialService.getDetail(id));
    }

    @PutMapping("/changeStatus")
    @ApiOperation("改变物料启停状态")
    public ResponseInfo<Void> changeStatus(@Validated @RequestBody ProductMaterialChangeStatusDTO dto) {
        materialService.changeStatus(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/principal/list")
    @ApiOperation("查询能被关联的物料列表")
    public ResponseInfo<List<PrincipalMaterialVO>> getPrincipalList(@Validated MaterialPrincipalQueryDTO dto) {
        return ResponseInfo.success(materialService.getPrincipalList(dto));
    }

    /**
     * 平台下发物料及分类的接口
     *
     * @return
     */
    @PostMapping("/issueMaterialAndCategory")
    public ResponseInfo<Void> issueMaterialAndCategory(@RequestBody RemoteIssueDTO dto) {
        materialService.issueMaterialAndCategory(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/syncTree")
    @ApiOperation("获取同步分类物料树")
    public ResponseInfo<List<MaterialTreeNodeVO>> getSyncTree(@Validated SyncTreeQueryDTO dto) {
        return ResponseInfo.success(materialService.getSyncTree(dto));
    }

    @GetMapping("/syncTreeAll")
    @ApiOperation("获取同步分类全量树")
    public ResponseInfo<List<SyncTreeNodeVO>> getSyncTreeAll() {
        return ResponseInfo.success(materialService.getSyncTreeAll());
    }

    @PostMapping("/sync")
    @ApiOperation("同步物料和分类")
    public ResponseInfo<Void> syncMaterialAndCategory(@RequestBody @Validated SyncMaterialInfoDTO dto) {
        materialService.syncMaterialAndCategory(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/productList")
    @ApiOperation("获取产品信息下拉列表")
    public ResponseInfo<List<ProductListVO>> getProductList(@EnumValidate(value = CategoryInfoTypeEnum.class) @NotNull Integer categoryType) {
        return ResponseInfo.success(materialService.getProductList(categoryType));
    }

    @GetMapping("/productTree")
    @ApiOperation("产品树")
    public ResponseInfo<List<ProductCategoryTreeNodeVO>> getProductTree(@EnumValidate(value = CategoryInfoTypeEnum.class) @NotNull Integer categoryType){
        return ResponseInfo.success(materialService.getProductTree(categoryType));
    }

    @PostMapping("/save/batchRecord")
    @ApiOperation("绑定批记录")
    public ResponseInfo<Void> bindBatchRecords(@Validated @RequestBody RecordSaveDTO dto){
        materialService.bindBatchRecords(dto);
        return ResponseInfo.success();
    }

    @ApiOperation("查询产品绑定的批记录id列表")
    @GetMapping("/bindRecordIds")
    public ResponseInfo<List<Long>> getProductBindRecordIds(@NotNull Long productId){
        return ResponseInfo.success(materialService.getProductBindRecordIds(productId));
    }

    @ApiOperation("查询分类下的所有子分类的id列表")
    @GetMapping("/category/allChildIds")
    public ResponseInfo<List<Long>> getAllChildCategory(@NotNull Long parentId){
        return ResponseInfo.success(materialService.getAllChildCategory(parentId));
    }

    @ApiOperation("产品树(产品信息、中间品)")
    @GetMapping("/allProductTree")
    public ResponseInfo<List<ProductCategoryTreeNodeVO>> getProductTree(@NotEmpty @RequestParam List<@EnumValidate(value = CategoryInfoTypeEnum.class) Integer> types){
        return ResponseInfo.success(materialService.getaLLProductTree(types));
    }

    @ApiOperation("成品列表")
    @GetMapping("/finishProductList")
    public ResponseInfo<List<ProductListVO>> getFinishProductList(@EnumValidate(value = CategoryInfoTypeEnum.class) @NotNull Integer categoryType){
        return ResponseInfo.success(materialService.getFinishProductList(categoryType));
    }

    @ApiOperation("成品树")
    @GetMapping("/finishProductTree")
    public ResponseInfo<List<ProductCategoryTreeNodeVO>> getFinishProductTree(@Validated FinishProductTreeQueryDTO dto){
        return ResponseInfo.success(materialService.getFinishProductTree(dto));
    }


}
