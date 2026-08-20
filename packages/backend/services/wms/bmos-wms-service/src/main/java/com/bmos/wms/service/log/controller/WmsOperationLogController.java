package com.bmos.wms.service.log.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.log.dto.ExportOperationLogDTO;
import com.bmos.wms.service.log.dto.QueryLogPageDTO;
import com.bmos.wms.service.log.service.WmsOperationLogService;
import com.bmos.wms.service.log.vo.WmsLogDetailVO;
import com.bmos.wms.service.log.vo.WmsLogPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/log")
@Api(tags = "wms操作日志相关接口")
public class WmsOperationLogController {

    @Autowired
    private WmsOperationLogService wmsOperationLogService;

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public ResponseInfo<CommonPage<WmsLogPageVO>> getPage(@Validated QueryLogPageDTO dto){
        return ResponseInfo.success(wmsOperationLogService.getPage(dto));
    }

    @GetMapping("/detail")
    @ApiOperation("获取详情")
    public ResponseInfo<WmsLogDetailVO> getDetail(Long id){
        return ResponseInfo.success(wmsOperationLogService.getDetail(id));
    }

    @GetMapping("/export")
    @ApiOperation("操作日志导出")
    public ResponseInfo<Void> exportOperationLog(@Validated ExportOperationLogDTO dto) throws IOException {
        wmsOperationLogService.exportOperationLog(dto);
        return ResponseInfo.success();
    }


}
