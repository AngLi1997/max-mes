package com.bmos.mes.service.requisition.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.requisition.dto.*;
import com.bmos.mes.service.requisition.service.RequisitionService;
import com.bmos.mes.service.requisition.vo.*;
import com.bmos.mes.service.storage.manage.dto.BatchReservedMaterialQueryDTO;
import com.bmos.mes.service.storage.manage.dto.StorageMaterialReserveBatchDTO;
import com.bmos.mes.service.storage.manage.service.IStorageMaterialService;
import com.bmos.mes.service.storage.manage.vo.BatchReservedAvailableMaterialVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/requisition")
@Api(tags = "领料相关接口")
public class RequisitionController {

    @Autowired
    private RequisitionService requisitionService;

    @Autowired
    private IStorageMaterialService storageMaterialService;

    @GetMapping("/detail")
    @ApiOperation("获取组件领料单详情")
    public ResponseInfo<RequisitionPlanVO> getMaterialRequisitionPlanVO(@Validated RequisitionQueryDTO dto) {
        return ResponseInfo.success(requisitionService.getMaterialRequisitionPlanVO(dto));
    }

    @PostMapping("/storage/reserve")
    @ApiOperation("暂存领料:暂存物料批量预定")
    @OperationLog
    public ResponseInfo<Void> reserveBatch(@Validated @RequestBody StorageMaterialReserveBatchDTO dto) {
        storageMaterialService.reserveBatch(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/storage/cancel")
    @ApiOperation("暂存领料:暂存物料单个取消预定")
    @OperationLog
    public ResponseInfo<Void> cancelReservedSingleStorage(@RequestBody @Validated StorageMaterialCancelReservedSingleDto dto) {
        requisitionService.cancelReservedSingle(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/reservedAvailableMaterial")
    @ApiOperation("仓库领料:获取可预订及已预订的暂存物料件")
    public ResponseInfo<List<BatchReservedAvailableMaterialVO>> getReservedAvailableStorageMaterial(@Validated BatchReservedMaterialQueryDTO dto) {
        return ResponseInfo.success(storageMaterialService.getReservedAvailableStorageMaterial(dto));
    }

    @GetMapping("/reservedMaterial")
    @ApiOperation("仓库领料:获取批次下已经预定的暂存物料件")
    public ResponseInfo<BatchReservedMaterialInfoVO> getBatchReservedMaterial(@Validated BatchReservedMaterialQueryDTO dto) {
        return ResponseInfo.success(requisitionService.getBatchReservedMaterialInfo(dto));
    }

    @GetMapping("/receive/repository/batchList")
    @ApiOperation("仓库领料:获取可选批次列表")
    public ResponseInfo<List<RepositoryMaterialBatchListVO>> getRepositoryMaterialBatch(RepositoryBatchQueryDTO dto) {
        return ResponseInfo.success(requisitionService.getRepositoryMaterialBatch(dto));
    }

    @GetMapping("/receive/repository/availableQuantityList")
    @ApiOperation("仓库领料:获取可选物料量列表")
    public ResponseInfo<List<InventoryAvailableQuantityListVO>> getRepositoryMaterialQuantityList(RepositoryQuantityQueryDTO dto) {
        return ResponseInfo.success(requisitionService.getRepositoryMaterialQuantityList(dto));
    }


    @PostMapping("/receive/repository/reserveBatch")
    @ApiOperation("仓库领料:预定批次")
    public ResponseInfo<Void> reserveRepositoryMaterial(@RequestBody @Validated ReserveRepositoryMaterialDTO dto) {
        requisitionService.reserveRepositoryMaterial(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/receive/repository/cancelReserved")
    @ApiOperation("仓库领料:完成前取消")
    public ResponseInfo<Void> cancelRepositoryMaterial(@RequestBody CancelRepositoryMaterialDTO dto) {
        requisitionService.cancelRepositoryMaterial(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/receive/repository/complete")
    @ApiOperation("仓库领料:完成领料")
    @OperationLog
    public ResponseInfo<Void> completeRequisitionPlan(@Validated @RequestBody RequisitionCompleteDTO dto) {
        requisitionService.completeRequisitionPlan(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/receive/repository/reservedBatch")
    @ApiOperation("仓库领料:获取已预订仓库物料批次和领料信息")
    public ResponseInfo<RepositoryReservedBatchVO> getRepositoryReservedBatch(@Validated RepositoryReservedBatchQueryDto dto) {
        return ResponseInfo.success(requisitionService.getRepositoryReservedBatch(dto));
    }

    @GetMapping("/list")
    @ApiOperation("领料接收:获取领料单列表")
    public ResponseInfo<List<RequisitionListVO>> getRequisitionList(@ApiParam(name = "batchId", value = "批次id", required = true) @NotNull Long batchId) {
        return ResponseInfo.success(requisitionService.getRequisitionList(batchId));
    }

    @GetMapping("/receive/repository/materialBatch")
    @ApiOperation("领料接收:获取领料单下物料批次信息")
    public ResponseInfo<List<RequisitionMaterialBatchVO>> getRequisitionMaterialBatchList(@NotNull @ApiParam(name = "requisitionPlanId", value = "领料单id", required = true)
                                                                                          Long requisitionPlanId) {
        return ResponseInfo.success(requisitionService.getRequisitionMaterialBatchList(requisitionPlanId));
    }

    @GetMapping("/receive/material/list")
    @ApiOperation("领料接收:物料件列表")
    public ResponseInfo<List<RepositoryBatchMaterialListVO>> getRepositoryBatchMaterialList(@Validated RepositoryBatchMaterialQueryDTO dto) {
        return ResponseInfo.success(requisitionService.getRepositoryBatchMaterialList(dto));
    }

    @PostMapping("/receive/repository/material")
    @ApiOperation("领料接收:按物料件")
    @OperationLog
    public ResponseInfo<Void> receiveRepositoryMaterialByMaterial(@Validated @RequestBody ReceiveRepositoryByMaterialDTO dto) {
        requisitionService.receiveRepositoryByMaterial(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/receive/boundRequisition")
    @ApiOperation("领料接收:获取组件绑定的领料单信息")
    public ResponseInfo<ComponentBoundRequisitionVO> getComponentBoundRequisition(@Validated ComponentBoundRequisitionQueryDTO dto){
        return ResponseInfo.success(requisitionService.getComponentBoundRequisition(dto));
    }

    @PostMapping("/receive/repository/batch")
    @ApiOperation("领料接收:按批次")
    @OperationLog
    public ResponseInfo<Void> receiveRepositoryMaterialByBatch(@Validated @RequestBody ReceiveRepositoryByBatchDTO dto) {
        requisitionService.receiveRepositoryByBatch(dto);
        return ResponseInfo.success();
    }


    @PostMapping("/receive/complete")
    @ApiOperation("领料接收:完成接收")
    @OperationLog
    public ResponseInfo<Void> completeReceive(@Validated @RequestBody ReceiveRequisitionCompleteDTO dto){
        requisitionService.completeReceive(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/receive/sendOut")
    @ApiModelProperty("仓库回调:发料")
    public ResponseInfo<Void> sendOut(@RequestBody @Validated SendOutFeignDTO dto) {
        requisitionService.sendOut(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/receive/cancelSendOut")
    @ApiOperation("仓库回调:取消发料")
    @ApiImplicitParam(name = "requisitionPlanId", value = "领料计划id", required = true)
    public ResponseInfo<Void> cancelSendOut(@RequestParam("requisitionPlanId") Long requisitionPlanId) {
        requisitionService.cancelSendOut(requisitionPlanId);
        return ResponseInfo.success();
    }

    @GetMapping("/quantity/calculate")
    @ApiOperation("计算理论量")
    public ResponseInfo<BigDecimal> calculate(@Validated QuantityCalculateDTO dto){
        return ResponseInfo.success(requisitionService.calculateQuantity(dto));
    }

    @GetMapping("/reserve/instance")
    @ApiOperation("物料预定组件:获取实例相关信息")
    public ResponseInfo<ReserveComponentInstanceVO> getReserveComponentInstanceInfo(@Validated ReserveComponentInstanceQueryDTO dto){
        return ResponseInfo.success(requisitionService.getReserveComponentInstanceInfo(dto));
    }

    @GetMapping("/reserve/availableList")
    @ApiOperation("物料预定组件:获取可预定物料件")
    public ResponseInfo<List<BatchAvailableMaterialVO>> getAvailableStorageMaterial(@Validated AvailableStorageMaterialQueryDTO dto){
        return ResponseInfo.success(storageMaterialService.getAvailableStorageMaterial(dto));
    }

    @PostMapping("/reserve")
    @ApiOperation("物料预定组件:物料批量预定")
    @OperationLog
    public ResponseInfo<Void> reserveStorageMaterial(@RequestBody @Validated ReserveComponentReserveDTO dto){
        requisitionService.reserveStorageMaterial(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/reserve/cancel")
    @ApiOperation("物料预定组件:取消预定")
    @OperationLog
    public ResponseInfo<Void> reserveComponentCancelReserve(@RequestBody @Validated ReserveComponentCancelReserveDTO dto){
        requisitionService.reserveComponentCancelReserve(dto);
        return ResponseInfo.success();
    }


}
