package com.bmos.platform.service.factory.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.factory.dto.MobileChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.dto.RoomMobilePageFeignDTO;
import com.bmos.platform.facade.factory.feign.FactoryAppFeign;
import com.bmos.platform.facade.factory.vo.RoomInfoMobileFeignVO;
import com.bmos.platform.facade.factory.vo.RoomMobilePageFeignVO;
import com.bmos.platform.service.factory.controller.vo.RoomAppPageVO;
import com.bmos.platform.service.factory.service.RoomService;
import com.bmos.platform.service.factory.service.dto.RoomAppPageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 移动端房间相关接口
 */
@RestController
@RequestMapping("/feign/factory/room")
@Validated
@Api(tags = "房间相关接口(app端)")
public class RoomAppController implements FactoryAppFeign {

    @Autowired
    RoomService roomService;

    @PostMapping("/page")
    @ApiOperation("(移动端)获取房间列表")
    public ResponseInfo<CommonPage<RoomMobilePageFeignVO>> getRoomMobilePage(@RequestBody RoomMobilePageFeignDTO dto) {
        return ResponseInfo.success(roomService.getRoomMobilePage(dto));
    }

    @PutMapping("/status")
    @ApiOperation("房间状态变更")
    @OperationLog
    public ResponseInfo<Void> operateRoomStatus(@RequestBody MobileChangeRoomStatusFeignDTO dto) {
        roomService.operateRoomStatus(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/info")
    @ApiOperation("获取房间详情")
    public ResponseInfo<RoomInfoMobileFeignVO> getMobileRoomInfo(@RequestParam("id") @NotNull @ApiParam("房间id") Long id) {
        return ResponseInfo.success(roomService.getMobileRoomInfo(id));
    }

    @GetMapping("/infoByCode")
    @Override
    public ResponseInfo<RoomInfoMobileFeignVO> getMobileRoomInfoByCode(String code) {
        return ResponseInfo.success(roomService.getMobileRoomInfoByCode(code));
    }

    @GetMapping("/pageByLineId")
    @ApiOperation("根据产线id获取房间分页")
    public ResponseInfo<CommonPage<RoomAppPageVO>> getRoomPageByLineId(RoomAppPageDTO dto) {
        return ResponseInfo.success(roomService.getRoomPageByLineId(dto));
    }


}
