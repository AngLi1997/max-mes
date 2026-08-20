package com.bmos.mes.service.ingredient.plan.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.ingredient.plan.dto.*;
import com.bmos.mes.service.ingredient.plan.service.IngredientService;
import com.bmos.mes.service.ingredient.plan.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient")
@Api(tags = "配料计划")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/detail")
    @ApiOperation("获取组件配料单详情")
    public ResponseInfo<IngredientPlanVO> getMaterialRequisitionPlanVO(@Validated IngredientQueryDTO dto){
        return ResponseInfo.success(ingredientService.getMaterialIngredientPlanVO(dto));
    }

    @GetMapping("/availableBoundMaterialBatch")
    @ApiOperation("获取物料批次")
    public ResponseInfo<List<AvailableAndBoundMaterialBatchVO>> getAvailableAndBoundMaterialBatch(@Validated IngredientAvailableAndBoundBatchQueryDTO dto){
        return ResponseInfo.success(ingredientService.getAvailableAndAddedMaterialBatch(dto));
    }

    @GetMapping("/boundMaterialBatch")
    @ApiOperation("获取已添加到配料单的批次列表")
    public ResponseInfo<List<IngredientBoundMaterialBatchVO>> getBoundMaterialBatch(@Validated IngredientBoundMaterialBatchQueryDTO dto){
        return ResponseInfo.success(ingredientService.getBoundMaterialBatch(dto));
    }

    @PostMapping("/bindMaterialBatch")
    @ApiOperation("添加物料批次")
    public ResponseInfo<Void> ingredientBindMaterialBatch(@RequestBody @Validated IngredientBindMaterialBatchDTO dto){
        ingredientService.ingredientBindMaterialBatch(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/complete")
    @ApiOperation("完成配料计划")
    @OperationLog
    public ResponseInfo<Void> completeIngredientPlan(@RequestBody IngredientPlanCompleteDTO dto){
        ingredientService.completeIngredientPlan(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/calculate")
    @ApiOperation("勾选批次配料量计算")
    public ResponseInfo<IngredientQuantityCalculateVO> calculateTheoreticalQuantity(@Validated TheoreticalQuantityCalculateDTO dto){
        return ResponseInfo.success(ingredientService.calculateTheoreticalQuantity(dto));
    }

    @PostMapping("/calculate/batch")
    @ApiOperation("配料量计算")
    public ResponseInfo<IngredientQuantityListCalculateVO> calculateIngredientQuantity(@RequestBody @Validated IngredientQuantityCalculateDTO dto){
        return ResponseInfo.success(ingredientService.calculateIngredientQuantity(dto));
    }

}
