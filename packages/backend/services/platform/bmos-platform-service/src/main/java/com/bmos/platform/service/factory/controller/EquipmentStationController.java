package com.bmos.platform.service.factory.controller;

import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.equipment.controller.vo.*;
import com.bmos.platform.service.factory.controller.vo.StationPageVO;
import com.bmos.platform.service.factory.controller.vo.StationTreeNodeVO;
import com.bmos.platform.service.factory.controller.vo.StationVO;
import com.bmos.platform.service.factory.service.FactoryStationService;
import com.bmos.platform.service.factory.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 设备工位controller
 */
@RestController
@RequestMapping("/equipment/station")
@Validated
@Api(tags = "设备工位接口")
public class EquipmentStationController {

    @Autowired
    private FactoryStationService factoryStationService;

    @PostMapping("/save")
    @ApiOperation("新建设备工位")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> saveStation(@RequestBody StationSaveDTO dto) {
        factoryStationService.saveStation(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/update")
    @ApiOperation("编辑设备工位")
    @OperationLog(remark = "getDescription")
    public ResponseInfo<Void> updateStation(@RequestBody StationUpdateDTO dto) {
        factoryStationService.updateStation(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/delete/{id}")
    @ApiOperation("删除设备工位")
    @OperationLog
    public ResponseInfo<Void> deleteStation(@PathVariable @NotNull @ApiParam("工位id") Long id) {
        factoryStationService.deleteStation(id);
        return ResponseInfo.success();
    }

    @PutMapping("/enable")
    @ApiOperation("启停设备工位")
    @OperationLog
    public ResponseInfo<Void> enableStation(@RequestBody StationEnableDTO dto) {
        factoryStationService.enableStation(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/page")
    @ApiOperation("获取设备工位列表")
    public ResponseInfo<CommonPage<StationPageVO>> getStationPage(StationPageDTO dto) {
        return ResponseInfo.success(factoryStationService.getStationPage(dto));
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取设备工位详情")
    public ResponseInfo<StationVO> getStationInfo(@PathVariable @NotNull @ApiParam("工位id") Long id) {
        return ResponseInfo.success(factoryStationService.getStationInfo(id));
    }

    @PostMapping("/bind/equipment")
    @ApiOperation("工位绑定设备")
    @OperationLog
    public ResponseInfo<Void> bindEquipment(@RequestBody StationBindEquipmentDTO dto) {
        factoryStationService.bindEquipment(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/bind/user")
    @ApiOperation("工位绑定用户")
    @OperationLog
    public ResponseInfo<Void> bindUser(@RequestBody StationBindUserDTO dto) {
        factoryStationService.bindUser(dto);
        return ResponseInfo.success();
    }

    @PostMapping("/user/bind/station")
    @ApiOperation("用户绑定工位")
    @OperationLog
    @DistributedLock(key = "userBindStations")
    public ResponseInfo<Void> userBindStations(@RequestBody UserBindStationsDTO dto) {
        factoryStationService.userBindStations(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/tree/equipment")
    @ApiOperation("设备树")
    public ResponseInfo<List<StationEquipmentInfoTreeNodeVO>> stationEquipment(){
        return ResponseInfo.success(factoryStationService.stationEquipment());
    }

    @GetMapping("/tree")
    @ApiOperation("工位树(包含工位信息)")
    public ResponseInfo<List<StationTreeNodeVO>> stationTree(){
        return ResponseInfo.success(factoryStationService.stationTree());
    }

    @GetMapping("/user/station/list")
    @ApiOperation("获取用户已绑定的工位")
    public ResponseInfo<List<Long>> userStationList(@RequestParam("userId") @ApiParam("用户id") @NotNull String userId) {
        return ResponseInfo.success(factoryStationService.userStationList(userId));
    }

}
