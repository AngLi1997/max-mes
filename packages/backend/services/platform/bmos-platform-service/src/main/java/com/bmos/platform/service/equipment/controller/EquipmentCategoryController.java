package com.bmos.platform.service.equipment.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.platform.service.equipment.controller.vo.CategoryTreeNodeVO;
import com.bmos.platform.service.equipment.controller.vo.CategoryVO;
import com.bmos.platform.service.equipment.service.EquipmentCategoryService;
import com.bmos.platform.service.equipment.service.dto.CategorySaveDTO;
import com.bmos.platform.service.equipment.service.dto.CategoryUpdateDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 设备分类建模相关接口
 */
@RestController
@RequestMapping("/equipment/category")
@Validated
@Api(tags = "设备分类接口")
public class EquipmentCategoryController {

    @Autowired
    private EquipmentCategoryService equipmentCategoryService;

    @PostMapping("/save")
    @ApiOperation("新建设备分类")
    @OperationLog
    public ResponseInfo<Void> saveCategory(@RequestBody CategorySaveDTO dto){
        equipmentCategoryService.saveCategory(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("修改设备分类")
    @OperationLog
    public ResponseInfo<Void> updateCategory(@RequestBody CategoryUpdateDTO dto){
        equipmentCategoryService.updateCategory(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除设备分类")
    @OperationLog
    public ResponseInfo<Void> deleteCategory(@PathVariable @NotNull @ApiParam("设备分类模型id") Long id){
        equipmentCategoryService.deleteCategory(id);
        return ResponseInfo.success();
    }

    @GetMapping("/list")
    @ApiOperation("获取设备分类树")
    public ResponseInfo<List<CategoryTreeNodeVO>> getCategoryTree(){
        return ResponseInfo.success(equipmentCategoryService.getCategoryTree());
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取设备分类基础信息")
    public ResponseInfo<CategoryVO> getCategoryTreeInfo(@PathVariable @NotNull @ApiParam("设备分类模型id") Long id){
        return ResponseInfo.success(equipmentCategoryService.getCategoryTreeInfo(id));
    }

}
