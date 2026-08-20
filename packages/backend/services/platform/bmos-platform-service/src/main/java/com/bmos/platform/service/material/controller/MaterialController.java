package com.bmos.platform.service.material.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.material.dto.MaterialTreeNodeVO;
import com.bmos.platform.service.material.dto.*;
import com.bmos.platform.service.material.service.MaterialCategoryService;
import com.bmos.platform.service.material.service.MaterialService;
import com.bmos.platform.service.material.vo.*;
import com.bmos.platform.service.util.UploadFileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/material")
@Validated
@Api(tags = "物料相关接口")
public class MaterialController {


    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialCategoryService categoryService;


    @PostMapping("/category/save")
    @ApiOperation("保存物料分类")
    public ResponseInfo<Long> saveCategory(@RequestBody @Validated MaterialCategorySaveDTO dto) {
        return ResponseInfo.success(categoryService.save(dto));
    }

    @PutMapping("/category/update")
    @ApiOperation("编辑物料分类")
    public ResponseInfo<Void> updateCategory(@RequestBody @Validated MaterialCategoryUpdateDTO dto) {
        categoryService.update(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/category/delete/{id}")
    @ApiOperation("删除分类")
    @ApiParam(value = "id", name = "id", required = true)
    public ResponseInfo<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseInfo.success();
    }


    @PostMapping("/save")
    @ApiOperation("保存物料")
    public ResponseInfo<Long> save(@RequestBody @Validated MaterialSaveDTO dto) {
        return ResponseInfo.success(materialService.save(dto));
    }

    @PostMapping("/update")
    @ApiOperation("更新物料")
    public ResponseInfo<Void> update(@RequestBody @Validated MaterialUpdateDTO dto) {
        materialService.update(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除物料")
    @ApiParam(value = "id", name = "id", required = true)
    public ResponseInfo<Void> delete(@PathVariable Long id) {
        materialService.deleteById(id);
        return ResponseInfo.success();
    }

    @PutMapping("/changeStatus")
    @ApiOperation("改变物料启停状态")
    public ResponseInfo<Void> changeStatus(@Validated @RequestBody MaterialChangeStatusDTO dto) {
        materialService.changeStatus(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("物料信息分页")
    public ResponseInfo<CommonPage<MaterialPageVO>> getPage(@Validated MaterialPageQueryDTO dto) {
        return ResponseInfo.success(materialService.getPage(dto));
    }

    @GetMapping("/principal/list")
    @ApiOperation("查询能被关联的物料列表")
    public ResponseInfo<List<MaterialVO>> getPrincipalList(@Validated MaterialPrincipalQueryDTO dto) {
        return ResponseInfo.success(materialService.getPrincipalList(dto));
    }

    @GetMapping("/detail")
    @ApiOperation("物料详情查询")
    public ResponseInfo<MaterialDetailVO> getDetail(@NotNull Long id) {
        return ResponseInfo.success(materialService.getDetail(id));
    }

    @GetMapping("/category/tree")
    @ApiOperation("物料分类树（全量）查询")
    public ResponseInfo<List<MaterialCategoryTreeNodeVO>> getCategoryTree() {
        return ResponseInfo.success(materialService.getCategoryTree());
    }

    @GetMapping("/tree")
    @ApiOperation("全量物料树(启用状态)")
    public ResponseInfo<List<MaterialTreeNodeVO>> getMaterialTree() {
        return ResponseInfo.success(materialService.getMaterialTree());
    }

    @GetMapping("/existed")
    public ResponseInfo<Boolean> checkMergeCodeExisted(@RequestParam("code") String code
            , @RequestParam(value = "platformMaterialId", required = false) Long platformMaterialId) {
        return ResponseInfo.success(materialService.checkMergeCodeExisted(code, platformMaterialId, null));
    }

    @PostMapping("/issue")
    @ApiOperation("物料下发")
    public ResponseInfo<Void> issueMaterial(@Validated @RequestBody MaterialIssueDTO dto) {
        materialService.issueMaterial(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/category/issueTree")
    @ApiOperation("物料分类下发树")
    public ResponseInfo<List<IssueTreeNodeVO>> getIssueTree(@NotNull Long parentId, String keyword) {
        return ResponseInfo.success(materialService.getIssueTree(parentId, keyword));
    }

    @GetMapping("/issueBusinesses")
    @ApiOperation("下发业务列表")
    public ResponseInfo<List<IssueBusinessVO>> getIssueBusinesses() {
        return ResponseInfo.success(materialService.getIssueBusinesses());
    }

    @PostMapping("/syncList")
    public ResponseInfo<RemoteSyncDTO> getSyncList(@RequestBody @Validated SyncMaterialInfoDTO dto) {
        return ResponseInfo.success(materialService.getSyncList(dto));
    }

    /**
     * 取消物料注册
     * @param dto
     * @return
     */
    @PostMapping("/unregister")
    public ResponseInfo<Void> unregisterMaterial(@RequestBody @Validated UnregisterMaterialDTO dto){
        materialService.unregisterMaterial(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/unregisterCategory")
    public ResponseInfo<Void> unregisterCategory(@RequestBody @Validated UnregisterMaterialCategoryDTO dto){
        categoryService.unregisterCategory(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/import/template")
    @ApiOperation("下载导入模板")
    public void downloadTemplate(HttpServletResponse response) {
        materialService.getImportTemplate(response);
    }


    @PostMapping("/import/material")
    @ApiOperation("导入物料信息")
    public void importMaterial(HttpServletResponse response,MultipartFile file) {
        UploadFileUtils.checkExcel(file);
        materialService.importMaterial(response, file);
    }

    @GetMapping("/export/material")
    @ApiOperation("导出物料信息")
    public void exportMaterial(HttpServletResponse response,@Validated MaterialExportDTO dto){
        materialService.exportMaterial(response,dto);
    }
}
