package com.bmos.mes.service.weigh.centre.config.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreCategoryCreateDTO;
import com.bmos.mes.service.weigh.centre.config.dto.WeighCentreCategoryEditDTO;
import com.bmos.mes.service.weigh.centre.config.service.IWeighCentreCategoryService;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 称量中心分类接口
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:20
 */
@RestController
@RequestMapping("/weigh/centre/category")
@Api(tags = "称量中心分类接口")
public class WeighCentreCategoryController {

    @Resource
    private IWeighCentreCategoryService weighCentreCategoryService;

    @ApiOperation("查询称量中心分类树")
    @GetMapping("/tree")
    public ResponseInfo<List<WeighCentreCategoryVO>> categoryTree() {
        return ResponseInfo.success(weighCentreCategoryService.categoryTree());
    }

    @ApiOperation("创建称量中心分类")
    @PostMapping("/create")
    @OperationLog
    public ResponseInfo<Void> createCategory(@Validated @RequestBody WeighCentreCategoryCreateDTO createDTO) {
        weighCentreCategoryService.createCategory(createDTO);
        return ResponseInfo.success();
    }

    @ApiOperation("编辑称量中心分类")
    @PutMapping("/edit")
    @OperationLog
    public ResponseInfo<Void> editCategory(@Validated @RequestBody WeighCentreCategoryEditDTO editDTO) {
        weighCentreCategoryService.editCategory(editDTO);
        return ResponseInfo.success();
    }

    @ApiOperation("删除称量中心分类")
    @DeleteMapping("/delete")
    @OperationLog
    public ResponseInfo<Void> deleteCategory(@RequestParam Long id) {
        weighCentreCategoryService.deleteCategory(id);
        return ResponseInfo.success();
    }
}
