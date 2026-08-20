package com.bmos.platform.service.equipment.service;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.equipment.controller.vo.EquipmentOperateLogVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentStatusLogVO;
import com.bmos.platform.service.equipment.model.EquipmentOperateLog;
import com.bmos.platform.service.equipment.service.data.EquipmentOperateLogData;
import com.bmos.platform.service.equipment.service.data.EquipmentStatusLogData;
import com.bmos.platform.service.equipment.service.dto.EquipmentOperateExportDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentOperateLogPageDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentStatusExportDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentStatusLogPageDTO;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public interface EquipmentLogService {

    /**
     * 操作日志分页
     * @param dto
     * @return
     */
    CommonPage<EquipmentOperateLogVO> operateLogPage(EquipmentOperateLogPageDTO dto);

    /**
     * 设备状态日志分页面
     * @param dto
     * @return
     */
    CommonPage<EquipmentStatusLogVO> statusLogPage(EquipmentStatusLogPageDTO dto);

    /**
     * 新增设备操作日志
     * @param operateLogData: 需要记录的日志id
     */
    Long saveOperateLog(EquipmentOperateLogData operateLogData);

    /**
     * 保存设备变更日志
     * @param statusLogData
     */
    void saveStatusLog(EquipmentStatusLogData statusLogData);

    /**
     * 导出操作日志
     * @param dto
     */
    void exportOperateLog(EquipmentOperateExportDTO dto);

    /**
     * 导出设备状态日志
     * @param dto
     */
    void exportStatusLog(EquipmentStatusExportDTO dto);

    /**
     * 根据id获取操作日志
     * @param id
     * @return
     */
    @Nullable
    EquipmentOperateLog selectOperateLogById(Long id);

    /**
     * 查询设备未填报完成的设备操作日志
     * @param equipmentId
     * @return
     */
    EquipmentOperateLogVO queryIncompleteFillingLog(@NotNull Long equipmentId);

    /**
     * 填报操作日志
     * 填报状态为未完成填报
     * @param equipmentOperateLogData
     */
    void fillOperateLog(EquipmentOperateLogData equipmentOperateLogData);
}
