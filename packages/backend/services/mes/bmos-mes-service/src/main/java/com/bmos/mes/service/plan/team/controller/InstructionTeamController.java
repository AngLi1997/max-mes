package com.bmos.mes.service.plan.team.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.plan.team.dto.InstructionBatchConfirmDTO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamConfirmDTO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamProductStartConfirmDTO;
import com.bmos.mes.service.plan.team.service.InstructionTeamService;
import com.bmos.mes.service.plan.team.vo.InstructionTeamDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plan/instruction/team")
@Api(tags = "生产计划指令单班组")
public class InstructionTeamController {
    @Autowired
    private InstructionTeamService instructionTeamService;

    @GetMapping("/detail/{id}")
    @ApiOperation("指令单确认-详情")
    @ApiParam(name = "id", value = "指令单id", required = true)
    public ResponseInfo<InstructionTeamDetailVO> detail(@PathVariable Long id) {
        return ResponseInfo.success(instructionTeamService.detail(id));
    }

    @PostMapping("/confirm")
    @OperationLog
    @ApiOperation("指令单确认-班组信息")
    public ResponseInfo<Void> confirm(@RequestBody @Validated InstructionTeamConfirmDTO dto) {
        instructionTeamService.confirm(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/batchConfirm")
    @OperationLog
    @ApiOperation("指令单确认-批量确认")
    public ResponseInfo<Void> batchConfirm(@RequestBody @Validated InstructionBatchConfirmDTO dto) {
        instructionTeamService.batchConfirm(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/save")
    @ApiOperation("指令单保存-班组信息")
    public ResponseInfo<Void> save(@RequestBody @Validated InstructionTeamConfirmDTO dto) {
        instructionTeamService.save(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/start/confirm")
    @ApiOperation("生产前确认保存-班组信息")
    public ResponseInfo<Void> startConfirm(@RequestBody @Validated InstructionTeamProductStartConfirmDTO dto) {
        instructionTeamService.startConfirm(dto);
        return ResponseInfo.success();
    }
}
