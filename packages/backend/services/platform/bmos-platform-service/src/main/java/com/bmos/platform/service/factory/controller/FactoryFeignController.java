package com.bmos.platform.service.factory.controller;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.equipment.dto.UpdateStationDTO;
import com.bmos.platform.facade.equipment.vo.EquipmentFeignStationVO;
import com.bmos.platform.facade.factory.dto.BatchRoomCleanPageDTO;
import com.bmos.platform.facade.factory.dto.ChangeRoomStatusFeignDTO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.*;
import com.bmos.platform.service.factory.service.FactoryStationService;
import com.bmos.platform.service.factory.service.LineService;
import com.bmos.platform.service.factory.service.RoomLogService;
import com.bmos.platform.service.factory.service.RoomService;
import com.bmos.platform.facade.factory.dto.LineUseDTO;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/feign/factory")
@Validated
public class FactoryFeignController implements FactoryFeign {

    @Autowired
    private RoomService roomService;

    @Autowired
    private FactoryStationService stationService;

    @Autowired
    private LineService lineService;
    @Autowired
    private RoomLogService roomLogService;

    @Override
    @GetMapping("/line/getLineByCondition")
    public ResponseInfo<List<FactoryLineFeignVO>> getLineByCondition(@RequestParam("name") String name) {
        return ResponseInfo.success(lineService.getLineByCondition(name));
    }

    @Override
    @GetMapping("/line/tree")
    public ResponseInfo<List<LineModuleTreeNodeFeignVO>> getLineModuleTreeVO() {
        return ResponseInfo.success(lineService.getLineModuleTreeVO());
    }

    @Override
    @GetMapping("/line/detail/list")
    public ResponseInfo<List<FactoryLineDetailFeignVO>> getLineDetailByLineIds(@RequestParam(value = "lineIds") Collection<Long> lineIds,
                                                                               @RequestParam("stationFlag") boolean stationFlag) {
        return ResponseInfo.success(lineService.getLineDetailByLineIds(lineIds, stationFlag));
    }

    @Override
    @PutMapping("/bind/config")
    public ResponseInfo<Void> bindUseCount(@RequestBody LineUseDTO dto) {
        lineService.bindUseCount(dto);
        return ResponseInfo.success();
    }

    @Override
    @GetMapping("/room/getRoomFeignTree")
    public ResponseInfo<List<RoomModuleTreeNodeFeignVO>> getRoomFeignTree() {
        return ResponseInfo.success(roomService.getRoomFeignTree());
    }

    @Override
    @GetMapping("/room/getRoomCleanInfoByRoomId")
    public ResponseInfo<RoomCleanInfoFeignVO> getRoomCleanInfoByRoomId(@RequestParam("roomId") Long roomId) {
        return ResponseInfo.success(roomService.getRoomCleanInfoByRoomId(roomId));
    }

    @Override
    @GetMapping("/room/getRoomInfoByLineId")
    public ResponseInfo<List<RoomInfoFeignVO>> getRoomInfoByLineId(@RequestParam("lineId") @NotNull Long lineId, @RequestParam(value = "findStation") @NotNull boolean findStation) {
        return ResponseInfo.success(roomService.getRoomInfoByLineId(lineId, findStation));
    }

    @Override
    @GetMapping("/station/getStationInfoByLineId")
    public ResponseInfo<List<FactoryStationFeignVO>> getStationInfoByLineId(Long lineId) {
        return ResponseInfo.success(stationService.getStationInfoByLineId(lineId));
    }

    @Override
    @PutMapping("/room/changeRoomStatus")
    public ResponseInfo<Void> changeRoomStatus(@RequestBody ChangeRoomStatusFeignDTO dto) {
        roomService.changeRoomStatus(dto);
        return ResponseInfo.success();
    }

    @Override
    @GetMapping("/room/selectByRoomIds")
    public ResponseInfo<List<RoomInfoFeignVO>> selectByRoomIds(@RequestParam(value = "roomIdList") List<Long> roomIdList) {
        return ResponseInfo.success(roomService.selectByRoomIds(roomIdList));
    }

    @Override
    @PostMapping("/room/getRoomCleanInfoPage")
    public ResponseInfo<CommonPage<BatchRoomCleanInfoVO>> getRoomCleanInfoPage(@RequestBody BatchRoomCleanPageDTO dto) {
        return ResponseInfo.success(roomLogService.getRoomCleanInfoPage(dto));
    }

    @Override
    @PostMapping("/station/updateStationUseCount")
    public ResponseInfo<Boolean> updateStationUseCount(UpdateStationDTO dto) {
        return ResponseInfo.success(stationService.updateStationUseCount(dto));
    }

    @Override
    @GetMapping("/station/getStationUserByStationId")
    public ResponseInfo<List<String>> getStationUserByStationId(@RequestParam(value = "stationId") Long stationId) {
        return ResponseInfo.success(stationService.getStationUserByStationId(stationId));
    }

    @Override
    @GetMapping("/station/getStationUserByStationIdList")
    public ResponseInfo<List<String>> getStationUserByStationIdList(@RequestParam(value = "stationIdList") List<Long> stationIdList) {
        return ResponseInfo.success(stationService.getStationUserByStationIdList(stationIdList));
    }

    @Override
    @GetMapping("/station/checkStationPermission")
    public ResponseInfo<List<StationPermissionVO>> checkStationPermission(Collection<Long> stationIdList, String userId) {
        return ResponseInfo.success(stationService.checkStationPermission(stationIdList, userId));
    }

    @Override
    @GetMapping("/station/getStationByRoomId")
    public ResponseInfo<List<EquipmentFeignStationVO>> getStationByRoomId(Long roomId) {
        return ResponseInfo.success(stationService.getStationByRoomId(roomId));
    }

    @Override
    @GetMapping("/station/queryStationById")
    public ResponseInfo<FactoryStationFeignVO> queryStationById(Long stationId) {
        return ResponseInfo.success(stationService.queryStationById(stationId));
    }

    @Override
    @GetMapping("/station/queryStationListByProductLineIds")
    public ResponseInfo<List<FactoryStationFeignVO>> queryStationListByProductLineIds(List<Long> productLineIds) {
        return ResponseInfo.success(stationService.queryStationListByLineIds(productLineIds));
    }

    @Override
    @GetMapping("/station/getStationIdsByUserId")
    public ResponseInfo<List<String>> getStationIdsByUserId(@RequestParam(value = "userId") String userId) {
        return ResponseInfo.success(stationService.getStationIdsByUserId(userId));
    }

    @Override
    @GetMapping("/station/queryLineDetailListByLineIds")
    public ResponseInfo<List<FactoryLineDetailFeignVO>> queryLineDetailListByLineIds(@RequestParam("lineIdList") List<Long> lineIdList) {
        return ResponseInfo.success(lineService.queryLineDetailListByLineIds(lineIdList));
    }

    @Override
    @GetMapping("/query/line/list")
    public ResponseInfo<List<FactoryLineFeignVO>> queryLineListByLineIds(@RequestParam("lineIdList") List<Long> lineIdList){
        return ResponseInfo.success(lineService.queryLineListByLineIds(lineIdList));
    }

    @Override
    @GetMapping("/query/room/list")
    public ResponseInfo<List<FactoryRoomFeignVO>> queryRoomListByRoomIds(@RequestParam("roomIds") List<Long> roomIds){
        return ResponseInfo.success(roomService.queryRoomListByRoomIds(roomIds));
    }

}
