package com.bmos.mes.service.dataset.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.dataset.dto.DatasetCategoryCreateDTO;
import com.bmos.mes.service.dataset.dto.DatasetCategoryEditDTO;
import com.bmos.mes.service.dataset.service.IDatasetCategoryService;
import com.bmos.mes.service.dataset.vo.DatasetCategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据集分类接口
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 10:41
 */
@RestController
@RequestMapping("/dataset/category")
@Api(tags = "数据集分类接口")
public class DatasetCategoryController {

    @Resource
    private IDatasetCategoryService datasetCategoryService;

    @PostMapping("/createCategory")
    @ApiOperation("新增数据集分类")
    @OperationLog
    public ResponseInfo<Void> createCategory(@RequestBody @Validated DatasetCategoryCreateDTO dto) {
        datasetCategoryService.createCategory(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/editCategory")
    @ApiOperation("修改数据集分类")
    @OperationLog
    public ResponseInfo<Void> editCategory(@RequestBody @Validated DatasetCategoryEditDTO dto) {
        datasetCategoryService.editCategory(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除数据集分类")
    @OperationLog
    @ApiImplicitParam(name = "id", value = "数据集分类id", required = true)
    public ResponseInfo<Void> deleteCategory(@RequestParam Long id) {
        datasetCategoryService.deleteCategory(id);
        return ResponseInfo.success();
    }

    @ApiOperation("查询数据集分类树")
    @GetMapping("/tree")
    public ResponseInfo<List<DatasetCategoryVO>> queryCategoryTree() {
        return ResponseInfo.success(datasetCategoryService.queryCategoryTree());
    }
}
