package com.bmos.mes.service.preparation.produce.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.preparation.produce.controller.vo.*;
import com.bmos.mes.service.preparation.produce.service.PreparationProduceService;
import com.bmos.mes.service.preparation.produce.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 配液产出控制器
 */
@RestController
@RequestMapping("/mobile/preparation/produce")
@Api(tags = "[移动端]配液产出")
@Validated
public class PreparationProduceController {

    @Autowired
    PreparationProduceService preparationProduceService;


    /**
     * 获取当前选择的产出组件选择的配液单信息以及产出批次信息
     * @param dto
     * @return
     */
    @GetMapping("/progress")
    @ApiOperation("获取当前选择的产出组件选择的配液单信息以及产出批次信息")
    public ResponseInfo<PreparationProduceProgressVO> getPreparationProduceProgress(PreparationProduceProgressDTO dto) {
        return ResponseInfo.success(preparationProduceService.getPreparationProduceProgress(dto));
    }

    /**
     * 获取当前生产批次下的配液单列表
     * @param productPlanId
     * @return
     */
    @GetMapping("/plan/list")
    @ApiOperation("获取当前生产批次下的配液单")
    public ResponseInfo<List<PreparationProducePlanVO>> getProducePlanList(@RequestParam("productPlanId") Long productPlanId) {
        return ResponseInfo.success(preparationProduceService.getProducePlanList(productPlanId));
    }

    @GetMapping("/queryMaterial")
    @ApiOperation("通过配液单查询当前配液单的配液计划组件中的产出中间品")
    public ResponseInfo<PreparationProduceMaterialVO> queryMaterial(@RequestParam("preparationPlanId") Long preparationPlanId) {
        return ResponseInfo.success(preparationProduceService.queryMaterial(preparationPlanId));
    }

    @GetMapping("/queryMaterialBatch")
    @ApiOperation("据所输入的物料批次编号以及配方物料id查询物料批次信息")
    public ResponseInfo<PreparationProduceMaterialBatchVO> queryMaterialBatch(@Validated PreparationMaterialBatchDTO dto) {
        return ResponseInfo.success(preparationProduceService.queryMaterialBatch(dto));
    }

    @GetMapping("/queryCheckUserList")
    @ApiOperation("获取配液产出确认的复核人员列表")
    public ResponseInfo<List<PreparationProduceUserVO>> queryCheckUserList(@Validated PreparationProduceCheckUserDTO dto) {
        return ResponseInfo.success(preparationProduceService.queryCheckUserList(dto));
    }


    @PutMapping("/confirm")
    @ApiOperation("配液产出确认")
    public ResponseInfo<Long> produceConfirm(@RequestBody @Validated ProduceConfirmUserDTO dto) {
        return ResponseInfo.success(preparationProduceService.produceConfirm(dto));
    }

    /**
     * 配液产出
     * @param dto
     * @return
     */
    @PutMapping("/handle")
    @ApiOperation("配液产出（批记录回填）")
    @OperationLog
    public ResponseInfo<String> produceHandle(@RequestBody @Validated PreparationProduceDTO dto) {
        return ResponseInfo.success(preparationProduceService.produceHandle(dto));
    }

    @GetMapping("/queryProduce")
    @ApiOperation("查看当前配液产出组件产出的物料件信息")
    public ResponseInfo<ProduceVO> queryProduce(@RequestParam("progressId") Long progressId) {
        return ResponseInfo.success(preparationProduceService.queryProduce(progressId));
    }

    @PutMapping("/sign")
    @ApiOperation("产出签名")
    @OperationLog
    public ResponseInfo<Void> sign(@RequestBody @Validated ProducerSignDTO dto) {
        preparationProduceService.sign(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/changeProducer")
    @ApiOperation("更换产出人员")
    @OperationLog
    public ResponseInfo<Long> changeProducer(@Validated @RequestBody ProduceChangeUserDTO dto) {
        return ResponseInfo.success(preparationProduceService.changeProducer(dto));
    }

    @PutMapping("/scrap")
    @ApiOperation("产出作废")
    @OperationLog
    public ResponseInfo<Void> scrap(@Validated @RequestBody ProduceScrapDTO dto) {
        preparationProduceService.scrap(dto);
        return ResponseInfo.success();
    }

}
