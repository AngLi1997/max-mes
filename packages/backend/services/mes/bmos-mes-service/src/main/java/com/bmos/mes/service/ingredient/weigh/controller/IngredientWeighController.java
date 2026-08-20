package com.bmos.mes.service.ingredient.weigh.controller;

import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.ingredient.weigh.dto.*;
import com.bmos.mes.service.ingredient.weigh.service.IIngredientWeighService;
import com.bmos.mes.service.ingredient.weigh.service.WeighLogService;
import com.bmos.mes.service.ingredient.weigh.vo.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 配料称量相关接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/17 17:03
 */
@RestController
@RequestMapping("/ingredient/weigh")
@Api(tags = "配料称量")
public class IngredientWeighController {

    @Resource
    private IIngredientWeighService iIngredientWeighService;

    @Resource
    private WeighLogService weighLogService;

    @PostMapping("/getIngredientWeighProcess")
    @ApiOperation("查询配料称量信息")
    public ResponseInfo<IngredientWeighProcessVO> getIngredientWeighProcess(@Validated @RequestBody InputWeighProcessQuery inputWeighProcessQuery) {
        return ResponseInfo.success(iIngredientWeighService.getIngredientWeighProcess(inputWeighProcessQuery));
    }

    @GetMapping("/queryPendingIngredientPlanList")
    @ApiOperation("获取未完成的配料单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productPlanId", value = "生产计划id", required = true, example = "1"),
            @ApiImplicitParam(name = "batchNo", value = "生产批号", required = true, example = "1")
    })
    public ResponseInfo<List<IngredientPlanItemVO>> queryPendingIngredientPlanList(@RequestParam Long productPlanId, @RequestParam String batchNo) {
        return ResponseInfo.success(iIngredientWeighService.queryPendingIngredientPlanList(productPlanId, batchNo));
    }

    @PutMapping("/makeSureWeigh")
    @ApiOperation("确认称量")
    @OperationLog
    public ResponseInfo<Void> makeSureWeigh(@Validated @RequestBody IngredientMakeSureWeighDTO ingredientMakeSureWeighDTO) {
        iIngredientWeighService.makeSureWeigh(ingredientMakeSureWeighDTO);
        return ResponseInfo.success();
    }

    @PostMapping("/getBalanceListByStationId")
    @ApiOperation("根据工位id获取秤具列表")
    @ApiImplicitParam(name = "stationId", value = "工位id")
    public ResponseInfo<List<WeighBalanceEquipment>> getBalanceListByStationId(@RequestBody List<Long> stationIds) {
        return ResponseInfo.success(iIngredientWeighService.getBalanceListByStationIds(stationIds));
    }

    @GetMapping("/queryIngredientPlanById")
    @ApiOperation("根据id查询配料单详情")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "id", value = "配料单id", required = true),
    @ApiImplicitParam(name = "componentId", value = "组件id", required = true),
    @ApiImplicitParam(name = "procedureStepModelId", value = "工序步骤模型id", required = true)
    })
    public ResponseInfo<IngredientPlanDetailVO> queryIngredientPlanById(@RequestParam Long id, @RequestParam Long componentId, @RequestParam Long procedureStepModelId) {
        return ResponseInfo.success(iIngredientWeighService.queryIngredientPlanById(id, componentId, procedureStepModelId));
    }

    @PostMapping("/addConsumeStorageMaterial")
    @ApiOperation("添加称量消耗物料件")
    @OperationLog
    public ResponseInfo<Void> addConsumeStorageMaterial(@Validated @RequestBody IngredientWeighConsumeStorageMaterialDTO dto) {
        iIngredientWeighService.addConsumeStorageMaterial(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/queryWeighDetailByPlanIdAndBatchId")
    @ApiOperation("根据批次id查询称量批次详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "planId", value = "配料计划id", required = true),
            @ApiImplicitParam(name = "batchId", value = "批次id", required = true),
    })
    public ResponseInfo<WeighStorageMaterialBatchVO> queryWeighDetailByPlanIdAndBatchId(@RequestParam Long planId, @RequestParam Long batchId) {
        return ResponseInfo.success(iIngredientWeighService.queryWeighDetailByPlanIdAndBatchId(planId, batchId));
    }

    @PostMapping("/queryResult")
    @ApiOperation("根据配料计划id查询称量结果列表")
    public ResponseInfo<IngredientWeighStorageMaterialListVO> queryResult(@RequestBody IngredientWeighResultQuery query) {
        return ResponseInfo.success(iIngredientWeighService.queryResult(query));
    }

    @PostMapping("/weighAndPrint")
    @ApiOperation("称量打码")
    @OperationLog
    @DistributedLock(expression = "#ingredientWeighAndPrintDTO.ingredientPlanId")
    public ResponseInfo<WeighResult> weighAndPrint(@Validated @RequestBody IngredientWeighAndPrintDTO ingredientWeighAndPrintDTO) {
        return ResponseInfo.success(iIngredientWeighService.weighAndPrint(ingredientWeighAndPrintDTO));
    }

    @PostMapping("/finish")
    @ApiOperation("完成称量")
    @OperationLog
    public ResponseInfo<Void> finish(@Validated @RequestBody IngredientWeighFinishDTO weighFinishDTO) {
        iIngredientWeighService.finish(weighFinishDTO);
        return ResponseInfo.success();
    }

    @PutMapping("/changeWeigher")
    @ApiOperation("更换称量人员")
    @OperationLog
    public ResponseInfo<Long> changeWeigher(@Validated @RequestBody IngredientChangeWeigherDTO dto) {
        iIngredientWeighService.changeWeigher(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/sign")
    @ApiOperation("物料签名")
    @OperationLog
    public ResponseInfo<Void> sign(@RequestBody @Validated IngredientWeighSignDTO ingredientWeighSignDTO) {
        iIngredientWeighService.sign(ingredientWeighSignDTO);
        return ResponseInfo.success();
    }

    @GetMapping("/log/page")
    @ApiOperation("称量日志-分页")
    public ResponseInfo<CommonPage<WeighLogPageVO>> queryWeighLogPage(@Validated WeighLogQueryDTO dto) {
        return ResponseInfo.success(weighLogService.queryWeighLogPage(dto));
    }

    @PostMapping("/validateComponentSign")
    @ApiOperation("校验配料称量组件物料件签名")
    @OperationLog
    public ResponseInfo<Boolean> validateComponentSign(@RequestBody @Validated List<IngredientWeighValidateSignDTO> validateSignList) {
        return ResponseInfo.success(iIngredientWeighService.validateComponentSign(validateSignList));
    }
}
