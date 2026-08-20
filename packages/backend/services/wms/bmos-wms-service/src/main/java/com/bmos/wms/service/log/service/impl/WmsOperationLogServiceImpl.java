package com.bmos.wms.service.log.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.log.convert.WmsLogConvert;
import com.bmos.wms.service.log.dto.ExportOperationLogDTO;
import com.bmos.wms.service.log.dto.QueryLogPageDTO;
import com.bmos.wms.service.log.mapper.WmsOperationLogMapper;
import com.bmos.wms.service.log.model.WmsLogModel;
import com.bmos.wms.service.log.service.WmsOperationLogService;
import com.bmos.wms.service.log.vo.WmsLogDetailVO;
import com.bmos.wms.service.log.vo.WmsLogPageVO;
import com.bmos.wms.service.log.vo.OperationLogExcelVO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class WmsOperationLogServiceImpl implements WmsOperationLogService {
    @Autowired
    private WmsOperationLogMapper mapper;

    @Override
    public CommonPage<WmsLogPageVO> getPage(QueryLogPageDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        return CommonPage.convertPage(mapper.selectPageList(dto));
    }

    @Override
    public void exportOperationLog(ExportOperationLogDTO dto) throws IOException {
        if (CollUtil.isNotEmpty(dto.getSelectIds())) {
            List<OperationLogExcelVO> operationLogModels = mapper.selectByIds(dto.getSelectIds());
            handleExport(operationLogModels);
            return;
        }
        List<OperationLogExcelVO> list = mapper.selectExportData(dto);
        handleExport(list);
    }

    @Override
    public WmsLogDetailVO getDetail(Long id) {
        return WmsLogConvert.INSTANCE.convert2Detail(mapper.selectById(id));
    }

    private void handleExport(List<OperationLogExcelVO> list) throws IOException {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        assert response != null;
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;fileName=operationLog.xlsx");
        EasyExcel.write(response.getOutputStream(), OperationLogExcelVO.class).sheet("操作日志").doWrite(list);
    }

    @Override
    public void save(WmsLogModel logModel) {
        mapper.insert(logModel);
    }
}
