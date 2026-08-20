package com.bmos.mes.service.audit.controller;

import com.bmos.cache.redis.stabilization.ApiStabilization;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.audit.dto.*;
import com.bmos.mes.service.audit.service.FlowAuditCategoryService;
import com.bmos.mes.service.audit.service.FlowAuditMessageService;
import com.bmos.mes.service.audit.service.FlowAuditService;
import com.bmos.mes.service.audit.service.FlowAuditVersionService;
import com.bmos.mes.service.audit.vo.*;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author renjinguang
 */
@RestController
@RequestMapping("/audit")
@Api(tags = "流程审核相关接口")
@Validated
public class FlowAuditController {

    @Autowired
    private FlowAuditService auditService;

    @Autowired
    private FlowAuditVersionService versionService;

    @Autowired
    private FlowAuditCategoryService categoryService;

    @Autowired
    private FlowAuditMessageService messageService;

    @GetMapping("/flow/audit/page")
    @ApiOperation(value = "查询审核流程配置管理页")
    public ResponseInfo<CommonPage<FlowAuditVO>> flowAuditPage(@Validated AuditPageDTO dto) {
        return ResponseInfo.success(auditService.flowAuditPage(dto));
    }

    @PutMapping("/flow/audit/bind/process")
    @ApiOperation(value = "流程绑定工艺")
    @OperationLog
    @ApiStabilization("#dto.id")
    public ResponseInfo<Void> bindFlowAuditProcess(@RequestBody @Validated FlowProcessBindDTO dto) {
        auditService.bindFlowAuditProcess(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/flow/audit/process/list")
    @ApiOperation(value = "根据流程code获取其绑定的工艺id集合")
    public ResponseInfo<List<Long>> flowAuditProcessList(@RequestParam("code") String code) {
        return ResponseInfo.success(auditService.flowAuditProcessList(code));
    }

    @PostMapping("/checkout/deployment")
    @ApiOperation(value = "校验流程")
    public ResponseInfo<Boolean> checkoutDeployment(@Validated @RequestBody FlowCheckoutDTO dto) {
        return ResponseInfo.success(auditService.checkoutDeployment(dto));
    }

    @PostMapping("/save/flow/audit")
    @ApiOperation(value = "保存流程模型")
    @OperationLog
    public ResponseInfo<Void> saveFlowAudit(@Validated @RequestBody SaveAuditDTO dto) {
        auditService.saveFlowAudit(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/changeState")
    @ApiOperation("启停流程版本")
    @OperationLog
    public ResponseInfo<Void> changeFlowAuditState(@RequestBody @Validated ChangeAuditVersionStateDTO dto) {
        auditService.changeFlowAuditState(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/delete/flow/audit")
    @ApiOperation(value = "删除流程")
    @ApiParam(name = "versionId", value = "流程版本id", required = true)
    @OperationLog
    public ResponseInfo<Boolean> deleteFlowAudit(@Validated @NotNull Long versionId) {
        return ResponseInfo.success(versionService.deleteFlowAudit(versionId));
    }

    @GetMapping("/detail/flow/audit")
    @ApiOperation(value = "根据流程版本id查询详情设计")
    @ApiParam(name = "versionId", value = "流程版本id", required = true)
    public ResponseInfo<FlowAuditDetailVO> detailFlowAudit(@Validated @NotNull Long versionId) {
        return ResponseInfo.success(auditService.detailFlowAudit(versionId));
    }

    @GetMapping("/get/flow/audit/code")
    @ApiOperation(value = "生成编码")
    public ResponseInfo<Long> getFlowAuditCode() {
        return ResponseInfo.success(CustomIdGenerator.nextId());
    }

    @GetMapping("/list/flow/audit/history")
    @ApiOperation(value = "查询审批历史")
    @ApiParam(name = "processInstanceId", value = "流程实例id", required = true)
    public ResponseInfo<FlowAuditHistoryVO> listFlowAuditHistory(FlowAuditHistoryDTO dto) {
        return ResponseInfo.success(auditService.listFlowAuditHistory(dto));
    }

    @GetMapping("/list/flow/audit/category")
    @ApiOperation(value = "查询左侧分类树")
    public ResponseInfo<List<FlowAuditCategoryVO>> listFlowAuditCategory() {
        return ResponseInfo.success(categoryService.listFlowAuditCategory());
    }

    @GetMapping("/flow/audit/history/category")
    @ApiOperation(value = "查询流程审核追溯左侧分类树")
    public ResponseInfo<List<FlowAuditCategoryVO>> flowAuditHistoryCategory() {
        return ResponseInfo.success(categoryService.flowAuditHistoryCategory());
    }

    @GetMapping("/list/audit/history")
    @ApiOperation(value = "查询审核流追溯")
    public ResponseInfo<CommonPage<AuditHistoryVO>> listAuditHistory(@Validated AuditHistoryDTO dto) {
        return ResponseInfo.success(auditService.listAuditHistory(dto));
    }

    @GetMapping("/list/task/history")
    @ApiOperation(value = "查询历史接口")
    public ResponseInfo<List<TaskHistoryVO>> listTaskHistory(@NotBlank String processInstanceId) {
        return ResponseInfo.success(auditService.listTaskHistory(processInstanceId));
    }

    @GetMapping("/export/audit/history")
    @ApiOperation(value = "导出流程追溯")
    @OperationLog
    public ResponseInfo<Void> exportAuditHistory(@Validated AuditHistoryExportDTO dto, HttpServletResponse response) {
        auditService.exportAuditHistory(dto, response);
        return ResponseInfo.success();
    }

    @GetMapping("/export/task/history")
    @ApiOperation(value = "导出任务列表")
    public ResponseInfo<Void> exportTaskHistory(@Validated ExportTaskHistoryDTO dto, HttpServletResponse response) {
        auditService.exportTaskHistory(dto, response);
        return ResponseInfo.success();
    }

    @PostMapping("/complete")
    @ApiOperation("任务处理-审批通过")
    @OperationLog
    public ResponseInfo<Boolean> flowAuditComplete(@RequestBody @Validated CompleteDTO dto) {
        return ResponseInfo.success(auditService.flowAuditComplete(dto));
    }

    @PostMapping("/complete/not/approve")
    @ApiOperation("任务处理-审批不通过")
    @OperationLog
    public ResponseInfo<Boolean> flowAuditCompleteNotApprove(@RequestBody @Validated CompleteDTO dto) {
        return ResponseInfo.success(auditService.flowAuditCompleteNotApprove(dto));
    }

    @PostMapping("/back/to/prev")
    @ApiOperation("任务处理-回退")
    @OperationLog
    public ResponseInfo<Boolean> flowAuditBackToPrev(@RequestBody @Validated AuditBackToPrevDTO dto) {
        return ResponseInfo.success(auditService.flowAuditBackToPrev(dto));
    }

    @GetMapping("/list/make/user")
    @ApiOperation("根据节点id以及流程启动id查询抄送人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nodeId", value = "节点id", required = true),
            @ApiImplicitParam(name = "deploymentId", value = "流程启动id", required = true),
    })
    public ResponseInfo<List<String>> listMakeUser(@NotBlank String nodeId, @NotBlank String deploymentId) {
        return ResponseInfo.success(messageService.listMakeUser(nodeId, deploymentId));
    }
}
