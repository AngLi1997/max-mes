package com.bmos.wms.service.inventory.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.inventory.dto.*;
import com.bmos.wms.service.inventory.service.IInventoryService;
import com.bmos.wms.service.inventory.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 库存管理
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/28 14:07
 */
@RestController
@RequestMapping("/inventory")
@Api(tags = "仓库管理相关接口")
@Validated
public class InventoryController {

    @Resource
    private IInventoryService inventoryService;

    @GetMapping("/batchPage")
    @ApiOperation("仓库库存 - 分页查询货品批次")
    public ResponseInfo<CommonPage<InventoryBatchVO>> queryBatchPage(@Validated InventoryBatchPageQuery pageQuery) {
        return ResponseInfo.success(inventoryService.queryBatchPage(pageQuery));
    }

    @GetMapping("/inventoryPageByBatchId")
    @ApiOperation("仓库库存 - 分页查询货品（根据货品批次）")
    public ResponseInfo<CommonPage<InventoryVO>> queryPageByBatchId(@Validated InventoryPageQueryWithBatchId pageQuery) {
        return ResponseInfo.success(inventoryService.queryPageByBatchId(pageQuery));
    }

    @PostMapping("/inbound")
    @ApiOperation("仓库库存 - 货品入库")
    @OperationLog
    public ResponseInfo<List<InventoryVO>> inbound(@Validated @RequestBody InventoryInboundDTO dto) {
        return ResponseInfo.success(inventoryService.inbound(dto));
    }

    @PutMapping("/outbound")
    @ApiOperation("仓库库存 - 货品出库")
    @OperationLog
    public ResponseInfo<Void> outbound(@Validated @RequestBody InventoryOutboundDTO dto) {
        inventoryService.outbound(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/move")
    @ApiOperation("仓库库存 - 货品移库")
    @OperationLog
    public ResponseInfo<Void> move(@Validated @RequestBody InventoryMoveDTO dto) {
        inventoryService.move(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/check")
    @ApiOperation("仓库库存 - 盘点")
    @OperationLog
    public ResponseInfo<Void> check(@Validated @RequestBody InventoryCheckDTO dto) {
        inventoryService.check(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/inventoryPageByCargoId")
    @ApiOperation("仓库货品 - 分页查询货品")
    public ResponseInfo<CommonPage<CargoInventoryVO>> queryPageByCargoIds(@Validated InventoryPageQueryWithCargoId pageQuery) {
        return ResponseInfo.success(inventoryService.queryPageByCargoIds(pageQuery));
    }

    @GetMapping("/batchPageByCargoIds")
    @ApiOperation("仓库货品 - 分页查询货品批次（包含可用量为0的）")
    public ResponseInfo<CommonPage<CargoInventoryBatchVO>> queryBatchPageByCargoIds(@Validated InventoryBatchPageQueryWithCargoId pageQuery) {
        return ResponseInfo.success(inventoryService.queryBatchPageByCargoIds(pageQuery));
    }

    @GetMapping("/queryInventoryBatchById")
    @ApiOperation("仓库货品 - 根据批次id查询批次详情")
    @ApiImplicitParam(name = "inventoryBatchId", value = "批次id", required = true)
    public ResponseInfo<CargoInventoryBatchDetailVO> queryInventoryBatchById(@RequestParam Long inventoryBatchId) {
        return ResponseInfo.success(inventoryService.queryInventoryBatchById(inventoryBatchId));
    }

    @GetMapping("/queryInventoryById")
    @ApiOperation("仓库货品 - 根据货品件id查询货品件详情")
    @ApiImplicitParam(name = "inventoryId", value = "货品件id", required = true)
    public ResponseInfo<InventoryVO> queryInventoryById(@RequestParam Long inventoryId) {
        return ResponseInfo.success(inventoryService.queryInventoryById(inventoryId));
    }

    @PostMapping("/addInventoryBatch")
    @ApiOperation("仓库货品 - 新增批次")
    @OperationLog
    public ResponseInfo<Void> addInventoryBatch(@Validated @RequestBody InventoryBatchCreateDTO dto) {
        inventoryService.addInventoryBatch(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/editInventoryBatch")
    @ApiOperation("仓库货品 - 编辑批次")
    @OperationLog
    public ResponseInfo<Void> editInventoryBatch(@Validated @RequestBody InventoryBatchEditDTO dto) {
        inventoryService.editInventoryBatch(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/addInventory")
    @ApiOperation("仓库货品 - 新增货品件")
    @OperationLog
    public ResponseInfo<Void> addInventory(@Validated @RequestBody InventoryCreateDTO dto) {
        inventoryService.addInventory(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/listByCargoIdAndBatchNo")
    @ApiOperation("根据物料id查询货品批次列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cargoId", value = "货品id", required = true, example = "1"),
            @ApiImplicitParam(name = "inventoryBatchNo", value = "货品批次号", example = "1")
    })
    public ResponseInfo<List<CargoInventoryBatchItemVO>> listByCargoIdAndBatchNo(@NotNull Long cargoId, String inventoryBatchNo) {
        return ResponseInfo.success(inventoryService.listByCargoIdAndBatchNo(cargoId, inventoryBatchNo));
    }

    @GetMapping("/listByBatchIdAndPositionId")
    @ApiOperation("根据批次id和货位id查询货品件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "inventoryBatchId", value = "货品批次id", required = true, example = "1"),
            @ApiImplicitParam(name = "positionId", value = "货品货位id", example = "1")
    })
    public ResponseInfo<List<InventoryVO>> listByBatchIdAndPositionId(@RequestParam Long inventoryBatchId,
                                                                      @RequestParam Long positionId) {
        return ResponseInfo.success(inventoryService.listByBatchIdAndPositionId(inventoryBatchId, positionId));
    }

    @GetMapping("/listByCargoIdAndBatchId")
    @ApiOperation("根据物料id和批次id查询货品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cargoId", value = "货品id", required = true, example = "1"),
            @ApiImplicitParam(name = "inventoryBatchId", value = "货品批次id", example = "1")
    })
    public ResponseInfo<List<CargoInventoryItemVO>> listByCargoIdAndBatchId(@NotNull Long cargoId, Long inventoryBatchId) {
        return ResponseInfo.success(inventoryService.listByCargoIdAndBatchId(cargoId, inventoryBatchId));
    }

    @PostMapping("/batchList")
    @ApiOperation("仓库货品 - 查询物料可用批次")
    public ResponseInfo<List<InventoryBatchListVO>> queryBatchByMaterial(@RequestBody InventoryBatchQueryDTO dto){
        return ResponseInfo.success(inventoryService.queryBatchByMaterial(dto));
    }

    @PostMapping("/availableQuantityList")
    @ApiOperation("仓库货品 - 查询物料可用量")
    public ResponseInfo<List<InventoryAvailableQuantityListVO>> queryAvailableQuantityList(@RequestBody InventoryAvailableQuantityQueryDTO dto){
        return ResponseInfo.success(inventoryService.queryAvailableQuantityList(dto));
    }

    @PostMapping("/printTag")
    @ApiOperation("物料件标签打印 - 根据货品件id获取标签数据")
    public ResponseInfo<InventoryVO> printTag(@Validated @RequestBody InventoryPrintTagDTO dto) {
        return ResponseInfo.success(inventoryService.queryInventoryById(dto.getId()));
    }

}
