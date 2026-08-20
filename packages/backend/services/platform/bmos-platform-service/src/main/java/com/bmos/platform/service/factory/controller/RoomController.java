package com.bmos.platform.service.factory.controller;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.factory.controller.vo.*;
import com.bmos.platform.service.factory.service.RoomService;
import com.bmos.platform.service.factory.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 房间相关接口
 */
@RestController
@RequestMapping("/factory/room")
@Validated
@Api(tags = "房间相关接口")
public class RoomController {

    @Autowired
    RoomService roomService;

    @PostMapping("/save")
    @ApiOperation("新建房间")
    @OperationLog
    public ResponseInfo<Void> saveRoom(@RequestBody @Validate RoomSaveDTO dto) {
        roomService.saveRoom(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("编辑房间")
    @OperationLog
    public ResponseInfo<Void> updateRoom(@RequestBody @Validate RoomUpdateDTO dto) {
        roomService.updateRoom(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/delete/{id}")
    @ApiOperation("删除房间")
    @OperationLog
    public ResponseInfo<Void> deleteRoom(@PathVariable @Validate @NotNull @ApiParam("工位id") Long id) {
        roomService.deleteRoom(id);
        return ResponseInfo.success();
    }

    @PutMapping("/enable")
    @ApiOperation("启停房间")
    @OperationLog
    public ResponseInfo<Void> enableRoom(@RequestBody @Validate RoomEnableDTO dto) {
        roomService.enableRoom(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("获取房间列表")
    public ResponseInfo<CommonPage<RoomPageVO>> getRoomPage(RoomPageDTO dto) {
        return ResponseInfo.success(roomService.getRoomPage(dto));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取房间详情")
    public ResponseInfo<RoomInfoVO> getRoomInfo(@PathVariable @NotNull @ApiParam("房间id") Long id) {
        return ResponseInfo.success(roomService.getRoomInfo(id));
    }

    @PostMapping("/bind/station")
    @ApiOperation("房间绑定工位")
    @OperationLog
    public ResponseInfo<Void> bindStation(@RequestBody @Validate RoomBindStationDTO dto) {
        roomService.bindStation(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/tree/room")
    @ApiOperation("获取房间树（包含房间信息）")
    public ResponseInfo<List<RoomTreeNodeVO>> getRoomTree() {
        return ResponseInfo.success(roomService.getRoomTree());
    }

    @PostMapping("/print")
    @ApiOperation("打印房间标签")
    public ResponseInfo<RoomPrintVO> printRoom(@RequestBody RoomPrintDTO roomPrintDTO) {
        return ResponseInfo.success(roomService.printRoom(roomPrintDTO.getRoomId()));
    }


    @PostMapping("/envProperty")
    @ApiOperation("保存房间环境参数配置")
    public ResponseInfo<Void> addRoomEnvProperty(@RequestBody @Validated List<FactoryRoomEnvPropertyAddVO> roomEnvPropertyAddVO) {
        roomService.addRoomEnvProperty(BeanUtil.copyToList(roomEnvPropertyAddVO, RoomEnvPropertyDTO.class));
        return ResponseInfo.success();
    }


    @PostMapping("/dashboard/page")
    @ApiOperation("大屏获取房间列表-分页")
    public ResponseInfo<CommonPage<FactoryRoomDTO>> dashBoardPage(@RequestBody RoomPageQueryVO roomPageQueryVO) {
        CommonPage<FactoryRoomDTO> page = roomService.page(BeanUtil.copyProperties(roomPageQueryVO, RoomListQueryDTO.class), BeanUtil.copyProperties(roomPageQueryVO, BasePage.class));
        return ResponseInfo.success(page);
    }

    @PostMapping("/dashboard/list")
    @ApiOperation("大屏获取房间列表")
    public ResponseInfo<List<FactoryRoomDTO>> dashBoardList(@RequestBody RoomListQueryVO listQueryVO) {
        List<FactoryRoomDTO> page = roomService.list(BeanUtil.copyProperties(listQueryVO, RoomListQueryDTO.class));
        return ResponseInfo.success(page);
    }


    @PutMapping("/3D/model")
    @ApiOperation("绑定3D模型")
    public ResponseInfo<Void> bind3DModel(@RequestParam("roomId") Long roomId, @RequestParam("3DModelId") String modelId) {
        roomService.save3DModel(roomId, modelId);
        return ResponseInfo.success();
    }


    @ApiOperation("通过3D模型id查询房间详情")
    @GetMapping("3D/model/{modelId}")
    public ResponseInfo<RoomInfoVO> get3DModel(@PathVariable("modelId") String modelId) {
        return ResponseInfo.success(roomService.getBy3DModel(modelId));
    }




    @ApiOperation("通过楼层id查询该楼层绑定的设备")
    @GetMapping("floor/equipment")
    public ResponseInfo<TenementFloorEquipmentStatisticsVO> getFloorEquipmentStatistics(@RequestParam("id") Long id) {
        return ResponseInfo.success(BeanUtil.copyProperties(roomService.getFloorEquipmentStatistics(id), TenementFloorEquipmentStatisticsVO.class));
    }
}
