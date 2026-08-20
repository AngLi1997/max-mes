package com.bmos.mes.service.log.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.exporter.ExcelWriterUtils;
import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.log.convert.MesLogConvert;
import com.bmos.mes.service.log.dto.ExportOperationLogDTO;
import com.bmos.mes.service.log.dto.OperationLogDetailDTO;
import com.bmos.mes.service.log.dto.QueryLogPageDTO;
import com.bmos.mes.service.log.mapper.MesOperationLogMapper;
import com.bmos.mes.service.log.model.MesLogModel;
import com.bmos.mes.service.log.mq.OperationLogTopic;
import com.bmos.mes.service.log.service.MesOperationLogService;
import com.bmos.mes.service.log.vo.MesLogDetailVO;
import com.bmos.mes.service.log.vo.MesLogPageVO;
import com.bmos.mes.service.log.vo.OperationLogExcelVO;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.menu.feign.MenuFeign;
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
public class MesOperationLogServiceImpl implements MesOperationLogService {

    @Autowired
    private MesOperationLogMapper mapper;

    @Autowired
    MenuFeign menuFeign;

    @Autowired
    OperationLogTopic operationLogTopic;

    @Override
    public CommonPage<MesLogPageVO> getPage(QueryLogPageDTO dto) {
        dto.convert2Date();
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        return CommonPage.convertPage(mapper.selectPageList(dto));
    }

    @Override
    public void exportOperationLog(ExportOperationLogDTO dto) {
        dto.convert2Date();
        if (CollUtil.isNotEmpty(dto.getSelectIds())) {
            List<OperationLogExcelVO> operationLogModels = mapper.selectByIds(dto.getSelectIds(), dto.getStartTimeDate(), dto.getEndTimeDate());
            handleExport(operationLogModels);
            return;
        }
        List<OperationLogExcelVO> list = mapper.selectExportData(dto);
        handleExport(list);
    }

    @Override
    public MesLogDetailVO getDetail(OperationLogDetailDTO dto) {
        MesLogModel mesLogModel = mapper.selectDetail(dto);
        if (Objects.isNull(mesLogModel)){
            throw new BmosException(MesResponseCode.LOG_NOT_EXIST);
        }
        return MesLogConvert.INSTANCE.convert2DetailVO(mesLogModel);
    }

    private void handleExport(List<OperationLogExcelVO> list) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        assert response != null;
        try {
            ExcelWriterUtils.write("操作日志", response, Collections.singletonList(new SheetDataBo("操作日志", OperationLogExcelVO.class, list, null)));
        } catch (Exception e) {
            log.error("操作日志导出失败", e);
        }
    }

    @Override
    public void save(MesLogModel logModel) {
        // 插入本地数据库防止数据丢失
        try {
            mapper.insert(logModel);
        } catch (Exception e){
            log.error("日志落库失败 {}", logModel, e);
        }

        // 发送异步消息
        operationLogTopic.product(logModel);
    }
}

