package com.bmos.wms.service.cargo.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.wms.service.cargo.dto.CargoCategoryCreateDTO;
import com.bmos.wms.service.cargo.service.ICargoCategoryService;
import com.bmos.wms.service.cargo.vo.CargoCategoryVO;
import com.bmos.wms.service.cargo.vo.CargoTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 货品分类相关接口
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 19:34
 */
@RestController
@RequestMapping("/cargo/category")
@Api(tags = "货品分类相关接口")
@Validated
public class CargoCategoryController {

    @Resource
    private ICargoCategoryService cargoCategoryService;

    @GetMapping("/queryTree")
    @ApiOperation(value = "查询全部货品分类树")
    public ResponseInfo<List<CargoCategoryVO>> queryTree() {
        return ResponseInfo.success(cargoCategoryService.queryTree());
    }

    @GetMapping("/queryTreeWithCargo")
    @ApiOperation(value = "根据父级id查询存储区域数据树(带有货品列表)", notes = "父节点为空则查询根节点")
    public ResponseInfo<List<CargoTreeVO>> queryTreeWithCargo() {
        return ResponseInfo.success(cargoCategoryService.queryTreeWithCargo());
    }

    @GetMapping("/queryById")
    @ApiOperation(value = "根据货品分类id查询货品分类")
    @ApiImplicitParam(value = "id", name = "id", example = "1", required = true)
    public ResponseInfo<CargoCategoryVO> queryById(@RequestParam @NotNull Long id) {
        return ResponseInfo.success(cargoCategoryService.queryById(id));
    }

    @GetMapping("/queryByCode")
    @ApiOperation(value = "根据货品分类编码查询货品分类")
    @ApiImplicitParam(value = "货品分类编码", name = "code", example = "F01", required = true)
    public ResponseInfo<CargoCategoryVO> queryByCode(@RequestParam @NotBlank String code) {
        return ResponseInfo.success(cargoCategoryService.queryByCode(code));
    }

    @PostMapping("/create")
    @ApiOperation(value = "新增货品分类")
    @OperationLog
    public ResponseInfo<Void> createCargoCategory(@Validated @RequestBody CargoCategoryCreateDTO dto) {
        cargoCategoryService.createCargoCategory(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除货品分类")
    @OperationLog
    @ApiImplicitParam(value = "id", name = "id", example = "1", required = true)
    public ResponseInfo<Void> deleteCargoCategory(@RequestParam @NotNull Long id) {
        cargoCategoryService.deleteCargoCategory(id);
        return ResponseInfo.success();
    }
}
