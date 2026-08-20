package com.bmos.mes.service.equipment.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.equipment.EquipmentHeartbeatDTO;
import com.bmos.mes.service.equipment.service.IEquipmentHeartbeatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设备心跳接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/5/9 09:20
 */
@RestController
@RequestMapping("/equipment/heartbeat")
@Validated
@Api(tags = "设备心跳相关接口")
public class EquipmentHeartbeatController {

    @Resource
    private IEquipmentHeartbeatService equipmentHeartbeatService;

    @PostMapping("/sendHeartBeat")
    @ApiOperation("电子天平发送心跳")
    public ResponseInfo<Void> saveEquipmentInfoComponent(@Validated @RequestBody EquipmentHeartbeatDTO dto) {
        equipmentHeartbeatService.flushHeartbeat(dto.getDeviceId(), dto.getBatchNo(), dto.getProductName());
        return ResponseInfo.success();
    }
}
