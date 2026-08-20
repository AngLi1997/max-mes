package com.bmos.platform.service.equipment.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.equipment.datasource.dto.MqttAccreditInfoDTO;
import com.bmos.platform.service.equipment.service.EquipmentService;
import com.bmos.platform.service.equipment.service.dto.DataPointNameValueDTO;
import com.bmos.platform.service.equipment.service.dto.DataPointValuePageQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 09:20
 */
@RestController
@RequestMapping("/equipment")
@Validated
@Api(tags = "设备相关接口")
public class EquipmentController {


    @Resource
    private EquipmentService equipmentService;


    @GetMapping("/acquisitionPointData")
    @ApiOperation("根据设备id获取设备绑定采集项的所有数据")
    public ResponseInfo<List<DataPointNameValueDTO>> acquisitionPointData(@Param("equipmentId") Long equipmentId) {
        return ResponseInfo.success(equipmentService.getData(equipmentId));
    }


    @GetMapping("/acquisitionPointHistoryData")
    @ApiOperation("查询设备点位历史数据")
    public ResponseInfo<CommonPage<DataPointNameValueDTO>> acquisitionPointHistoryData(DataPointValuePageQueryDTO dataPointValuePageQueryDTO) {
        return ResponseInfo.success(equipmentService.getHistoryData(dataPointValuePageQueryDTO));
    }


    @GetMapping("/mqttAccredit")
    @ApiOperation("获取mqtt授权信息")
    public ResponseInfo<MqttAccreditInfoDTO> getMqttAccreditInfo() {
        return ResponseInfo.success(equipmentService.getMqttAccreditInfo());
    }
}
