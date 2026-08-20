package com.bmos.mes.service.weigh.centre2.ticket.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.weigh.centre2.ticket.dto.TicketEditDTO;
import com.bmos.mes.service.weigh.centre2.ticket.dto.TicketPageQuery;
import com.bmos.mes.service.weigh.centre2.ticket.service.ITicketService;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketPageVO;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketWeighRecordVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:13
 */
@RestController
@RequestMapping("/weigh/ticket")
@Api(tags = "称量工单管理")
@Validated
public class TicketWeighTicketController {

    @Resource
    private ITicketService ticketService;

    @PostMapping("/programAuto")
    @ApiOperation("自动规划")
    @OperationLog
    public ResponseInfo<CommonPage<Void>> programAuto() {
        ticketService.programAuto();
        return ResponseInfo.success();
    }

    @PostMapping("/programManual")
    @ApiOperation("手动规划")
    @OperationLog
    public ResponseInfo<CommonPage<Void>> programManual(@RequestBody @Validated @NotEmpty List<Long> requirementIds) {
        ticketService.programManual(requirementIds);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询工单")
    public ResponseInfo<CommonPage<TicketPageVO>> page(TicketPageQuery pageDTO) {
        CommonPage<TicketPageVO> page = ticketService.page(pageDTO);
        return ResponseInfo.success(page);
    }

    @PostMapping("/issue")
    @ApiOperation("下发工单")
    @OperationLog
    public ResponseInfo<Void> issue(@RequestParam @ApiParam(value = "工单ID", required = true, example = "1") @NotNull Long id) {
        ticketService.issue(id);
        return ResponseInfo.success();
    }

    @PostMapping("/cancel")
    @ApiOperation("取消工单")
    @OperationLog
    public ResponseInfo<Void> cancel(@RequestParam @ApiParam(value = "工单ID", required = true, example = "1") @NotNull Long id) {
        ticketService.cancel(id);
        return ResponseInfo.success();
    }
    
    @PostMapping("/edit")
    @ApiOperation("编辑工单")
    @OperationLog
    public ResponseInfo<Void> edit(@RequestBody @Validated TicketEditDTO editDTO) {
        ticketService.edit(editDTO);
        return ResponseInfo.success();
    }

    @PostMapping("/getWeighRecord")
    @ApiOperation("根据工单id查询称量详情")
    public ResponseInfo<List<TicketWeighRecordVO>> getWeighRecord(@RequestParam Long ticketId) {
        List<TicketWeighRecordVO> result = ticketService.getWeighRecord(ticketId);
        return ResponseInfo.success(result);
    }
}