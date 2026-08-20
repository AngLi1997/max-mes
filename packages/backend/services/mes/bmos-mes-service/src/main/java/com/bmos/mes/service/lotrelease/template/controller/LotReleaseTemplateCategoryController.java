package com.bmos.mes.service.lotrelease.template.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.lotrelease.template.dto.LotReleaseTemplateCategoryCreateDTO;
import com.bmos.mes.service.lotrelease.template.dto.LotReleaseTemplateCategoryEditDTO;
import com.bmos.mes.service.lotrelease.template.service.ILotReleaseTemplateCategoryService;
import com.bmos.mes.service.lotrelease.template.vo.LotReleaseTemplateCategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 批签发模板分类相关接口
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 14:02
 */
@RestController
@RequestMapping("/lotRelease/template/category")
@Api(tags = "批签发模板分类相关接口")
public class LotReleaseTemplateCategoryController {

    @Resource
    private ILotReleaseTemplateCategoryService lotReleaseTemplateCategoryService;

    @PostMapping("/createCategory")
    @ApiOperation("新增批签发模板分类")
    @OperationLog
    public ResponseInfo<Void> createCategory(@RequestBody @Validated LotReleaseTemplateCategoryCreateDTO dto) {
        lotReleaseTemplateCategoryService.createCategory(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/editCategory")
    @ApiOperation("修改批签发模板分类")
    @OperationLog
    public ResponseInfo<Void> editCategory(@RequestBody @Validated LotReleaseTemplateCategoryEditDTO dto) {
        lotReleaseTemplateCategoryService.editCategory(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除批签发模板分类")
    @OperationLog
    @ApiImplicitParam(name = "id", value = "批签发模板分类id", required = true)
    public ResponseInfo<Void> deleteCategory(@RequestParam Long id) {
        lotReleaseTemplateCategoryService.deleteCategory(id);
        return ResponseInfo.success();
    }

    @ApiOperation("查询批签发模板分类树")
    @GetMapping("/tree")
    public ResponseInfo<List<LotReleaseTemplateCategoryVO>> queryCategoryTree() {
        return ResponseInfo.success(lotReleaseTemplateCategoryService.queryCategoryTree());
    }
}
