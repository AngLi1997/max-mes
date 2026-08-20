package com.bmos.mes.service.weigh.centre2.execute.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.mes.service.weigh.centre2.execute.service.TicketWeighExecuteService;
import com.bmos.mes.service.weigh.centre2.execute.service.dto.*;
import com.bmos.mes.service.weigh.centre2.execute.controller.vo.*;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:15
 */
@RestController
@RequestMapping("/weigh/centre2/execute")
public class TicketWeighExecuteController {

    @Autowired
    private TicketWeighExecuteService ticketWeighExecuteService;

    @GetMapping("/ticket/page")
    @ApiOperation(value = "分页查询 ticket(称量执行页面)")
    public ResponseInfo<CommonPage<WeighTicketPageVO>> pageWeighTicket(WeighTicketPageDTO dto) {
        return ResponseInfo.success(ticketWeighExecuteService.pageWeighTicket(dto, false));
    }

    @GetMapping("/ticket/history/page")
    @ApiOperation(value = "分页查询 ticket(称量历史页面)")
    public ResponseInfo<CommonPage<WeighTicketPageVO>> pageHistoryWeighTicket(WeighTicketPageDTO dto) {
        return ResponseInfo.success(ticketWeighExecuteService.pageWeighTicket(dto, true));
    }

    @PostMapping("/ticket/bind-operator")
    @ApiModelProperty(value = "绑定操作人以及复核人")
    @OperationLog
    public ResponseInfo<Void> bindOperator(@RequestBody TicketBindOperatorDTO dto) {
        ticketWeighExecuteService.bindOperator(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/requirement/execute")
    @ApiOperation(value = "执行称量需求")
    public ResponseInfo<Void> executeWeighRequirement(@RequestBody TicketRequirementBindStorageMaterialDTO dto) {
        ticketWeighExecuteService.executeWeighRequirement(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/requirement/bind-storage-material")
    @ApiOperation(value = "添加物料")
    @OperationLog
    public ResponseInfo<Void> bindStorageMaterial(@RequestBody TicketRequirementBindStorageMaterialDTO dto) {
        ticketWeighExecuteService.bindMaterialToRequirement(dto.getRequirementId(), dto.getStorageMaterialIds());
        return ResponseInfo.success();
    }

    @GetMapping("/ticket/detail/{ticketId}")
    @ApiOperation(value = "获取 ticket 详情(包含所有称量需求（已称量、未称量、称量中）)")
    public ResponseInfo<WeighTicketDetailVO> getWeighTicketDetail(@PathVariable Long ticketId) {
        return ResponseInfo.success(ticketWeighExecuteService.getWeighTicketDetail(ticketId));
    }

    @GetMapping("/requirement/unweighed-or-weighing/{ticketId}")
    @ApiOperation(value = "根据工单ID查询所有未称量或称量中的需求")
    public ResponseInfo<List<WeighRequirementVO>> listUnWeighedOrWeighingRequirements(@PathVariable Long ticketId) {
        return ResponseInfo.success(ticketWeighExecuteService.listUnWeighedOrWeighingRequirements(ticketId));
    }

    @PostMapping("/requirement/record")
    @ApiOperation(value = "保存称量记录")
    @OperationLog
    public ResponseInfo<TicketRequirementEnoughVO> saveWeighRequirementRecord(@RequestBody WeighRequirementRecordDTO dto) {
        return ResponseInfo.success(ticketWeighExecuteService.saveWeighRequirementRecord(dto));
    }

    @PostMapping("/requirement/sign")
    @ApiOperation(value = "称量需求签名")
    @OperationLog
    public ResponseInfo<Void> signWeigh(@RequestBody SignWeighDTO dto) {
        ticketWeighExecuteService.signWeigh(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/requirement/finish")
    @ApiOperation(value = "完成称量接口(非正常完成称量需求时调用此完成接口)")
    @OperationLog
    public ResponseInfo<Void> finishWeighRequirement(@RequestBody FinishWeighDTO dto) {
        ticketWeighExecuteService.finishWeighRequirement(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/requirement/detail/{requirementId}")
    @ApiOperation(value = "根据称量需求id查询称量需求详情")
    public ResponseInfo<WeighRequirementVO> getWeighRequirementDetail(@PathVariable Long requirementId) {
        return ResponseInfo.success(ticketWeighExecuteService.getWeighRequirementDetail(requirementId));
    }

    @PostMapping("/requirement/oddment")
    @ApiOperation(value = "保存余料的称量记录")
    @OperationLog
    public ResponseInfo<TicketRequirementEnoughVO> saveOddmentWeighRecord(@RequestBody WeighRequirementRecordDTO dto) {
        return ResponseInfo.success(ticketWeighExecuteService.saveOddmentWeighRecord(dto));
    }

    /**
     * 根据工单id查询工单内余料信息
     */
    @GetMapping("/ticket/{ticketId}/oddment-info")
    @ApiOperation(value = "根据工单id查询工单内余料信息")
    public ResponseInfo<TicketOddmentInfoVO> getOddmentInfoByTicketId(@PathVariable Long ticketId) {
        return ResponseInfo.success(ticketWeighExecuteService.getOddmentInfoByTicketId(ticketId));
    }

    /**
     * 根据工单id和称量类型查询工单内所有称量记录
     */
    @GetMapping("/ticket/{ticketId}/weigh-records")
    @ApiOperation(value = "根据工单id查询工单内所有称量记录")
    public ResponseInfo<TicketWeighRequirementRecordVO> getWeighRecordsByTicketId(@PathVariable Long ticketId) {
        return ResponseInfo.success(ticketWeighExecuteService.getWeighRecordsByTicketId(ticketId));
    }
}
