package com.bmos.platform.service.system.expression.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.system.expression.dto.*;
import com.bmos.platform.service.system.expression.service.ExpressionCategoryService;
import com.bmos.platform.service.system.expression.service.ExpressionService;
import com.bmos.platform.service.system.expression.vo.ExpressionCategoryTreeNodeVO;
import com.bmos.platform.service.system.expression.vo.ExpressionPageVO;
import com.bmos.platform.service.system.expression.vo.ExpressionTreeNodeVO;
import com.bmos.platform.service.system.expression.vo.MesRecordTreeNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/expression")
@Api(tags = "公式配置")
public class ExpressionController {
    @Autowired
    private ExpressionService expressionService;

    @Autowired
    private ExpressionCategoryService expressionCategoryService;

    @GetMapping("/category/tree")
    @ApiOperation("公式分类树")
    public ResponseInfo<List<ExpressionCategoryTreeNodeVO>> getCategoryTree() {
        return ResponseInfo.success(expressionCategoryService.getCategoryTree());
    }

    @PostMapping("/category/save")
    @ApiOperation("公式分类保存")
    @OperationLog
    public ResponseInfo<Void> saveCategory(@RequestBody @Validated ExpressionCategorySaveDTO dto) {
        expressionCategoryService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/category/update")
    @ApiOperation("公式分类更新")
    @OperationLog
    public ResponseInfo<Void> updateCategory(@RequestBody @Validated ExpressionCategoryUpdateDTO dto) {
        expressionCategoryService.update(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/category/delete/{id}")
    @ApiOperation("公式分类删除")
    @OperationLog
    @ApiParam(value = "id", name = "id", required = true)
    public ResponseInfo<Void> deleteCategory(@PathVariable Long id) {
        expressionService.deleteCategory(id);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("公式列表")
    public ResponseInfo<CommonPage<ExpressionPageVO>> page(ExpressionPageDTO dto) {
        return ResponseInfo.success(
            CommonPage.convertPage(expressionService.page(dto))
        );
    }

    @GetMapping("/list")
    @ApiOperation("公式列表--查询已确认的公式  mes系统使用")
    public ResponseInfo<List<ExpressionPageVO>> list() {
        return ResponseInfo.success(expressionService.list());
    }

    @PostMapping("/save")
    @ApiOperation("公式保存")
    @OperationLog
    public ResponseInfo<Void> save(@RequestBody @Validated ExpressionSaveDTO dto) {
        expressionService.save(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("公式更新")
    @OperationLog
    public ResponseInfo<Void> update(@RequestBody @Validated ExpressionUpdateDTO dto) {
        expressionService.update(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/confirm/{id}")
    @ApiOperation("公式确认")
    @OperationLog
    public ResponseInfo<Void> confirm(@PathVariable Long id) {
        expressionService.confirm(id);
        return ResponseInfo.success();
    }

    @PutMapping("/verify/{id}")
    @ApiOperation("验证通过")
    public ResponseInfo<Void> verify(@PathVariable Long id) {
        expressionService.verify(id);
        return ResponseInfo.success();
    }


    @DeleteMapping("/delete/{id}")
    @ApiOperation("公式删除")
    @OperationLog
    @ApiParam(value = "id", name = "id", required = true)
    public ResponseInfo<Void> delete(@PathVariable Long id) {
        expressionService.delete(id);
        return ResponseInfo.success();
    }

    @PostMapping("/parse")
    @ApiOperation("公式解析")
    @ApiParam(value = "expression", name = "公式", required = true)
    public ResponseInfo<Set<String>> parse(@RequestBody @Validated ExpressionParseDTO dto) {
        return ResponseInfo.success(expressionService.parse(dto.getExpression()));
    }

    @GetMapping("/fullExpressionList")
    @ApiOperation("所有分类列表及已确认公式列表")
    public ResponseInfo<List<ExpressionTreeNodeVO>> getFullExpressionAndCategoryList(Boolean tree){
        return ResponseInfo.success(expressionService.getFullExpressionAndCategoryList(tree));
    }

    @PostMapping("/calculate")
    @ApiOperation("计算校验")
    public ResponseInfo<String> calculateExpression(@Validated @RequestBody ExpressionCalculateDTO dto){
        return ResponseInfo.success(expressionService.calculateExpression(dto));
    }

    @GetMapping("/recordTree")
    @ApiOperation("记录绑定树")
    public ResponseInfo<List<MesRecordTreeNodeVO>> getRecordBindTree(@NotNull @ApiParam(name = "id", value = "公式id", required = true) Long id) {
        return ResponseInfo.success(expressionService.getRecordBindTree(id));
    }

    @GetMapping("/boundRecordIdList")
    @ApiOperation("获取绑定的记录id列表")
    public ResponseInfo<List<Long>> getBoundRecordIdList(@NotNull Long id) {
        return ResponseInfo.success(expressionService.getBoundRecordIdList(id));
    }

    @PostMapping("/bindRecord")
    @ApiOperation("公式绑定记录")
    public ResponseInfo<Void> bindBatchRecord(@Validated @RequestBody ExpressionBindRecordDTO dto) {
        expressionService.bindBatchRecord(dto);
        return ResponseInfo.success();
    }
}
