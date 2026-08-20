package com.bmos.lims2.web.material;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.material.dto.*;
import com.bmos.lims2.server.material.dto.MaterialInspectTreeWithDocumentDTO;
import com.bmos.lims2.server.material.service.MaterialService;
import com.bmos.lims2.server.platform.material.dto.MaterialPrincipalQueryDTO;
import com.bmos.lims2.web.material.vo.req.MaterialPageReqVO;
import com.bmos.lims2.web.material.vo.req.MaterialSaveVO;
import com.bmos.lims2.web.material.vo.req.MaterialSyncReqVO;
import com.bmos.lims2.web.material.vo.req.MaterialUpdateReqVO;
import com.bmos.lims2.web.material.vo.resp.MaterialEasyInfoVO;
import com.bmos.lims2.web.material.vo.resp.MaterialWithFieldVO;
import com.bmos.lims2.web.material.vo.resp.PrincipalMaterialVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 检品
 */

@RestController
@RequestMapping("/material")
@Api(tags = "检品管理-接口")
@Validated
public class MaterialController {

    @Autowired
    MaterialService materialService;

    @PostMapping("/sync")
    @ApiOperation("同步物料(包含物料分类)")
    public ResponseInfo<Void> syncInspectionProducts(@RequestBody @Validated MaterialSyncReqVO reqVO) {
        materialService.syncMaterialAndCategory(BeanUtil.copyProperties(reqVO, MaterialSyncDTO.class));
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除检品")
    public ResponseInfo<Void> deleteProducts(@PathVariable Long id) {
        materialService.deleteProducts(id);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("编辑检品信息")
    public ResponseInfo<Void> updateProducts(@RequestBody @Validated MaterialUpdateReqVO reqVO) {
        materialService.updateProducts(BeanUtil.copyProperties(reqVO, MaterialUpdateDTO.class));
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("检品列表分页查询")
    public ResponseInfo<CommonPage<MaterialDTO>> inspectionProductsPage(@Validated MaterialPageReqVO reqVO) {
        return ResponseInfo.success(materialService.inspectionProductsPage(BeanUtil.copyProperties(reqVO, MaterialPageQueryDTO.class)));
    }

    @GetMapping("/list")
    @ApiOperation("检品简单信息全量查询")
    public ResponseInfo<List<MaterialEasyInfoVO>> productsEasyInfo(String keyword) {
        return ResponseInfo.success(BeanUtil.copyToList(materialService.productsEasyInfo(keyword), MaterialEasyInfoVO.class));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("检品详情查询")
    public ResponseInfo<MaterialWithFieldVO> productsInfo(@PathVariable Long id) {
        return ResponseInfo.success(BeanUtil.copyProperties(materialService.productsInfo(id), MaterialWithFieldVO.class));
    }

    @PostMapping("/save")
    @ApiOperation("新增检品")
    public ResponseInfo<Void> saveInspectionProduct(@RequestBody @Validated MaterialSaveVO dto) {
        materialService.saveInspectionProduct(BeanUtil.copyProperties(dto, MaterialSaveDTO.class));
        return ResponseInfo.success();
    }

    @PutMapping("/enable/{id}")
    @ApiOperation("启用检品")
    @ApiParam(name = "id", value = "检品id", required = true)
    public ResponseInfo<Void> enableInspectProduct(@PathVariable Long id) {
        materialService.enableInspectProduct(id);
        return ResponseInfo.success();
    }

    @PutMapping("/disable/{id}")
    @ApiOperation("停用检品")
    @ApiParam(name = "id", value = "检品id", required = true)
    public ResponseInfo<Void> disableInspectProduct(@PathVariable Long id) {
        materialService.disableInspectProduct(id);
        return ResponseInfo.success();
    }

    @GetMapping("/principal/list")
    @ApiOperation("查询能被关联的物料列表")
    public ResponseInfo<List<PrincipalMaterialVO>> getPrincipalList(@Validated MaterialPrincipalQueryDTO dto) {
        return ResponseInfo.success(BeanUtil.copyToList(materialService.getPrincipalList(dto), PrincipalMaterialVO.class));
    }

    @GetMapping("/tree")
    @ApiOperation("查询检品分类和检品的完整树形结构")
    @ApiParam(name = "categoryType", value = "分类类型，可为空")
    public ResponseInfo<List<MaterialInspectTreeNodeDTO>> getMaterialInspectTree(
            @RequestParam(required = false) Integer categoryType) {
        return ResponseInfo.success(materialService.getMaterialInspectTree(categoryType));
    }

    @GetMapping("/tree/with-document")
    @ApiOperation("查询绑定了启用请验单的检品树（检品已绑定启用的请验单），树上节点带请验单的ID")
    @ApiParam(name = "categoryType", value = "分类类型，可为空")
    public ResponseInfo<List<MaterialInspectTreeWithDocumentDTO>> getMaterialInspectTreeWithDocument(
            @RequestParam(required = false) Integer categoryType) {
        return ResponseInfo.success(materialService.getMaterialInspectTreeWithDocument(categoryType));
    }

    @GetMapping("/tree/with-schemes")
    @ApiOperation("物料树（子节点为检验方案），节点含nodeType区分")
    @ApiParam(name = "categoryType", value = "分类类型，可为空")
    public ResponseInfo<List<MaterialTreeWithSchemeNodeDTO>> getMaterialTreeWithSchemes(
            @RequestParam(required = false) Integer categoryType) {
        return ResponseInfo.success(materialService.getMaterialTreeWithSchemes(categoryType));
    }

}
