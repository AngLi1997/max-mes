package com.bmos.mes.service.log.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.log.dto.ExportOperationLogDTO;
import com.bmos.mes.service.log.dto.OperationLogDetailDTO;
import com.bmos.mes.service.log.dto.QueryLogPageDTO;
import com.bmos.mes.service.log.service.MesOperationLogService;
import com.bmos.mes.service.log.vo.MesLogDetailVO;
import com.bmos.mes.service.log.vo.MesLogPageVO;
import com.bmos.mybatis.page.CommonPage;
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
@Api(tags = "mes操作日志相关接口")
public class MesOperationLogController {

    @Autowired
    private MesOperationLogService mesOperationLogService;

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public ResponseInfo<CommonPage<MesLogPageVO>> getPage(@Validated QueryLogPageDTO dto){
        return ResponseInfo.success(mesOperationLogService.getPage(dto));
    }

    @GetMapping("/detail")
    @ApiOperation("查询详情")
    public ResponseInfo<MesLogDetailVO> getDetail(OperationLogDetailDTO dto){
        return ResponseInfo.success(mesOperationLogService.getDetail(dto));
    }

    @GetMapping("/export")
    @ApiOperation("操作日志导出")
    public ResponseInfo<Void> exportOperationLog(@Validated ExportOperationLogDTO dto) {
        mesOperationLogService.exportOperationLog(dto);
        return ResponseInfo.success();
    }

}
