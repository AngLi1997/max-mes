package com.bmos.mes.service.record.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.record.dto.RecordAuditDTO;
import com.bmos.mes.service.record.service.BatchRecordVersionService;
import com.bmos.mes.service.record.vo.PageRecordAuditVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author renjinguang
 */
@RestController
@RequestMapping("/record/audit")
@Api(tags = "批记录审核接口")
@Validated
public class BatchRecordAuditController {

    @Autowired
    private BatchRecordVersionService versionService;

    @GetMapping("/page/record/audit")
    @ApiOperation(value = "查询记录审核页面")
    public ResponseInfo<CommonPage<PageRecordAuditVO>> pageRecordAudit(RecordAuditDTO dto) {
        return ResponseInfo.success(versionService.pageRecordAudit(dto));
    }

    @GetMapping("/start/flow")
    @ApiOperation(value = "记录发起审核")
    @ApiParam(name = "versionId", value = "记录版本id", required = true)
    @OperationLog
    public ResponseInfo<Boolean> startFlow(Long versionId) {
        return ResponseInfo.success(versionService.startFlow(versionId));
    }
}
