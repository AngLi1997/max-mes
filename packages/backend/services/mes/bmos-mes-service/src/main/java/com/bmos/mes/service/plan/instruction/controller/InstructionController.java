package com.bmos.mes.service.plan.instruction.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.plan.info.dto.PlanPageDTO;
import com.bmos.mes.service.plan.info.vo.PlanPageVO;
import com.bmos.mes.service.plan.instruction.dto.InstructionSaveDTO;
import com.bmos.mes.service.plan.instruction.dto.InstructionUpdateDTO;
import com.bmos.mes.service.plan.instruction.dto.TeamDetailQueryDTO;
import com.bmos.mes.service.plan.instruction.service.InstructionService;
import com.bmos.mes.service.plan.instruction.vo.InstructionDetailVO;
import com.bmos.mes.service.plan.instruction.vo.InstructionPageVO;
import com.bmos.mes.service.plan.instruction.vo.InstructionProcedureVO;
import com.bmos.mybatis.page.CommonPage;
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

import java.util.List;

@RestController
@RequestMapping("/plan/instruction")
@Api(tags = "生产计划指令单")
public class InstructionController {
    @Autowired
    private InstructionService instructionService;

    @GetMapping("/page")
    @ApiOperation("指令单确认列表")
    public ResponseInfo<CommonPage<InstructionPageVO>> page(PlanPageDTO dto) {
        return ResponseInfo.success(
            CommonPage.convertPage(instructionService.page(dto))
        );
    }

    @GetMapping("/start/page")
    @ApiOperation("指令单生产前确认")
    public ResponseInfo<CommonPage<PlanPageVO>> startPage(PlanPageDTO dto) {
        return ResponseInfo.success(
            CommonPage.convertPage(instructionService.startPage(dto))
        );
    }

    @GetMapping("/detail/{id}")
    @ApiOperation("指令单分解详情")
    public ResponseInfo<InstructionDetailVO> detail(@ApiParam(value = "id", name = "生产计划id", required = true) @PathVariable Long id) {
        return ResponseInfo.success(instructionService.detail(id));
    }

    @GetMapping("/team/detail")
    @ApiOperation("生产执行工序换班、工艺换班查询班组信息接口")
    public ResponseInfo<List<InstructionProcedureVO>> teamDetail(@Validated TeamDetailQueryDTO dto){
        return ResponseInfo.success(instructionService.teamDetail(dto));
    }


    @PostMapping("/save")
    @ApiOperation("指令单分解保存")
    @OperationLog
    public ResponseInfo<Long> save(@RequestBody @Validated InstructionSaveDTO dto) {
        return ResponseInfo.success(instructionService.save(dto));
    }

    @PostMapping("/update")
    @ApiOperation("指令单分解更新")
    @OperationLog
    public ResponseInfo<Void> update(@RequestBody @Validated InstructionUpdateDTO dto) {
        instructionService.update(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/generate/{id}")
    @ApiOperation("指令单生成")
    @OperationLog
    public ResponseInfo<Void> generate(@ApiParam(name = "id", value = "生产计划id", required = true) @PathVariable Long id) {
        instructionService.generate(id, false);
        return ResponseInfo.success();
    }

    @PostMapping("/send/{id}")
    @ApiOperation("指令单下发")
    @OperationLog
    public ResponseInfo<Void> send(@ApiParam(name = "id", value = "生产计划id", required = true) @PathVariable Long id) {
        instructionService.send(id);
        return ResponseInfo.success();
    }
}
