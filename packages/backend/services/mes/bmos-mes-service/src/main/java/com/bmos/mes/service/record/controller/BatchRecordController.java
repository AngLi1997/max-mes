package com.bmos.mes.service.record.controller;


import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.expression.model.RoundingVO;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;
import com.bmos.mes.service.platform.expression.vo.ExpressionTreeNodeVO;
import com.bmos.mes.service.process.dto.ProcessRecordQueryDTO;
import com.bmos.mes.service.record.dto.*;
import com.bmos.mes.service.record.service.*;
import com.bmos.mes.service.record.vo.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/record")
@Api(tags = "批记录配置接口")
@Validated
@Slf4j
public class BatchRecordController {

    @Autowired
    private BatchRecordService batchRecordService;

    @Autowired
    private BatchRecordCategoryService categoryService;

    @Autowired
    private BatchRecordProductService productService;

    @Autowired
    private BatchRecordVersionService versionService;

    @Autowired
    private BatchRecordItemService itemService;

    @Autowired
    private BatchRecordComponentService componentService;

    @ApiOperation(value = "添加记录配置分类")
    @PostMapping("/save/category")
    public ResponseInfo<Boolean> saveCategory(@Validated @RequestBody CategorySaveDTO dto) {
        return ResponseInfo.success(categoryService.saveCategory(dto));
    }

    @PostMapping("/update/category")
    @ApiOperation(value = "编辑分类")
    @OperationLog
    public ResponseInfo<Boolean> updateCategory(@Validated @RequestBody CategoryUpdateDTO dto) {
        return ResponseInfo.success(categoryService.updateCategory(dto));
    }

    @GetMapping("/delete/category")
    @ApiOperation(value = "删除分类")
    @ApiParam(name = "id", value = "分类id", required = true)
    @OperationLog
    public ResponseInfo<Boolean> deleteCategory(@NotBlank String id) {
        return ResponseInfo.success(categoryService.deleteCategory(id));
    }

    @GetMapping("/list/category")
    @ApiOperation(value = "查询分类信息")
    @ApiParam(name = "categoryName", value = "分类名称")
    public ResponseInfo<List<CategoryListVO>> listCategory() {
        return ResponseInfo.success(categoryService.listCategory());
    }

    @PostMapping("/save/record")
    @ApiOperation(value = "新增记录接口")
    public ResponseInfo<BatchRecordSaveVO> saveRecord(@Validated @RequestBody BatchRecordSaveDTO dto) {
        return ResponseInfo.success(batchRecordService.saveRecord(dto));
    }

    @PostMapping("/fileUpload")
    @ApiOperation(value = "文件上传")
    @ApiParam(name = "file", value = "文件流", required = true)
    public ResponseInfo<RecordUploadVo> fileUpload(@RequestParam("file") MultipartFile file) {
        RecordUploadVo recordUploadVo = batchRecordService.fileUpload(file);
        return ResponseInfo.success(recordUploadVo);
    }

    @GetMapping("/list/record")
    @ApiOperation(value = "查询批记录列表数据")
    public ResponseInfo<CommonPage<RecordListVO>> getRecordPage(@Validated RecordListQueryDTO dto) {
        return ResponseInfo.success(batchRecordService.getRecordPage(dto));
    }

    @PostMapping("/save/product")
    @ApiOperation(value = "绑定产品")
    public ResponseInfo<Boolean> saveProduct(@Validated @RequestBody ProductSaveDTO dto) {
        return ResponseInfo.success(productService.saveProduct(dto));
    }

    @GetMapping("/query/product/id")
    @ApiOperation(value = "根据记录id查询产品id")
    @ApiParam(name = "recordId", value = "记录id", required = true)
    public ResponseInfo<List<Long>> queryProductIdByRecordId(@Validated @NotNull Long recordId) {
        return ResponseInfo.success(productService.queryProductIdByRecordId(recordId));
    }

