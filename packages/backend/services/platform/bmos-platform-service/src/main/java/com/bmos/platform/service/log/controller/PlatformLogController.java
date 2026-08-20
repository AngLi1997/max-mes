package com.bmos.platform.service.log.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.log.dto.*;
import com.bmos.platform.service.log.service.LoginLogService;
import com.bmos.platform.service.log.service.PlatformLogService;
import com.bmos.platform.service.log.vo.LoginLogVO;
import com.bmos.platform.service.log.vo.OperationLogDetailVO;
import com.bmos.platform.service.log.vo.OperationLogPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log")
@Api(tags = "平台日志相关接口")
@Validated
public class PlatformLogController {

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private PlatformLogService platformLogService;

    @GetMapping("/login/page")
    @ApiOperation("登录日志查询分页")
    public ResponseInfo<CommonPage<LoginLogVO>> queryLogPage(@Validated QueryLoginLogDTO dto){
        return ResponseInfo.success(loginLogService.queryLogPage(dto));
    }

    @GetMapping("/login/export")
    @ApiOperation("登录日志导出")
    @OperationLog
    public ResponseInfo<Void> exportLog(ExportLoginLogDTO dto){
        loginLogService.exportLog(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/operation/page")
    @ApiOperation("平台操作日志查询分页")
    public ResponseInfo<CommonPage<OperationLogPageVO>> getOperationLogPage(@Validated QueryOperationLogPageDTO dto){
        return ResponseInfo.success(platformLogService.getOperationLogPage(dto));
    }

    @GetMapping("/operation/detail/info")
    @ApiOperation("平台操作日志详情")
    public ResponseInfo<OperationLogDetailVO> getOperationLogDetailInfo(@Validated OperationLogDetailDTO dto){
        return ResponseInfo.success(platformLogService.getOperationLogDetailInfo(dto));
    }

    @GetMapping("/operation/export")
    @ApiOperation("操作日志导出")
    public ResponseInfo<Void> exportOperationLog(@Validated ExportOperationLogDTO dto){
        platformLogService.exportOperationLog(dto);
        return ResponseInfo.success();
    }

    /**
     * 保存审计追溯下的操作日志
     * 包括各服务的操作日志导出以及MES的审批流追溯导出
     * @param object
     * @return
     */
    @PostMapping("/export/save")
    @ApiOperation("保存日志导出日志")
    @OperationLog
    public ResponseInfo<Void> saveExportOperationLog(@RequestBody Object object) {
        return ResponseInfo.success();
    }


}
