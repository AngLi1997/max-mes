package com.bmos.lims2.web.audit;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.audit.operationlog.dto.OperationHistorySaveDTO;
import com.bmos.lims2.server.audit.operationlog.dto.OperationLogPageQueryDTO;
import com.bmos.lims2.server.audit.operationlog.service.AuditOperationLogService;
import com.bmos.lims2.server.audit.operationlog.vo.ListLogVO;
import com.bmos.lims2.server.audit.operationlog.vo.OperationLogPageVO;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit/operation/history")
@Validated
@Api(tags = "操作日志相关接口")
public class AuditOperationHistoryController {

    @Autowired
    private AuditOperationLogService auditOperationLogService;

    @GetMapping("/page")
    @ApiOperation("分页")
    public ResponseInfo<CommonPage<OperationLogPageVO>> getPage(@Validated OperationLogPageQueryDTO dto) {
        return ResponseInfo.success(auditOperationLogService.getPage(dto));
    }

    @GetMapping("/list/{businessId}")
    @ApiOperation("列表")
    public ResponseInfo<List<ListLogVO>> getList(@PathVariable("businessId") Long businessId) {
        return ResponseInfo.success(auditOperationLogService.listRecordLog(businessId));
    }

    @PostMapping("/save")
    @ApiOperation("保存操作历史")
    @OperationLog
    public ResponseInfo<Void> save(@Validated @RequestBody OperationHistorySaveDTO dto){
        auditOperationLogService.saveLog(dto);
        return ResponseInfo.success();
    }

}