    @PostMapping("/copy/version")
    @ApiOperation(value = "复制已有版本")
    public ResponseInfo<Long> copyVersion(@Validated @RequestBody CopyVersionDTO dto) {
        return ResponseInfo.success(versionService.copyVersion(dto));
    }

    @PostMapping("/update/version")
    @ApiOperation(value = "更新批记录版本")
    public ResponseInfo<Boolean> updateVersion(@Validated @RequestBody RecordVersionDTO dto) {
        return ResponseInfo.success(batchRecordService.updateVersion(dto));
    }

    @PostMapping("/record/item/upload")
    @ApiOperation(value = "记录项上传")
    public ResponseInfo<RecordUploadItemVO> recordItemUpload(@RequestParam("file") MultipartFile file) {
        RecordUploadItemVO recordUploadItemVO = batchRecordService.recordItemUpload(file);
        return ResponseInfo.success(recordUploadItemVO);
    }

    @GetMapping("/copy/record/item")
    @ApiOperation(value = "复制记录项")
    @ApiParam(name = "itemId", value = "记录项id", required = true)
    @Deprecated
    public ResponseInfo<RecordItemDetailVO> copyRecordItem(@NotNull Long itemId, @NotNull String itemName) {
        return ResponseInfo.success(batchRecordService.copyRecordItem(itemId, itemName));
    }

    @GetMapping("/delete/record/item")
    @ApiOperation(value = "删除记录项")
    @ApiParam(name = "itemId", value = "记录项id", required = true)
    public ResponseInfo<RecordItemDetailVO> deleteRecordItem(@NotNull Long itemId) {
        return ResponseInfo.success(batchRecordService.deleteRecordItem(itemId));
    }

    @GetMapping("/production/id")
    @ApiOperation(value = "批量生成id")
    public ResponseInfo<List<Long>> productionId() {
        return ResponseInfo.success(itemService.productionId());
    }

    @GetMapping("/list/version")
    @ApiOperation(value = "根据记录id查询版本号")
    public ResponseInfo<List<RecordVersionVO>> listVersion(@NotNull Long recordId) {
        return ResponseInfo.success(versionService.listVersion(recordId));
    }

    @GetMapping("/list/product/record")
    @ApiOperation(value = "根据产品id查询批记录")
    @ApiParam(name = "productId", value = "产品id", required = true)
    public ResponseInfo<List<RecordVersionVO>> listProductRecord(@NotNull Long productId, Long recordId) {
        return ResponseInfo.success(versionService.listPorductRecord(productId, recordId));
    }

    @PostMapping(value = "/list/record/item")
    @ApiOperation(value = "根据多个版本id查询记录项")
    @ApiParam(name = "versionId", value = "批记录版本id集合", required = true)
    public ResponseInfo<List<ProcessRecordItemVO>> listRecordItem(@NotNull @RequestBody List<Long> versionIdList) {
        return ResponseInfo.success(itemService.listRecordItem(versionIdList));
    }

    @GetMapping(value = "/list/component")
    @ApiOperation(value = "根据记录项id查询组件")
    @ApiParam(name = "itemId", value = "记录项id", required = true)
    public ResponseInfo<ParseComponentVO> listComponent(@NotNull Long itemId, @NotNull Long recordVersionId) {
        return ResponseInfo.success(componentService.listComponent(itemId, recordVersionId));
    }

