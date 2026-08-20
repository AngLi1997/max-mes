package com.bmos.platform.service.log.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.log.convert.PlatformLogConvert;
import com.bmos.platform.service.log.dto.ExportOperationLogDTO;
import com.bmos.platform.service.log.dto.OperationLogDetailDTO;
import com.bmos.platform.service.log.dto.QueryOperationLogPageDTO;
import com.bmos.platform.service.log.mapper.OperationLogMapper;
import com.bmos.platform.service.log.model.OperationLogModel;
import com.bmos.platform.service.log.mq.OperationLogTopic;
import com.bmos.platform.service.log.service.PlatformLogService;
import com.bmos.platform.service.log.vo.OperationLogDetailVO;
import com.bmos.platform.service.log.vo.OperationLogExcelVO;
import com.bmos.platform.service.log.vo.OperationLogPageVO;
import com.bmos.platform.service.system.menu.service.MenuService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlatformLogServiceImpl implements PlatformLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    MenuService menuService;

    private final String EXPORT_NAME = "操作日志";
    private final String EXPORT_SHEET_NAME = "操作日志";

    @Override
    public void save(OperationLogModel logModel) {
        // 插入本地数据库防止数据丢失
        try {
            operationLogMapper.insert(logModel);
        } catch (Exception e){
            log.error("日志落库失败 {}", logModel, e);
        }
    }

    @Override
    public CommonPage<OperationLogPageVO> getOperationLogPage(QueryOperationLogPageDTO dto) {
        dto.convert2Date();
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        return CommonPage.convertPage(operationLogMapper.selectPageList(dto));
    }

    @Override
    public void exportOperationLog(ExportOperationLogDTO dto) {
        if (CollUtil.isNotEmpty(dto.getSelectIds())) {
            List<OperationLogExcelVO> vos = operationLogMapper.selectByIds(dto.getSelectIds());
            handleExport(vos);
            return;
        }
        List<OperationLogExcelVO> list = operationLogMapper.selectExportData(dto);
        handleExport(list);
    }

    @Override
    public OperationLogDetailVO getOperationLogDetailInfo(OperationLogDetailDTO dto) {
        OperationLogModel operationLogDetailInfo = operationLogMapper.getOperationLogDetailInfo(dto);
        return PlatformLogConvert.INSTANCE.convert2DetailInfoVO(operationLogDetailInfo);
    }

    private void handleExport(List<OperationLogExcelVO> list) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        try {
            assert response != null;
            ExcelWriterUtils.write(EXPORT_NAME, response, Collections.singletonList(new SheetDataBo(EXPORT_SHEET_NAME, OperationLogExcelVO.class, list, null)));
        } catch (Exception e) {
            log.error("导出操作日志异常:" + e.getCause() + e.getMessage());
        }
    }
}
