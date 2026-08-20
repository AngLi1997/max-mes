package com.bmos.mes.service.plan.info.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.audit.vo.AuditCategoryCountVO;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.plan.info.dto.*;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.plan.info.vo.*;
import com.bmos.mes.service.requisition.vo.RequisitionPlanMaterialVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/plan/info")
@Api(tags = "生产计划 - 生产指令单")
public class PlanController {
    @Autowired
    private PlanService planService;

    @Resource
    private ProductFormulaConfigureService productFormulaConfigureService;

    @ApiOperation("分页列表 指令单分解页面 需传递 orderBy=t1.confirm_time&dir=desc")
    @GetMapping("/page")
    public ResponseInfo<CommonPage<PlanPageVO>> page(@Validated PlanPageDTO dto) {
        dto.parseProductionStatus();
        return ResponseInfo.success(
            CommonPage.convertPage(planService.page(dto))
        );
    }

    @ApiOperation("待办任务数量")
    @GetMapping("/wait/task/count")
    public ResponseInfo<List<AuditCategoryCountVO>> waitTaskCount() {
        return ResponseInfo.success(planService.waitTaskCount());
    }

    @ApiOperation("生产计划追溯分页列表")
    @PostMapping("/pageTraceable")
    public ResponseInfo<CommonPage<PlanPageVO>> pageTraceable(@Validated @RequestBody PlanTraceablePageDTO dto) {
        return ResponseInfo.success(planService.pageTraceable(dto));
    }

    @ApiOperation("获取所有待执行的生产批次的简单信息")
    @GetMapping("/batch/list")
    public ResponseInfo<List<PlanEasyInfoVO>> batchListByPlanStart() {
        return ResponseInfo.success(planService.batchListByPlanStart());
    }

    @ApiOperation("计划审批分页列表")
    @GetMapping("/audit/page")
    public ResponseInfo<CommonPage<PlanAuditPageVO>> auditPage(PlanAuditPageDTO dto) {
        return ResponseInfo.success(planService.auditPage(dto));
    }

    @ApiOperation("生产前确认关联批次")
    @GetMapping("/startPage")
    public ResponseInfo<List<PlanStartPageVO>> startPage(PlanStartPageDTO dto) {
        return ResponseInfo.success(planService.startPage(dto));
    }

    @ApiOperation("详情")
    @GetMapping("/detail/{id}")
    public ResponseInfo<PlanDetailVO> detail(@PathVariable Long id) {
        return ResponseInfo.success(planService.detail(id));
    }

    @PutMapping("/update")
    @ApiOperation("更新")
    @OperationLog
    public ResponseInfo<Void> update(@RequestBody @Validated PlanUpdateDTO dto) {
        planService.update(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/updateRelation")
    @ApiOperation("更新关联关系")
    @OperationLog
    public ResponseInfo<Void> updateRelation(@RequestBody @Validated PlanRelationUpdateDTO dto) {
        planService.updateRelation(dto);
        return ResponseInfo.success();
    }


    @PutMapping("/discard/{id}")
    @ApiOperation("废弃")
    @OperationLog
    public ResponseInfo<Void> discard(@PathVariable Long id) {
        planService.discard(id);
        return ResponseInfo.success();
    }

    @PutMapping("/approve/{id}")
    @ApiOperation("申请")
    @OperationLog
    public ResponseInfo<Void> approve(@PathVariable Long id) {
        planService.approve(id);
        return ResponseInfo.success();
    }

    @PostMapping("/approveBatch")
    @ApiOperation("批量提交审核")
    @OperationLog
    public ResponseInfo<Void> approveBatch(@RequestBody @Validated PlanApproveBatchDTO dto) {
        planService.approveBatch(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/pause/{id}")
    @ApiOperation("暂停生产执行")
    public ResponseInfo<Void> pauseExecute(@PathVariable Long id){
        planService.pauseExecute(id);
        return ResponseInfo.success();
    }

    @PutMapping("/recover/{id}")
    @ApiOperation("恢复生产执行")
    public ResponseInfo<Void> recoveryExecute(@PathVariable Long id){
        planService.recoveryExecute(id);
        return ResponseInfo.success();
    }

    @ApiOperation("查询所有正在执行中的生产计划")
    @GetMapping("/start/plan/list")
    public ResponseInfo<List<PlanEasyInfoVO>> startPlanList(PlanStartQueryDTO dto) {
        return ResponseInfo.success(planService.startPlanList(dto));
    }

    @ApiOperation("根据产品id和工艺id查询未终止的生产批次信息")
    @GetMapping("/queryNotTerminatedBatchListByProductIdAndProcessId")
    public ResponseInfo<List<PlanSimpleVO>> queryBatchListByProductIdAndProcessId(@RequestParam Long productId, @RequestParam Long processId) {
        return ResponseInfo.success(planService.queryBatchListByProductIdAndProcessId(productId, processId));
    }

    @GetMapping("/listPlanByProcess")
    @ApiOperation("根据工艺版本查询所有生产批次信息列表")
    public ResponseInfo<List<PlanListVO>> queryPlanListByProcess(@Validated PlanListByProcessDTO dto) {
        return ResponseInfo.success(planService.queryPlanListByProcess(dto));
    }

    @GetMapping("/listUnTerminatePlanByProcessId")
    @ApiOperation("根据工艺id查询所有未终止的生产批次信息列表")
    @ApiImplicitParam(name = "processId", value = "工艺id", required = true)
    public ResponseInfo<List<PlanListVO>> listUnTerminatePlanByProcessId(@RequestParam Long processId) {
        return ResponseInfo.success(planService.listUnTerminatePlanByProcessId(processId));
    }

    @GetMapping("/listAllPlanByProductId")
    @ApiOperation("根据产品id查询所有批次列表")
    @ApiImplicitParam(name = "productId", value = "产品id", required = true)
    public ResponseInfo<List<PlanListVO>> listAllPlanByProductId(@RequestParam @NotNull Long productId) {
        return ResponseInfo.success(planService.listAllPlanByProductId(productId));
    }

    @ApiOperation("生产审核进度:批次分页")
    @GetMapping("/productionAuditProgressPage")
    public ResponseInfo<CommonPage<ProductionAuditProgressPageVO>> queryProductionAuditProgressPage(@Validated ProductionAuditProgressQueryDTO dto) {
        return ResponseInfo.success(planService.queryProductionAuditProgressPage(dto));
    }

    @ApiOperation("生产审核进度:批次详情")
    @GetMapping("/auditProgressDetail")
    public ResponseInfo<List<PlanAuditProgressDetailVO>> queryPlanAuditDetailList(@Validated PlanAuditProgressDetailQueryDTO dto) {
        return ResponseInfo.success(planService.queryPlanAuditDetailList(dto));
    }

    @ApiOperation(value = "获取批次所关联的所有批次的信息")
    @GetMapping("/relation/plan/list")
    public ResponseInfo<List<PlanEasyInfoVO>> relationPlan(@RequestParam("planId") Long planId) {
        return ResponseInfo.success(planService.relationPlan(planId));
    }

    @ApiOperation("根据生产计划id查询配方物料信息")
    @GetMapping("/formula/material/list")
    public ResponseInfo<List<RequisitionPlanMaterialVO>> getFormulaMaterialList(@RequestParam("productPlanId") Long productPlanId) {
        return ResponseInfo.success(productFormulaConfigureService.getFormulaMaterialListByPlanId(productPlanId));
    }

}