    @PostMapping(value = "/save/formula")
    @ApiOperation(value = "添加公式配置")
    @OperationLog
    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.FORMULA_CONFIG, businessId
            = "#dto.recordVersionId")
    public ResponseInfo<Boolean> saveFormula(@Validated @RequestBody SaveFormulaDTO dto) {
        return ResponseInfo.success(componentService.saveFormula(dto));
    }

    @GetMapping(value = "/delete/formula")
    @ApiOperation(value = "清除公式")
    @ApiParam(name = "componentId", value = "组件表id", required = true)
    public ResponseInfo<Boolean> deleteFormula(@NotNull Long componentId) {
        return ResponseInfo.success(componentService.deleteFormula(componentId));
    }

    @GetMapping(value = "/list/record/log")
    @ApiOperation(value = "根据版本号查询历史记录")
    @ApiParam(name = "versionId", value = "版本表id", required = true)
    public ResponseInfo<List<VersionLogVO>> listRecordLog(@NotNull Long versionId) {
        return ResponseInfo.success(versionService.listRecordLog(versionId));
    }

    @GetMapping(value = "/list/record/tree")
    @ApiOperation(value = "查询产品信息绑定批记录树结构")
    public ResponseInfo<List<ProductRecordTreeVO>> listRecordTree() {
        return ResponseInfo.success(categoryService.listRecordTree());
    }

    @GetMapping(value = "/list/rounding")
    @ApiOperation(value = "查询修约规则下拉框")
    public ResponseInfo<List<RoundingVO>> listRounding() {
        return ResponseInfo.success(Arrays.stream(RoundingEnum.values()).map(e -> {
            RoundingVO vo = new RoundingVO();
            vo.setValue(e.getValue());
            vo.setLabel(I18nUtils.getEnumMessage(e));
            return vo;
        }).collect(Collectors.toList()));
    }

    @GetMapping("/checkout/save/record")
    @ApiOperation(value = "校验是否可新增版本")
    @ApiParam(name = "recordId", value = "记录id", required = true)
    public ResponseInfo<Boolean> checkoutSaveRecord(@Validated @NotNull Long recordId) {
        return ResponseInfo.success(versionService.checkoutSaveRecord(recordId));
    }

    @GetMapping("/query/record/item")
    @ApiOperation(value = "根据itemId以及versionId查询记录项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "记录项业务id", value = "recordItemId", required = true),
            @ApiImplicitParam(name = "记录版本id", value = "recordVersionId", required = true),
    })
    public ResponseInfo<RecordItemVO> queryRecordItemByItemIdAndVersionId(@NotNull Long recordItemId, @NotNull Long recordVersionId) {
        return ResponseInfo.success(itemService.queryRecordItemByItemIdAndVersionId(recordItemId, recordVersionId));
    }

    @GetMapping("/query/list/record")
    @ApiOperation(value = "根据产品id查询批记录")
    @ApiParam(name = "productId", value = "产品id", required = true)
    public ResponseInfo<List<SelectRecorVO>> queryListRecordByProductId(@Validated @NotNull Long productId) {
        return ResponseInfo.success(batchRecordService.queryListRecordByProductId(productId));
    }

    @GetMapping("/query/record/version")
    @ApiOperation(value = "根据记录id查询版本号")
    @ApiParam(name = "recordId", value = "记录id", required = true)
    public ResponseInfo<List<SelectRecorVO>> queryRecordVersionByRecordId(@Validated @NotNull Long recordId) {
        return ResponseInfo.success(versionService.queryRecordVersionByRecordId(recordId));
    }

    @GetMapping("/query/process/record/version")
    @ApiOperation(value = "根据记录id以及工艺版本id查询版本号")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "记录id",name = "recordId",required = true),
            @ApiImplicitParam(value = "工艺版本id",name = "processVersionId"),
    })
    public ResponseInfo<List<SelectRecorVO>> queryProcessRecordVersionByRecordId(@Validated @NotNull Long recordId,Long processVersionId) {
        return ResponseInfo.success(versionService.queryProcessRecordVersionByRecordId(recordId,processVersionId));
    }

    @GetMapping("/query/process/record/version/list")
    @ApiOperation("根据记录id列表以及工艺id查询版本列表")
    public ResponseInfo<List<ProcessRecordListVO>> queryProcessRecordVersionByRecordIdList(@Validated ProcessRecordVersionQueryDTO dto) {
        return ResponseInfo.success(versionService.queryProcessRecordVersionByRecordIdList(dto));
    }

    @GetMapping("/functionTree")
    @ApiOperation("获取平台公式配置及内置公式")
    public ResponseInfo<List<ExpressionTreeNodeVO>> queryPlatformExpressionAndBuiltInFunction(@Validated ExpressionQueryDTO dto) {
        return ResponseInfo.success(versionService.queryPlatformExpressionAndBuiltInFunction(dto));
    }

    @PostMapping("/function/preview")
    @ApiOperation("计算预览")
    public ResponseInfo<String> getFunctionCalculatePreview(@RequestBody @Validated FunctionCalculatePreviewDTO dto) {
        return ResponseInfo.success(versionService.getFunctionCalculatePreview(dto));
    }

    @PostMapping("/handle/item")
    @ApiOperation("拆分记录项大字段处理")
    public ResponseInfo<Void> handelItem() {
        itemService.handelItem();
        return ResponseInfo.success();
    }

    @PostMapping("/item/changeName")
    @ApiOperation("记录项名称修改")
    public ResponseInfo<Void> changeItemName(@RequestBody @Validated ItemNameChangeDTO dto) {
        itemService.changeItemName(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/item/singleSave")
    @ApiOperation("单个记录项新增")
    @OperationLog
    public ResponseInfo<SaveSingleItemVO> saveSingleItem(@RequestBody @Validated RecordItemSingleSaveDTO dto) {
        return ResponseInfo.success(batchRecordService.saveSingleItem(dto));
    }

    @PostMapping("/item/singleEdit")
    @ApiOperation("记录项编辑和组件保存")
    @OperationLog
    public ResponseInfo<Void> editSingleItem(@RequestBody @Validated RecordItemSingleEditDTO dto) {
        batchRecordService.editSingleItem(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/item/detail")
    @ApiOperation("记录信息及记录项列表")
    public ResponseInfo<RecordInfoItemListVO> getRecordInfoAndItemList(@NotNull Long recordVersionId) {
        return ResponseInfo.success(versionService.getRecordInfoAndItemList(recordVersionId));
    }

    @PostMapping("/item/changeSort")
    @ApiOperation("修改记录项顺序")
    public ResponseInfo<Void> changeRecordItemSort(@RequestBody @Validated RecordItemSortUpdateDTO dto) {
        versionService.changeRecordItemSort(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/item/edit")
    @ApiOperation("记录编辑历史")
    @OperationHistory(module = BusinessModule.BATCH_RECORD, operationType = OperationType.REDACT, businessId = "#recordVersionId")
    public ResponseInfo<Void> saveRecordEditHistory(@RequestParam @NotNull Long recordVersionId) {
        return ResponseInfo.success();
    }

    @GetMapping("/expressionBindTree")
    @ApiOperation("获取公式绑定树")
    public ResponseInfo<List<RecordExpressionBindTreeNodeVO>> getExpressionBindTree(@NotNull @ApiParam(required = true, name = "id", value = "记录id") Long id) {
        return ResponseInfo.success(batchRecordService.getExpressionTreeByRecordId(id));
    }

    @GetMapping("/boundExpressionIdList")
    @ApiOperation("根据记录id获取绑定的公式id")
    public ResponseInfo<List<Long>> getRecordBoundExpressionIdList(@NotNull Long id) {
        return ResponseInfo.success(batchRecordService.getRecordBoundExpressionIdList(id));
    }

    @PostMapping("/bindExpression")
    @ApiOperation("记录绑定公式")
    public ResponseInfo<Void> bindExpression(@RequestBody @Validated RecordBindExpressionDTO dto) {
        batchRecordService.bindExpression(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/downloadByUrl")
    @ApiOperation("根据地址下载记录文件")
    public ResponseInfo<Void> downloadByUrl(HttpServletResponse response, @RequestParam String url) throws Exception {
        batchRecordService.downloadByUrl(response, url);
        return ResponseInfo.success();
    }

}
