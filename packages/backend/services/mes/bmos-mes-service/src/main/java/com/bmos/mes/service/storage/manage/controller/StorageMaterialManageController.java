package com.bmos.mes.service.storage.manage.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.storage.manage.dto.*;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialManageService;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialManageBatchVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialManageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/26 17:37
 */
@RestController
@RequestMapping("/storage/material/manage")
@Validated
@Api(tags = "物料管理")
public class StorageMaterialManageController {

    @Resource
    private IStorageMaterialManageService storageMaterialManageService;


    @GetMapping("/queryBatchPage")
    @ApiOperation("分页查询物料管理批次分页")
    public ResponseInfo<CommonPage<StorageMaterialManageBatchVO>> queryBatchPage(@Validated StorageMaterialBatchManagePageQuery pageQuery) {
        return ResponseInfo.success(storageMaterialManageService.queryBatchPage(pageQuery));
    }

    @GetMapping("/queryBatchDetail")
    @ApiOperation("查询物料批次详情")
    @ApiImplicitParam(name = "id", value = "物料批次id")
    public ResponseInfo<StorageMaterialManageBatchVO> queryBatchDetail(@RequestParam Long id) {
        return ResponseInfo.success(storageMaterialManageService.queryBatchDetail(id));
    }

    @GetMapping("/queryPage")
    @ApiOperation("分页查询物料管理物料件分页")
    public ResponseInfo<CommonPage<StorageMaterialManageVO>> queryPage(@Validated StorageMaterialManagePageQuery pageQuery) {
        return ResponseInfo.success(storageMaterialManageService.queryPage(pageQuery));
    }

    @PostMapping("/addBatch")
    @ApiOperation("新增物料批次")
    @OperationLog
    public ResponseInfo<Void> addBatch(@RequestBody @Validated StorageMaterialManageBatchCreateDTO dto) {
        storageMaterialManageService.addBatch(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/editBatch")
    @ApiOperation("编辑物料批次")
    @OperationLog
    public ResponseInfo<Void> editBatch(@RequestBody @Validated StorageMaterialManageEditBatchDTO dto) {
        storageMaterialManageService.editBatch(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/add")
    @ApiOperation("新增物料件")
    @OperationLog
    public ResponseInfo<Void> add(@RequestBody @Validated StorageMaterialManageCreateDTO dto) {
        storageMaterialManageService.add(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/saveMaterialComponentValue")
    @ApiOperation("保存物料件组件值")
    @OperationLog
    public ResponseInfo<Void> saveMaterialComponentValue(@RequestBody @Validated StorageMaterialComponentDTO dto) {
        storageMaterialManageService.saveMaterialComponentValue(dto);
        return ResponseInfo.success();
    }
}
