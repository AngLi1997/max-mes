package com.bmos.mes.service.weigh.centre2.requirement.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.weigh.centre2.requirement.dto.*;
import com.bmos.mes.service.weigh.centre2.requirement.service.ITicketRequirementGroupService;
import com.bmos.mes.service.weigh.centre2.requirement.service.ITicketRequirementService;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementGroupInfoVO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementGroupPageVO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementVO;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketWeighRecordVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 工单称量（联环）
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 18:53
 */
@RestController
@RequestMapping("/weigh/ticket/requirement/group")
@Api(tags = "称量工单需求管理")
@Validated
public class TicketWeighRequirementGroupController {

    @Resource
    private ITicketRequirementGroupService ticketRequirementGroupService;

    @Resource
    private ITicketRequirementService ticketRequirementService;

    @PostMapping("/create")
    @ApiOperation("创建称量工单组需求")
    @OperationLog
    public ResponseInfo<Long> createRequirementGroup(@RequestBody @Validated TicketRequirementGroupDTO createDTO) {
        Long requirementGroupId = ticketRequirementGroupService.createRequirementGroup(createDTO);
        return ResponseInfo.success(requirementGroupId);
    }

    @PostMapping("/queryMaterialList")
    @ApiOperation(value = "查询配料信息物料列表")
    public ResponseInfo<List<TicketRequirementVO>> queryMaterialList(@Validated @RequestBody TicketRequirementQueryDTO queryDTO) {
        List<TicketRequirementVO> list = ticketRequirementService.queryMaterialList(queryDTO);
        return ResponseInfo.success(list);
    }

    @PostMapping("/saveRequirement")
    @ApiOperation("保存称量工单组配料信息")
    @OperationLog
    public ResponseInfo<Void> saveRequirement(@RequestBody @Validated TicketRequirementGroupRequirementDTO createDTO) {
        ticketRequirementGroupService.saveRequirement(createDTO);
        return ResponseInfo.success();
    }

    @PostMapping("/validateSaveRequirement")
    @ApiOperation("校验保存称量工单组配料信息")
    public ResponseInfo<List<String>> validateSaveRequirement(@RequestBody @Validated TicketRequirementGroupRequirementDTO createDTO) {
        return ResponseInfo.success(ticketRequirementGroupService.validateSaveRequirement(createDTO));
    }

    @PostMapping("/calcFormulaQuantity")
    @ApiOperation("计算配料量")
    public ResponseInfo<BigDecimal> calcFormulaQuantity(@RequestBody @Validated TicketCalcFormulaQuantityDTO dto) {
        return ResponseInfo.success(ticketRequirementGroupService.calcFormulaQuantity(dto));
    }

    @PostMapping("/queryInfo")
    @ApiOperation(value = "查询需求组配料信息物料列表")
    public ResponseInfo<TicketRequirementGroupInfoVO> queryInfo(@Validated @RequestBody TicketRequirementInfoQuery query) {
        TicketRequirementGroupInfoVO res = ticketRequirementGroupService.queryInfo(query);
        return ResponseInfo.success(res);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询需求组")
    public ResponseInfo<CommonPage<TicketRequirementGroupPageVO>> page(TicketRequirementGroupPageDTO pageDTO) {
        CommonPage<TicketRequirementGroupPageVO> page = ticketRequirementGroupService.page(pageDTO);
        return ResponseInfo.success(page);
    }

    @PostMapping("/edit")
    @ApiOperation("修改称量工单需求组")
    @OperationLog
    public ResponseInfo<Boolean> editRequirementGroup(@RequestBody @Validated TicketRequirementGroupEditDTO editDTO) {
        Boolean result = ticketRequirementGroupService.editRequirementGroup(editDTO);
        return ResponseInfo.success(result);
    }
    
    @PostMapping("/makeSure")
    @ApiOperation("确认称量工单需求组")
    @OperationLog
    public ResponseInfo<Boolean> makeSureRequirementGroup(
            @ApiParam(value = "需求组id", required = true, example = "1001")
            @RequestParam("id") @NotNull(message = "需求组id不能为空") Long id) {
        Boolean result = ticketRequirementGroupService.makeSureRequirementGroup(id);
        return ResponseInfo.success(result);
    }
    
    @PostMapping("/cancel")
    @ApiOperation("取消称量工单需求")
    @OperationLog
    public ResponseInfo<Boolean> cancelRequirement(
            @ApiParam(value = "需求id", required = true, example = "1001")
            @RequestParam("id") @NotNull(message = "需求id不能为空") Long id) {
        Boolean result = ticketRequirementGroupService.cancelRequirement(id);
        return ResponseInfo.success(result);
    }

    @PostMapping("/getWeighRecord")
    @ApiOperation("根据工单id查询称量详情")
    public ResponseInfo<List<TicketWeighRecordVO>> getWeighRecord(@RequestParam Long groupId) {
        List<TicketWeighRecordVO> result = ticketRequirementGroupService.getWeighRecord(groupId);
        return ResponseInfo.success(result);
    }
}
