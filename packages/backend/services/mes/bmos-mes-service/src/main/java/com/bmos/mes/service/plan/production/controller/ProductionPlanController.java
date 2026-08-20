package com.bmos.mes.service.plan.production.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.plan.production.dto.*;
import com.bmos.mes.service.plan.production.dto.PlanBatchNoDTO;
import com.bmos.mes.service.plan.production.service.ProductionPlanItemService;
import com.bmos.mes.service.plan.production.service.ProductionPlanService;
import com.bmos.mes.service.plan.production.vo.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author renjinguang
 */
@RestController
@RequestMapping("/production")
@Api(tags = "生产计划管理相关接口")
@Validated
public class ProductionPlanController {

    @Resource
    private ProductionPlanService productionPlanService;

    @Resource
    private ProductionPlanItemService productionPlanItemService;

    @GetMapping("/list/page")
    @ApiOperation("查询生产计划管理页")
    public ResponseInfo<CommonPage<ProductionPlanPageVO>> listPage(ProductionPageDTO dto){
        return ResponseInfo.success(productionPlanService.listPage(dto));
    }

    @PutMapping("/plan/nullify/{id}")
    @ApiOperation("计划作废")
    @ApiImplicitParam(value = "计划id",name = "id",required = true)
    @OperationLog
    public ResponseInfo<Void> planNullify(@NotNull @Validated @PathVariable Long id){
        productionPlanService.planNullify(id);
        return ResponseInfo.success();
    }

    @GetMapping("/list/plan/detail")
    @ApiOperation("查询计划详情数据")
    @ApiImplicitParam(value = "计划主键id",name = "id",required = true)
    public ResponseInfo<ProductionPlanDetailVO> listPlanDetail(@NotNull @Validated Long id){
        return ResponseInfo.success(productionPlanService.listPlanDetail(id));
    }

    @GetMapping("/build/plan")
    @ApiOperation("生成生产计划")
    public ResponseInfo<List<List<ProductionPlanItemDetailVO>>> buildPlan(@Validated BuildPlanDTO dto){
        return ResponseInfo.success(productionPlanService.buildPlan(dto));
    }

    @PostMapping("/build/batch/no")
    @ApiOperation("生成编码")
    public ResponseInfo<PlanBatchNextNoMessageVO> buildBatchNo(@RequestBody @Validated List<List<PlanBatchNoDTO>> dto){
        return ResponseInfo.success(productionPlanService.buildBatchNo(dto));
    }

    @PostMapping("/plan/issue")
    @ApiOperation("下发生产计划")
    @OperationLog
    public ResponseInfo<ProductionPlanIssueResVO> issueProductionPlan(@Validated @RequestBody ProductionPlanIssueDTO dto) {
        dto.validBatchList();;
        return ResponseInfo.success(productionPlanService.issueProductionPlan(dto));
    }


    @GetMapping("/calendar")
    @ApiOperation("生产计划日历")
    public ResponseInfo<List<ProductionPlanCalendarVO>> getProductionPlanCalendar(@Validated ProductionPlanCalendarQueryDTO dto) {
        return ResponseInfo.success(productionPlanItemService.getProductionPlanCalendar(dto));
    }

    @GetMapping("/calendar/months")
    @ApiOperation("生产计划日历:多月份")
    public ResponseInfo<List<ProductionPlanCalendarVO>> getProductionPlanMonthsCalendar(@Validated ProductionPlanMonthsCalendarQueryDTO dto) {
        return ResponseInfo.success(productionPlanItemService.getProductionPlanMonthsCalendar(dto));
    }

    @PostMapping("/changeCalendar")
    @ApiOperation("计划日历调整")
    @OperationLog
    public ResponseInfo<Void> changeProductionItemCalendar(@RequestBody ProductionPlanCalendarChangeDTO dto) {
        productionPlanItemService.changeProductionItemCalendar(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/directlyCreate/buildNo")
    @ApiOperation("[直接创建指令单]:根据编号规则生成编号")
    public ResponseInfo<DirectlyCreateBuildNoVO> buildPlanNoAndBatchNo(@Validated DirectlyCreateBuildNoDTO dto) {
        return ResponseInfo.success(productionPlanService.buildPlanNoAndBatchNo(dto));
    }

    @PostMapping("/directlyCreate")
    @ApiOperation("[直接创建指令单]:保存")
    public ResponseInfo<Void> directlyCreatePlan(@RequestBody @Validated DirectlyCreatePlanDTO dto) {
        productionPlanService.directlyCreatePlan(dto);
        return ResponseInfo.success();
    }

}
