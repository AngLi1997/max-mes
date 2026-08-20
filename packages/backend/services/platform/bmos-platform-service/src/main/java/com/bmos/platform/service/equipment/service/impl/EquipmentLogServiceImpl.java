package com.bmos.platform.service.equipment.service.impl;

import com.alibaba.excel.EasyExcel;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.exception.BmosException;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.common.enums.equipment.OperateLogFillingStatusEnum;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.service.equipment.controller.vo.EquipmentOperateExportLogVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentOperateLogVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentStatusExportLogVO;
import com.bmos.platform.service.equipment.controller.vo.EquipmentStatusLogVO;
import com.bmos.platform.service.equipment.convert.EquipmentConvert;
import com.bmos.platform.service.equipment.mapper.EquipmentOperateLogMapper;
import com.bmos.platform.service.equipment.mapper.EquipmentStatusLogMapper;
import com.bmos.platform.service.equipment.model.EquipmentOperateLog;
import com.bmos.platform.service.equipment.model.EquipmentStatusLog;
import com.bmos.platform.service.equipment.service.EquipmentLogService;
import com.bmos.platform.service.equipment.service.data.EquipmentOperateLogData;
import com.bmos.platform.service.equipment.service.data.EquipmentStatusLogData;
import com.bmos.platform.service.equipment.service.dto.EquipmentOperateExportDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentOperateLogPageDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentStatusExportDTO;
import com.bmos.platform.service.equipment.service.dto.EquipmentStatusLogPageDTO;
import com.bmos.platform.service.utils.UserUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class EquipmentLogServiceImpl implements EquipmentLogService {

    @Autowired
    EquipmentStatusLogMapper equipmentStatusLogMapper;

    @Autowired
    EquipmentOperateLogMapper equipmentOperateLogMapper;

    private final String EXPORT_NAME = "设备状态日志";

    private final String EXPORT_SHEET_NAME = "设备状态日志";

    @Override
    public CommonPage<EquipmentOperateLogVO> operateLogPage(EquipmentOperateLogPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<EquipmentOperateLog> equipmentOperateLogList = equipmentOperateLogMapper.selectPageByParam(dto);
        return CommonPage.convertPage(equipmentOperateLogList, EquipmentConvert.INSTANCE::convert2OperateLogVO);
    }

    @Override
    public CommonPage<EquipmentStatusLogVO> statusLogPage(EquipmentStatusLogPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<EquipmentStatusLog> equipmentOperateLogList = equipmentStatusLogMapper.selectPageByParam(dto);
        return CommonPage.convertPage(equipmentOperateLogList, EquipmentConvert.INSTANCE::convert2StatusLogVO);
    }

    @Override
    public Long saveOperateLog(EquipmentOperateLogData operateLogData) {
        SysUser operator = Optional.ofNullable(operateLogData.getOperator())
                .map(e-> (SysUser) UserUtils.getUser(operateLogData.getOperator()))
                .orElse(SysUserHolder.getUser());
        EquipmentOperateLog operateLog;
        if (Objects.nonNull(operateLogData.getOperateLogId()) && !operateLogData.isFillLog()){
            operateLog = equipmentOperateLogMapper.selectById(operateLogData.getOperateLogId());
            if (Objects.nonNull(operator) && Objects.nonNull(operator.getUserId())){
                // 自动释放没有操作人
                operateLog.setEndOperator(operator.getUserId());
                operateLog.setEndOperatorName(operator.getUserName() + "-" + operator.getLoginName());
            }
            operateLog.setEndTime(operateLogData.getOperateTime());
            equipmentOperateLogMapper.updateById(operateLog);
        } else if (operateLogData.isFillLog()) {
            EquipmentOperateLog equipmentOperateLog = equipmentOperateLogMapper.selectIncompleteFillingLog(operateLogData.getEquipmentId());
            if (Objects.nonNull(equipmentOperateLog) && !Objects.equals(operateLogData.getOperateLogId(), equipmentOperateLog.getId())) {
                throw new BmosException(PlatformResponseCode.THERE_IS_LOG_THAT_HAS_NOT_BEEN_FILLED_OUT);
            }
            EquipmentOperateLog log = EquipmentConvert.INSTANCE.convertEquipmentOperateLog(operateLogData, operator);
            log.setId(Optional.ofNullable(equipmentOperateLog).orElse(log).getId());
            log.setFillStatus(OperateLogFillingStatusEnum.COMPLETED);
            equipmentOperateLogMapper.saveOrUpdate(log);
            return log.getId();
        } else {
            operateLog = EquipmentConvert.INSTANCE.convertEquipmentOperateLog(operateLogData, operator);
            equipmentOperateLogMapper.insert(operateLog);
        }
        return operateLog.getId();
    }

    @Override
    public void saveStatusLog(EquipmentStatusLogData statusLogData) {
        SysUser loginUser = SysUserHolder.getUser();
        EquipmentStatusLog statusLog = EquipmentConvert.INSTANCE.convertEquipmentStatusLog(statusLogData, loginUser);
        equipmentStatusLogMapper.insert(statusLog);
    }

    @Override
    public void exportOperateLog(EquipmentOperateExportDTO dto) {
        if (Objects.isNull(dto.getAll()) || !dto.getAll()){
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        }
        List<EquipmentOperateLog> equipmentOperateLogList = equipmentOperateLogMapper.selectPageByParam(dto);
        List<EquipmentOperateExportLogVO> exportLogVOS = EquipmentConvert.INSTANCE.convertExportOperateLog(equipmentOperateLogList);
        exportLogVOS.forEach(exportLogVO -> {
            if (StringUtils.equals(EquipmentStatusLogChangeType.MANUAL.getValue(), exportLogVO.getChangeType())){
                exportLogVO.setChangeType(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_USE_LOG_EXPORT_CHANGE_TYPE_MANUAL.getCode(),PlatformResponseCode.EQUIPMENT_USE_LOG_EXPORT_CHANGE_TYPE_MANUAL.getMessage()));
            }else {
                exportLogVO.setChangeType(I18nUtils.getResponseMessage(PlatformResponseCode.EQUIPMENT_USE_LOG_EXPORT_CHANGE_TYPE_AUTO.getCode(),PlatformResponseCode.EQUIPMENT_USE_LOG_EXPORT_CHANGE_TYPE_MANUAL.getMessage()));
            }
        });
        handleOperateLogExport(exportLogVOS);
    }

    @Override
    public void exportStatusLog(EquipmentStatusExportDTO dto) {
        if (Objects.isNull(dto.getAll()) || !dto.getAll()){
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        }
        List<EquipmentStatusLog> equipmentStatusLogList = equipmentStatusLogMapper.selectPageByParam(dto);
        List<EquipmentStatusExportLogVO> exportLogVOS = EquipmentConvert.INSTANCE.convertExportStatusLog(equipmentStatusLogList);
        handleStatusLogExport(exportLogVOS);
    }

    @Nullable
    @Override
    public EquipmentOperateLog selectOperateLogById(Long id) {
        return equipmentOperateLogMapper.selectById(id);
    }

    @Override
    public EquipmentOperateLogVO queryIncompleteFillingLog(Long equipmentId) {
        EquipmentOperateLog log = equipmentOperateLogMapper.selectIncompleteFillingLog(equipmentId);
        return EquipmentConvert.INSTANCE.convert2OperateLogVO(log);
    }

    @Override
    public void fillOperateLog(EquipmentOperateLogData data) {
        EquipmentOperateLog equipmentOperateLog = equipmentOperateLogMapper.selectIncompleteFillingLog(data.getEquipmentId());
        if (Objects.nonNull(equipmentOperateLog) && !Objects.equals(equipmentOperateLog.getId(), data.getOperateLogId())) {
            throw new BmosException(PlatformResponseCode.THERE_IS_LOG_THAT_HAS_NOT_BEEN_FILLED_OUT);
        }
        EquipmentOperateLog log = EquipmentConvert.INSTANCE.convertEquipmentOperateLog(data, SysUserHolder.getUser());
        log.setId(Optional.ofNullable(equipmentOperateLog).orElse(log).getId());
        log.setFillStatus(OperateLogFillingStatusEnum.INCOMPLETE_FILLING);
        equipmentOperateLogMapper.saveOrUpdate(log);
    }

    private void handleOperateLogExport(List<EquipmentOperateExportLogVO> list) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        try {
            assert response != null;
            response.setContentType("application/ms-excel; charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            String fileName=new String("operationLog.xlsx".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            EasyExcel.write(response.getOutputStream(), EquipmentOperateExportLogVO.class).sheet("操作日志").doWrite(list);
        } catch (Exception e) {
            log.error("导出操作日志异常:" + e.getCause() + e.getMessage());
        }
    }

    private void handleStatusLogExport(List<EquipmentStatusExportLogVO> list) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        try {
            assert response != null;
            ExcelWriterUtils.write(EXPORT_NAME, response, Collections.singletonList(new SheetDataBo(EXPORT_SHEET_NAME, EquipmentStatusExportLogVO.class, list, null)));
        } catch (Exception e) {
            log.error("导出操作日志异常:" + e.getCause() + e.getMessage());
        }
    }
}
