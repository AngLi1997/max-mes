package com.bmos.platform.service.equipment.controller;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.equipment.controller.vo.EquipmentOperateAddVO;
import com.bmos.platform.service.equipment.service.EquipmentLogService;
import com.bmos.platform.service.equipment.service.data.EquipmentOperateLogData;
import com.bmos.platform.service.equipment.service.dto.*;
import com.bmos.platform.service.equipment.controller.vo.EquipmentOperateLogVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentStatusLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 设备日志相关接口
 */
@RestController
@RequestMapping("/equipment/log")
@Validated
@Api(tags = "设备日志接口")
public class EquipmentLogController {

    @Autowired
    private EquipmentLogService equipmentLogService;

    @GetMapping("/operate/page")
    @ApiOperation("操作日志分页")
    public ResponseInfo<CommonPage<EquipmentOperateLogVO>> operateLogPage(EquipmentOperateLogPageDTO dto) {
        return ResponseInfo.success(equipmentLogService.operateLogPage(dto));
    }

    @GetMapping("/status/page")
    @ApiOperation("设备状态日志分页面")
    public ResponseInfo<CommonPage<EquipmentStatusLogVO>> statusLogPage(EquipmentStatusLogPageDTO dto) {
        return ResponseInfo.success(equipmentLogService.statusLogPage(dto));
    }


    @GetMapping("/operate/export")
    @ApiOperation("导出操作日志")
    public ResponseInfo<Void> exportOperateLog(EquipmentOperateExportDTO dto) {
        equipmentLogService.exportOperateLog(dto);
        return ResponseInfo.success();
    }

    @GetMapping("/status/export")
    @ApiOperation("导出设备状态日志")
    public void exportStatusLog(EquipmentStatusExportDTO dto) {
        equipmentLogService.exportStatusLog(dto);
    }

    /**
     * 完成填报 会将日志填报状态置为完成
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("保存操作日志")
    @DistributedLock(expression = "#dto.code + #dto.equipmentId")
    public ResponseInfo<Long> saveOperateLog(@RequestBody EquipmentOperateAddVO dto) {
        EquipmentOperateLogData equipmentOperateLogData = BeanUtil.copyProperties(dto,
                EquipmentOperateLogData.class);
        equipmentOperateLogData.setOperateTime(dto.getBeginTime());
        equipmentOperateLogData.setOperateLogId(dto.getId());
        equipmentOperateLogData.setFillLog(true);
        return ResponseInfo.success(equipmentLogService.saveOperateLog(equipmentOperateLogData));
    }

    @PostMapping("/fill")
    @ApiOperation("填报操作日志")
    @DistributedLock(expression = "#dto.code + #dto.equipmentId")
    public ResponseInfo<Void> fillOperateLog(@RequestBody EquipmentOperateLogFillDTO dto) {
        EquipmentOperateLogData equipmentOperateLogData = BeanUtil.copyProperties(dto,
                EquipmentOperateLogData.class);
        equipmentOperateLogData.setOperateTime(dto.getBeginTime());
        equipmentOperateLogData.setOperateLogId(dto.getId());
        equipmentLogService.fillOperateLog(equipmentOperateLogData);
        return ResponseInfo.success();
    }

    @GetMapping("/incomplete")
    @ApiOperation("查询未完成填报的日志")
    public ResponseInfo<EquipmentOperateLogVO> queryIncompleteFillingLog(@NotNull Long equipmentId) {
        return ResponseInfo.success(equipmentLogService.queryIncompleteFillingLog(equipmentId));
    }

}
