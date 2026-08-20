package com.bmos.mes.service.output.weigh.controller;

import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.ingredient.weigh.vo.WeighBalanceEquipment;
import com.bmos.mes.service.ingredient.weigh.vo.WeighResult;
import com.bmos.mes.service.output.weigh.dto.*;
import com.bmos.mes.service.output.weigh.service.IOutputWeighService;
import com.bmos.mes.service.output.weigh.vo.OutputMaterialItem;
import com.bmos.mes.service.output.weigh.vo.OutputWeighProcessVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialSimpleBatchVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产出称量
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/28 09:24
 */
@RestController
    @RequestMapping("/output/weigh")
@Api(tags = "产出称量")
public class OutputWeighController {

    @Resource
    private IOutputWeighService outputWeighService;

    @GetMapping("/getMiddleMaterialList")
    @ApiOperation("获取产出批次中的中间品物料列表")
    @ApiImplicitParam(name = "outputWeighProcessId", value = "产出称量流程id")
    public ResponseInfo<List<OutputMaterialItem>> getMiddleMaterialList(@RequestParam Long outputWeighProcessId) {
        return ResponseInfo.success(outputWeighService.getMiddleMaterialList(outputWeighProcessId));
    }

    @GetMapping("/getUnionOriginMaterialList")
    @ApiOperation("获取关联批次中的原辅包物料列表")
    @ApiImplicitParam(name = "outputWeighProcessId", value = "产出称量流程id")
    public ResponseInfo<List<OutputMaterialItem>> getUnionOriginMaterialList(@RequestParam Long outputWeighProcessId) {
        return ResponseInfo.success(outputWeighService.getUnionOriginMaterialList(outputWeighProcessId));
    }

    @GetMapping("/queryBatchInfo")
    @ApiOperation("根据物料id和批次编号查询批次信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "materialId", value = "物料id"),
            @ApiImplicitParam(name = "batchNo", value = "批次编号")
    })
    public ResponseInfo<StorageMaterialSimpleBatchVO> queryBatchInfo(@RequestParam Long materialId, @RequestParam String batchNo) {
        return ResponseInfo.success(outputWeighService.queryBatchInfo(materialId, batchNo));
    }

    @PostMapping("/getBalanceListByStationId")
    @ApiOperation("根据工位id获取秤具列表")
    @ApiImplicitParam(name = "stationId", value = "工位id")
    public ResponseInfo<List<WeighBalanceEquipment>> getBalanceListByStationId(@RequestBody List<Long> stationIds) {
        return ResponseInfo.success(outputWeighService.getBalanceListByStationIds(stationIds));
    }

    @PostMapping("/getOutputWeighProcess")
    @ApiOperation("查询产出称量信息")
    public ResponseInfo<OutputWeighProcessVO> getOutputWeighProcess(@Validated @RequestBody OutputWeighProcessQuery query) {
        return ResponseInfo.success(outputWeighService.getOutputWeighProcess(query));
    }

    @PutMapping("/makeSureWeigher")
    @ApiOperation("确认称量人员")
    @OperationLog
    public ResponseInfo<Long> makeSureWeigher(@Validated @RequestBody OutputMakeSureWeigherDTO dto) {
        return ResponseInfo.success(outputWeighService.makeSureWeigher(dto));
    }

    @PutMapping("/makeSureBatch")
    @ApiOperation("确认产出批次")
    @OperationLog
    public ResponseInfo<Void> makeSureBatch(@Validated @RequestBody OutputMakeSureBatchDTO dto) {
        outputWeighService.makeSureBatch(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/weighAndPrint")
    @ApiOperation("称量打码")
    @OperationLog
    @DistributedLock(expression = "#dto.outputWeighProcessId")
    public ResponseInfo<List<WeighResult.WeighResultItem>> weighAndPrint(@Validated @RequestBody OutputWeighAndPrintDTO dto) {
        return ResponseInfo.success(outputWeighService.weighAndPrint(dto));
    }

    @PostMapping("/sign")
    @ApiOperation("产出签名")
    @OperationLog
    public ResponseInfo<Void> sign(@RequestBody @Validated OutputWeighSignDTO dto) {
        outputWeighService.sign(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/changeWeigher")
    @ApiOperation("更换称量人员")
    @OperationLog
    public ResponseInfo<Long> changeWeigher(@Validated @RequestBody OutputChangeWeigherDTO dto) {
        outputWeighService.changeWeigher(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/scrap")
    @ApiOperation("称量作废")
    @OperationLog
    public ResponseInfo<Void> scrap(@Validated @RequestBody OutputScrapDTO dto) {
        outputWeighService.scrap(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/validateComponentSign")
    @ApiOperation("校验产出称量组件物料件签名")
    @OperationLog
    public ResponseInfo<Boolean> validateComponentSign(@RequestBody @Validated List<OutputWeighValidateSignDTO> validateSignList) {
        return ResponseInfo.success(outputWeighService.validateComponentSign(validateSignList));
    }
}
