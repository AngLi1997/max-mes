package com.bmos.mes.service.preparation.plan.controller;

import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.preparation.plan.dto.*;
import com.bmos.mes.service.preparation.plan.service.LiquidPreparationPlanService;
import com.bmos.mes.service.preparation.plan.vo.LiquidPreparationAvailableBoundMaterialBatchVO;
import com.bmos.mes.service.preparation.plan.vo.LiquidPreparationBoundMaterialBatchVO;
import com.bmos.mes.service.preparation.plan.vo.LiquidPreparationPlanInstanceVO;
import com.bmos.mes.service.preparation.plan.vo.LiquidPreparationQuantityCalculateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/liquid/preparation/plan")
@Api(tags = "配液计划")
@Validated
public class LiquidPreparationPlanController {

    @Autowired
    private LiquidPreparationPlanService liquidPreparationPlanService;

    @GetMapping("/instance")
    @ApiOperation("获取配液单实例")
    @DistributedLock(expression = "#dto.productPlanId + #dto.componentId")
    public ResponseInfo<LiquidPreparationPlanInstanceVO> getPreparationPlanInstance(@Validated LiquidPreparationPlanInstanceQueryDTO dto) {
        return ResponseInfo.success(liquidPreparationPlanService.getPreparationPlanInstance(dto));
    }

    @GetMapping("/boundMaterialBatch")
    @ApiOperation("获取已添加批次列表")
    public ResponseInfo<List<LiquidPreparationBoundMaterialBatchVO>> getBoundMaterialBatch(@Validated LiquidPreparationBoundBatchQueryDTO dto) {
        return ResponseInfo.success(liquidPreparationPlanService.getBoundMaterialBatch(dto));
    }

    @GetMapping("/availableBoundMaterialBatch")
    @ApiOperation("获取可添加与已添加的批次列表")
    public ResponseInfo<List<LiquidPreparationAvailableBoundMaterialBatchVO>> getBoundAndAvailableMaterialBatch(@Validated LiquidPreparationAvailableBoundBatchQueryDTO dto) {
        return ResponseInfo.success(liquidPreparationPlanService.getBoundAndAvailableMaterialBatch(dto));
    }

    @PostMapping("/boundMaterialBatch")
    @ApiOperation("配液计划添加批次")
    public ResponseInfo<Void> preparationBindMaterialBatch(@RequestBody @Validated LiquidPreparationBindMaterialBatchDTO dto){
        liquidPreparationPlanService.BindMaterialBatch(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/complete")
    @ApiOperation("完成配料计划")
    @OperationLog
    public ResponseInfo<Void> completePreparationPlan(@RequestBody @Validated LiquidPreparationPlanCompleteDTO dto) {
        liquidPreparationPlanService.completePreparationPlan(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/calculate")
    @ApiOperation("配液量计算")
    public ResponseInfo<LiquidPreparationQuantityCalculateVO> calculatePreparationQuantity(@RequestBody LiquidPreparationQuantityCalculateDTO dto) {
        return ResponseInfo.success(liquidPreparationPlanService.calculatePreparationQuantity(dto));
    }

}
