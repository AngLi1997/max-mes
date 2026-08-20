package com.bmos.mes.service.exception.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.exception.dto.*;
import com.bmos.mes.service.exception.service.ExceptionManageService;
import com.bmos.mes.service.exception.vo.ExceptionPageVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/exception")
@Api(tags = "异常管理")
@Validated
public class ExceptionManageController {

    @Resource
    private ExceptionManageService exceptionManageService;

    @GetMapping("/page")
    @ApiOperation("分页")
    public ResponseInfo<CommonPage<ExceptionPageVO>> queryExceptionPage(@Validated ExceptionPageQueryDTO dto) {
        return ResponseInfo.success(exceptionManageService.queryExceptionPage(dto));
    }

    @PostMapping("/save")
    @ApiOperation("手动录入异常")
    @OperationLog
    public ResponseInfo<Void> manualRecordException(@RequestBody @Validated ExceptionManualRecordDTO dto) {
        exceptionManageService.manualRecordException(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/edit")
    @ApiOperation("编辑异常")
    @OperationLog
    public ResponseInfo<Void> editException(@RequestBody @Validated ExceptionEditDTO dto) {
        exceptionManageService.editException(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/cancel")
    @ApiOperation("作废异常")
    @OperationLog
    public ResponseInfo<Void> cancelException(@RequestBody @Validated ExceptionCancelDTO dto) {
        exceptionManageService.cancelException(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/handle")
    @ApiOperation("处理异常")
    @OperationLog
    public ResponseInfo<Void> handleException(@RequestBody @Validated ExceptionHandleDTO dto) {
        exceptionManageService.handleException(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/reInvestigate")
    @ApiOperation("重新调查")
    @OperationLog
    public ResponseInfo<Void> reInvestigateException(@RequestBody @Validated ExceptionReInvestigateDTO dto) {
        exceptionManageService.reInvestigateException(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/batch/page")
    @ApiOperation("批次异常信息分页")
    public ResponseInfo<CommonPage<ExceptionPageVO>> getBatchExceptionPage(@Validated BatchExceptionQueryDTO dto) {
        return ResponseInfo.success(exceptionManageService.getBatchExceptionPage(dto));
    }


}
