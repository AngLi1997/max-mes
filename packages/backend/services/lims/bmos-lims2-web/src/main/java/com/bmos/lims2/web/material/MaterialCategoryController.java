package com.bmos.lims2.web.material;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.material.dto.MaterialCategoryCreateDTO;
import com.bmos.lims2.server.material.dto.MaterialCategoryTreeQueryDTO;
import com.bmos.lims2.server.material.dto.MaterialCategoryUpdateDTO;
import com.bmos.lims2.server.material.service.MaterialCategoryService;
import com.bmos.lims2.server.material.service.MaterialService;
import com.bmos.lims2.web.material.vo.req.MaterialCategoryCreateReqVO;
import com.bmos.lims2.web.material.vo.req.MaterialCategoryUpdateReqVO;
import com.bmos.lims2.web.material.vo.req.MaterialTreeReqVO;
import com.bmos.lims2.web.material.vo.req.MaterialCategoryReqVO;
import com.bmos.lims2.web.material.vo.resp.MaterialTreeNodeVO;
import com.bmos.logging.annotation.OperationLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检品
 */
@RestController
@RequestMapping("/material/category")
@Api(tags = "检品分类管理-接口")
@Validated
public class MaterialCategoryController {

    @Autowired
    MaterialCategoryService materialCategoryService;

    @Autowired
    MaterialService materialService;

    @PostMapping("/save")
    @ApiOperation("新增检品分类")
    @OperationLog
    public ResponseInfo<Void> saveCategory(@RequestBody @Validated MaterialCategoryCreateReqVO reqVO) {
        materialCategoryService.saveCategory(BeanUtil.copyProperties(reqVO, MaterialCategoryCreateDTO.class));
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除检品分类")
    @OperationLog
    public ResponseInfo<Void> deleteCategory(@PathVariable Long id) {
        materialCategoryService.deleteCategory(id);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("编辑检品分类")
    @OperationLog
    public ResponseInfo<Void> updateCategory(@Validated @RequestBody MaterialCategoryUpdateReqVO reqVO) {
        materialCategoryService.updateCategory(BeanUtil.copyProperties(reqVO, MaterialCategoryUpdateDTO.class));
        return ResponseInfo.success();
    }

    @PostMapping("/tree")
    @ApiOperation("查询当前系统内的分类树")
    public ResponseInfo<List<MaterialTreeNodeVO>> queryCategoryTree(@Validated @RequestBody MaterialCategoryReqVO reqVO) {
        return ResponseInfo.success(BeanUtil.copyToList(materialCategoryService.queryCategoryTree(BeanUtil.copyProperties(reqVO, MaterialCategoryTreeQueryDTO.class)), MaterialTreeNodeVO.class));
    }

    @GetMapping("/syncTree")
    @ApiOperation("获取同步分类物料树（包含物料）")
    public ResponseInfo<List<MaterialTreeNodeVO>> getSyncTree(@Validated MaterialTreeReqVO reqVO) {
        return ResponseInfo.success(BeanUtil.copyToList(materialCategoryService.getSyncTree(BeanUtil.copyProperties(reqVO, MaterialCategoryTreeQueryDTO.class)), MaterialTreeNodeVO.class));
    }

    @GetMapping("/syncTreeAll")
    @ApiOperation("获取同步分类全量树")
    public ResponseInfo<List<MaterialTreeNodeVO>> getSyncTreeAll() {
        return ResponseInfo.success(BeanUtil.copyToList(materialCategoryService.getSyncTreeAll(), MaterialTreeNodeVO.class));
    }

}
