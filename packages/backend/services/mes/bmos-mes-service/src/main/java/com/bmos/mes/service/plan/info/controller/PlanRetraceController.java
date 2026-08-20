package com.bmos.mes.service.plan.info.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.plan.info.dto.PlanRetraceInfoPageDTO;
import com.bmos.mes.service.plan.info.dto.ProductPlanBatchDTO;
import com.bmos.mes.service.plan.info.service.PlanRetraceService;
import com.bmos.mes.service.plan.info.vo.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/plan/retrace")
@Api(tags = "批次追溯相关接口")
public class PlanRetraceController {

    @Autowired
    private PlanRetraceService planRetraceService;

    @ApiOperation("查询产品已完成的生产批次的分页信息")
    @GetMapping("/page")
    public ResponseInfo<CommonPage<ProductPlanBatchPageVO>> page(@Validated ProductPlanBatchDTO dto) {
       return ResponseInfo.success(planRetraceService.planBatchRetracePage(dto));
    }

    @ApiOperation("查询产品已完成的生产批次的简单信息")
    @GetMapping("/info")
    public ResponseInfo<PlanRetraceInfoVO> detailInfo(@Validated PlanRetraceInfoPageDTO dto) {
       return ResponseInfo.success(planRetraceService.detailInfo(dto));
    }

    @ApiOperation("批次追溯-生产批次的执行信息")
    @GetMapping("/execute/trace/page")
    public ResponseInfo<CommonPage<PlanRetraceExecutePageVO>> executeTracePage(@Validated PlanRetraceInfoPageDTO dto) {
       return ResponseInfo.success(planRetraceService.executeTracePage(dto));
    }

    @ApiOperation("批次追溯-生产批次的物料信息")
    @GetMapping("/material/trace/page")
    public ResponseInfo<CommonPage<PlanRetraceMaterialPageVO>> materialTracePage(@Validated PlanRetraceInfoPageDTO dto) {
       return ResponseInfo.success(planRetraceService.materialTracePage(dto));
    }

    @ApiOperation("批次追溯-生产批次的设备使用日志")
    @GetMapping("/equipment/trace/page")
    public ResponseInfo<CommonPage<PlanRetraceEquipmentPageVO>> equipmentTracePage(@Validated PlanRetraceInfoPageDTO dto) {
       return ResponseInfo.success(planRetraceService.equipmentTracePage(dto));
    }

    @ApiOperation("批次追溯-生产批次的房间清场信息")
    @GetMapping("/room/trace/page")
    public ResponseInfo<CommonPage<PlanRetraceRoomPageVO>> roomTracePage(@Validated PlanRetraceInfoPageDTO dto) {
       return ResponseInfo.success(planRetraceService.roomTracePage(dto));
    }

    @ApiOperation("批次追溯-生产批次偏差信息")
    @GetMapping("/deviation/trace/page")
    public ResponseInfo<CommonPage<PlanRetraceDeviationPageVO>> procedureTracePage(@Validated PlanRetraceInfoPageDTO dto) {
       return ResponseInfo.success(planRetraceService.procedureTracePage(dto));
    }

    @GetMapping("/executeList")
    @ApiOperation("步骤/任务执行列表")
    public ResponseInfo<List<ProcedureStepTaskExecuteVO>> getProcedureStepTaskExecuteList(@NotNull Long id) {
        return ResponseInfo.success(planRetraceService.getProcedureStepTaskExecuteList(id));
    }
}
