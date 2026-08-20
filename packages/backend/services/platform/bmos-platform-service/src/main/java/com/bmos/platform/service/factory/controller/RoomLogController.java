package com.bmos.platform.service.factory.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.factory.controller.vo.RoomLogPageVO;
import com.bmos.platform.service.factory.service.RoomLogService;
import com.bmos.platform.service.factory.service.dto.RoomLogPageDTO;
import com.bmos.platform.service.factory.service.dto.RoomStatusLogExportDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 房间日志接口
 */
@RestController
@RequestMapping("/factory/room/log")
@Validated
@Api(tags = "房间日志")
public class RoomLogController {

    @Autowired
    private RoomLogService roomLogService;

    @GetMapping("/page")
    @ApiOperation("房间状态清洁日志分页查询")
    public ResponseInfo<CommonPage<RoomLogPageVO>> statusLogPage(RoomLogPageDTO dto) {
        return ResponseInfo.success(roomLogService.cleanLogPage(dto));
    }

    @GetMapping("/export")
    @ApiOperation("导出房间清场日志")
    public ResponseInfo<Void> exportLog(RoomStatusLogExportDTO dto){
        roomLogService.exportLog(dto);
        return ResponseInfo.success();
    }

}
