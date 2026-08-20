package com.bmos.mes.service.storage.manage.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialBatchPageQuery;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialBatchService;
import com.bmos.mes.service.storage.manage.vo.MaterialBatchListVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialBatchDetailVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialBatchVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 暂存间管理 - 物料批次
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/6 11:58
 */
@RestController
@RequestMapping("/storage/material/batch")
@Api(tags = "暂存间管理 - 物料批次")
@Validated
public class StorageMaterialBatchController {

    @Resource
    private IStorageMaterialBatchService storageMaterialBatchService;

    @GetMapping("/page")
    @ApiOperation("分页查询暂存物料批次")
    public ResponseInfo<CommonPage<StorageMaterialBatchVO>> queryPage(@Validated StorageMaterialBatchPageQuery pageQuery) {
        return ResponseInfo.success(storageMaterialBatchService.queryPage(pageQuery));
    }

    @GetMapping("/listByMaterialId")
    @ApiOperation("根据物料id查询物料批次列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "materialId", value = "物料id", required = true, example = "1"),
            @ApiImplicitParam(name = "batchNo", value = "物料批次号", example = "1")
    })
    public ResponseInfo<List<MaterialBatchListVO>> queryMaterialBatchListByMaterialId(@NotNull Long materialId, String batchNo) {
        return ResponseInfo.success(storageMaterialBatchService.queryMaterialBatchListByMaterialId(materialId, batchNo));
    }

    @GetMapping("/batchDetail")
    @ApiOperation("根据物料批次id查询详情")
    public ResponseInfo<StorageMaterialBatchDetailVO> queryMaterialBatchDetail(@NotNull Long materialBatchId) {
        return ResponseInfo.success(storageMaterialBatchService.queryMaterialBatchDetail(materialBatchId));
    }
}
