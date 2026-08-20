package com.bmos.mes.service.plan.document.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.plan.document.controller.vo.TemplateCategoryTreeVO;
import com.bmos.mes.service.plan.document.service.BatchTemplateCategoryService;
import com.bmos.mes.service.plan.document.service.dto.TemplateCategorySaveDTO;
import com.bmos.mes.service.plan.document.service.dto.TemplateCategoryUpdateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 批记录模板接口（用于归档）
 */
@RestController
@RequestMapping("/plan/archive/template/category")
@Api(tags = "批记录模板分类接口")
public class BatchTemplateCategoryController {

    @Autowired
    BatchTemplateCategoryService batchTemplateCategoryService;

    @ApiOperation(value = "添加记录配置分类")
    @PostMapping("/save")
    @OperationLog
    public ResponseInfo<Void> saveCategory(@Validated @RequestBody TemplateCategorySaveDTO dto) {
        batchTemplateCategoryService.saveCategory(dto);
        return ResponseInfo.success();
    }


    @ApiOperation(value = "编辑分类")
    @PutMapping("/update")
    @OperationLog
    public ResponseInfo<Boolean> updateCategory(@Validated @RequestBody TemplateCategoryUpdateDTO dto) {
        batchTemplateCategoryService.updateCategory(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除分类")
    @ApiParam(name = "id", value = "分类id", required = true)
    @OperationLog
    public ResponseInfo<Void> deleteCategory(@PathVariable("id") Long id) {
        batchTemplateCategoryService.deleteCategory(id);
        return ResponseInfo.success();
    }

    @GetMapping("/tree")
    @ApiOperation(value = "查询分类树")
    public ResponseInfo<List<TemplateCategoryTreeVO>> categoryTree() {
        return ResponseInfo.success(batchTemplateCategoryService.categoryTree());
    }

}
