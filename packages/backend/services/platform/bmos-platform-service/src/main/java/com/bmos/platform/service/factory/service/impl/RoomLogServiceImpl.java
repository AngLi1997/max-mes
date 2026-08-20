package com.bmos.platform.service.factory.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.factory.dto.BatchRoomCleanPageDTO;
import com.bmos.platform.facade.factory.vo.BatchRoomCleanInfoVO;
import com.bmos.platform.facade.factory.vo.RoomCleanInfoFeignVO;
import com.bmos.platform.service.factory.controller.vo.RoomLogExportVO;
import com.bmos.platform.service.factory.controller.vo.RoomLogPageVO;
import com.bmos.platform.service.factory.convert.RoomLogConverter;
import com.bmos.platform.service.factory.mapper.FactoryRoomLogMapper;
import com.bmos.platform.service.factory.model.FactoryCleanRoomLog;
import com.bmos.platform.service.factory.service.RoomLogService;
import com.bmos.platform.service.factory.service.dto.RoomLogPageDTO;
import com.bmos.platform.service.factory.service.dto.RoomStatusLogExportDTO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RoomLogServiceImpl implements RoomLogService {

    @Autowired
    private FactoryRoomLogMapper factoryRoomLogMapper;

    private final String EXPORT_NAME = "房间清场日志";

    private final String EXPORT_SHEET_NAME = "房间清场日志";

    @Override
    public CommonPage<RoomLogPageVO> cleanLogPage(RoomLogPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<FactoryCleanRoomLog> roomLogs = factoryRoomLogMapper.selectByParam(dto);
        return CommonPage.convertPage(roomLogs, RoomLogConverter.INSTANCE::convert2PageVO);
    }

    @Override
    public void exportLog(RoomStatusLogExportDTO dto) {
        if (Objects.isNull(dto.getAll()) ||  !dto.getAll()){
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        }
        List<FactoryCleanRoomLog> roomLogs = factoryRoomLogMapper.selectByParam(dto);
        List<RoomLogExportVO> roomLogExportVOS = RoomLogConverter.INSTANCE.convert2ExportVO(roomLogs);
        handlerExport(roomLogExportVOS);
    }

    @Override
    public CommonPage<BatchRoomCleanInfoVO> getRoomCleanInfoPage(BatchRoomCleanPageDTO dto) {
        RoomLogPageDTO roomLogPageDTO = new RoomLogPageDTO();
        roomLogPageDTO.setBatchNo(dto.getBatchNo());
        if (StrUtil.isEmpty(dto.getOrderSql())){
            roomLogPageDTO.setOrderSql("beginTime asc");
        } else {
            roomLogPageDTO.setOrderSql(dto.getOrderSql());
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), roomLogPageDTO.getOrderSql());
        List<FactoryCleanRoomLog> factoryCleanRoomLogs = factoryRoomLogMapper.selectByParam(roomLogPageDTO);
        return CommonPage.convertPage(factoryCleanRoomLogs, RoomLogConverter.INSTANCE::convert2RoomCleanInfoFeignVO);
    }

    private void handlerExport(List<RoomLogExportVO> roomLogExportVOS) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        try {
            assert response != null;
            ExcelWriterUtils.write(EXPORT_NAME, response, Collections.singletonList(new SheetDataBo<>(EXPORT_SHEET_NAME, RoomLogExportVO.class, roomLogExportVOS, null)));
        } catch (Exception e) {
            log.error("导出操作日志异常:" + e.getCause() + e.getMessage());
        }
    }
}
