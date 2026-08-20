package com.bmos.mes.service.operate.controller;


import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.operate.dto.SaveCategoryDTO;
import com.bmos.mes.service.operate.dto.UpdateCategoryDTO;
import com.bmos.mes.service.operate.service.OperateRuleCategoryService;
import com.bmos.mes.service.operate.vo.OperateRuleCategoryVO;
import com.bmos.mes.service.operate.vo.OperateRuleSopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author renjinguang
 */
@RestController
@RequestMapping("/operate")
@Api(tags = "操作规程分类相关接口")
@Validated
public class OperateRuleCategoryController {

    @Autowired
    private OperateRuleCategoryService service;

    @GetMapping("/list/category")
    @ApiOperation(value = "查询操作规程分类树列表")
    public ResponseInfo<List<OperateRuleCategoryVO>> getListCategory() {
        return ResponseInfo.success(service.getListCategory());
    }

    @PostMapping("/save/category")
    @ApiOperation(value = "新增分类")
    public ResponseInfo<Void> saveCategory(@RequestBody @Validated SaveCategoryDTO dto) {
        service.saveCategory(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/update/category")
    @ApiOperation(value = "修改分类")
    public ResponseInfo<Void> updateCategory(@RequestBody @Validated UpdateCategoryDTO dto) {
        service.updateCategory(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete/category")
    @ApiOperation(value = "删除分类")
    @ApiParam(name = "id", value = "分类id", required = true)
    public ResponseInfo<Void> deleteCategory(@NotNull @Valid Long id){
        service.deleteCategory(id);
        return ResponseInfo.success();
    }

    @GetMapping("/list/sop")
    @ApiOperation(value = "工艺配置sop树")
    public ResponseInfo<List<OperateRuleSopVO>> listSop(){
        return ResponseInfo.success(service.listSop());
    }

}
