package com.bmos.mes.service.operation.history.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.operation.history.dto.OperationHistorySaveDTO;
import com.bmos.mes.service.operation.history.dto.OperationLogPageQueryDTO;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import com.bmos.mes.service.operation.history.vo.OperationLogPageVO;
import com.bmos.mes.service.record.vo.VersionLogVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operation/history")
@Validated
@Api(tags = "操作日志相关接口")
public class OperationHistoryController {

    @Autowired
    private OperationHistoryService operationHistoryService;

    @GetMapping("/page")
    @ApiOperation("分页")
    public ResponseInfo<CommonPage<OperationLogPageVO>> getPage(@Validated OperationLogPageQueryDTO dto) {
        return ResponseInfo.success(operationHistoryService.getPage(dto));
    }

    @GetMapping("/list/{businessId}")
    @ApiOperation("列表")
    public ResponseInfo<List<VersionLogVO>> getList(@PathVariable("businessId") Long businessId) {
        return ResponseInfo.success(operationHistoryService.listRecordLog(businessId));
    }

    @PostMapping("/save")
    @ApiOperation("保存操作历史")
    @OperationLog
    public ResponseInfo<Void> save(@Validated @RequestBody OperationHistorySaveDTO dto){
        operationHistoryService.saveLog(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/plan/history/list/{businessId}")
    @ApiOperation("生产历史操作历史")
    public ResponseInfo<List<VersionLogVO>> getPlanHistoryList(@PathVariable("businessId") Long businessId) {
        return ResponseInfo.success(operationHistoryService.getPlanHistoryList(businessId));
    }

}
