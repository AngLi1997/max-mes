package com.bmos.mes.service.preparation.measure.controller;


import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.preparation.measure.dto.*;
import com.bmos.mes.service.preparation.measure.service.LiquidPreparationMeasureLogService;
import com.bmos.mes.service.preparation.measure.service.LiquidPreparationMeasureService;
import com.bmos.mes.service.preparation.measure.vo.*;
import com.bmos.mes.service.preparation.plan.service.LiquidPreparationPlanService;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/liquid/preparation/measure")
@Api(tags = "配液量取")
@Validated
public class LiquidPreparationMeasureController {

    @Autowired
    private LiquidPreparationMeasureService measureService;

    @Autowired
    private LiquidPreparationPlanService liquidPreparationPlanService;

    @Autowired
    private LiquidPreparationMeasureLogService measureLogService;

    @GetMapping("/instance")
    @ApiOperation("获取配液量取组件实例")
    @DistributedLock(expression = "#dto.productPlanId + #dto.componentId")
    public ResponseInfo<LiquidMeasureInstanceVO> getMeasureInstance(@Validated LiquidMeasureInstanceQueryDTO dto) {
        return ResponseInfo.success(measureService.getMeasureInstance(dto));
    }

    @GetMapping("/plan/list")
    @ApiOperation("获取未量取的配液单列表")
    public ResponseInfo<List<UnmeasuredPreparationPlanVO>> getUnmeasuredPreparationPlanList(@NotNull Long productPlanId) {
        return ResponseInfo.success(liquidPreparationPlanService.getUnmeasuredPreparationPlanList(productPlanId));
    }

    @GetMapping("/plan/detail")
    @ApiOperation("配液单详情")
    @ApiImplicitParam(name = "id", value = "配液单id")
    public ResponseInfo<LiquidPreparationDetailVO> queryLiquidPreparationPlanDetailById(@Validated LiquidPreparationPlanDetailQueryDTO detailQueryDTO) {
        return ResponseInfo.success(measureService.queryLiquidPreparationPlanDetail(detailQueryDTO));
    }

    @PostMapping("/confirmMeasure")
    @ApiOperation("确认量取")
    @OperationLog
    public ResponseInfo<Long> confirmMeasure(@RequestBody LiquidPreparationConfirmMeasureDTO dto) {
        return ResponseInfo.success(measureService.confirmMeasure(dto));
    }

    @PostMapping("/addConsumeStorageMaterial")
    @ApiOperation("添加量取物料件")
    @OperationLog
    public ResponseInfo<Void> addConsumeStorageMaterial(@Validated @RequestBody LiquidPreparationAddMaterialDTO dto) {
        measureService.addConsumeStorageMaterial(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/queryMeasureBatchDetail")
    @ApiOperation("查询量取批次详细信息")
    @ApiImplicitParam(name = "measureBatchId", value = "量取批次id")
    public ResponseInfo<LiquidPreparationMeasureBatchDetailVO> queryMeasureBatchDetailInfo(@NotNull Long measureBatchId) {
        return ResponseInfo.success(measureService.queryMeasureBatchDetailInfo(measureBatchId));
    }

    @GetMapping("/result")
    @ApiOperation("量取结果")
    public ResponseInfo<LiquidMeasureResultVO> queryMeasureResult(@Validated LiquidMeasureResultQueryDTO dto) {
        return ResponseInfo.success(measureService.queryMeasureResult(dto));
    }

    @PostMapping("/measureAndPrint")
    @ApiOperation("量取打码")
    @OperationLog
    public ResponseInfo<MeasurePrintResultVO> measureAndPrint(@RequestBody @Validated LiquidMeasureAndPrintDTO dto) {
        return ResponseInfo.success(measureService.measureAndPrint(dto));
    }

    @PostMapping("/complete")
    @ApiOperation("完成量取")
    @OperationLog
    public ResponseInfo<Void> completeMeasure(@Validated @RequestBody LiquidMeasureCompleteDTO dto) {
        measureService.completeMeasure(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/changeMeasurer")
    @ApiOperation("更换量取人员")
    @OperationLog
    public ResponseInfo<Void> changeMeasurer(@Validated @RequestBody LiquidMeasureChangeMeasurerDTO dto) {
        measureService.changeMeasurer(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/sign")
    @ApiOperation("配液量取签名")
    @OperationLog
    public ResponseInfo<Void> sign(@Validated @RequestBody LiquidMeasureSignDTO dto) {
        measureService.sign(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/log/page")
    @ApiOperation("量取日志分页")
    public ResponseInfo<CommonPage<LiquidMeasureLogPageVO>> queryMeasureLogPage(@Validated LiquidMeasureLogPageQueryDTO dto) {
        return ResponseInfo.success(measureLogService.queryMeasureLogPage(dto));
    }




}
