package com.bmos.mes.service.facotry.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.facotry.controller.vo.FactoryRoomAuthUserVO;
import com.bmos.mes.service.facotry.controller.vo.RoomInfoMobileVO;
import com.bmos.mes.service.facotry.controller.vo.RoomMobilePageVO;
import com.bmos.mes.service.facotry.service.FactoryService;
import com.bmos.mes.service.facotry.service.data.PlanComponentRoomDTO;
import com.bmos.mes.service.facotry.service.dto.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/mobile/factory/room")
@Api(tags = "房间(app相关接口)")
@Validated
public class FactoryRoomAppController {

    @Autowired
    private FactoryService factoryService;

    @GetMapping("/page")
    @ApiOperation("获取房间列表")
    public ResponseInfo<CommonPage<RoomMobilePageVO>> getRoomMobilePage(RoomMobilePageDTO dto) {
        return ResponseInfo.success(factoryService.getRoomMobilePage(dto));
    }

    @PutMapping("/status")
    @ApiOperation("手动改变房间状态")
    @OperationLog
    public ResponseInfo<Void> operateRoomStatus(@RequestBody ChangeRoomStatusDTO dto) {
        factoryService.operateRoomStatus(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/info/{id}")
    @ApiOperation("根据房间id获取房间信息")
    public ResponseInfo<RoomInfoMobileVO> getMobileRoomInfo(@PathVariable @NotNull @ApiParam("房间id") Long id) {
        return ResponseInfo.success(factoryService.getMobileRoomInfo(id));
    }

    @GetMapping("/infoByCode/{code}")
    @ApiOperation("根据房间code获取房间信息")
    public ResponseInfo<RoomInfoMobileVO> getMobileRoomInfoByCode(@PathVariable @NotNull @ApiParam("房间code") String code) {
        return ResponseInfo.success(factoryService.getMobileRoomInfoByCode(code));
    }

    @GetMapping("/component/room/info")
    @ApiOperation("清场相关组件扫描二维码时获取房间详情")
    public ResponseInfo<RoomInfoMobileVO> getMobileComponentRoomInfo(CleanExecuteRoomInfoDTO dto) {
        return ResponseInfo.success(factoryService.getMobileComponentRoomInfo(dto));
    }

    @PostMapping("/room/clean/check/component/save")
    @ApiOperation("保存房间清场检测组件信息")
    public ResponseInfo<Void> saveRoomCleanCheckComponent(@RequestBody RoomCleanCheckSaveDTO dto){
        factoryService.saveRoomCleanCheckComponent(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/room/clean/info/component/save")
    @ApiOperation("保存房间清场信息组件信息")
    public ResponseInfo<Void> saveRoomCleanInfoComponent(@RequestBody RoomCleanInfoSaveDTO dto){
        factoryService.saveRoomCleanInfoComponent(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/clean/room")
    @ApiOperation("根据房间id进行房间清场操作,保存房间执行组件信息")
    public ResponseInfo<Void> cleanRoom(@RequestBody @Validated FactoryRoomCleanDTO dto) {
        factoryService.cleanRoom(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/plan/step/component/room")
    @ApiOperation("查询生产计划所使用的工艺中工序内组件上所绑定的房间信息(剔除不属于生产计划中所配置的产线id)")
    public ResponseInfo<List<RoomInfoMobileVO>> planStepComponentRoomList(PlanComponentRoomDTO dto){
        return ResponseInfo.success(factoryService.planStepComponentRoomList(dto));
    }

    @GetMapping("/auth/user")
    @ApiOperation("获取具有权限的清场人/QA人员")
    public ResponseInfo<List<FactoryRoomAuthUserVO>> getRoomAuthUser(FactoryRoomAuthUserDTO dto) {
        return ResponseInfo.success(factoryService.getRoomAuthUser(dto));
    }

}
