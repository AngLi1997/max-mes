package com.bmos.lims2.server.inspect.scheme.service.impl;

import com.bmos.lims2.server.inspect.scheme.dto.request.ReportDataPointPageReqDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.ReportDataPointRespDTO;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeReportMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeReportService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class InspectionSchemeReportServiceImpl implements InspectionSchemeReportService {

    @Resource
    private InspectionSchemeReportMapper inspectionSchemeReportMapper;

    @Override
    public CommonPage<ReportDataPointRespDTO> pageReportDataPoints(ReportDataPointPageReqDTO reqDTO) {
        PageHelper.startPage(reqDTO.getPageNum(), reqDTO.getPageSize());
        List<ReportDataPointRespDTO> list = inspectionSchemeReportMapper.pageReportDataPoints(
                reqDTO.getSchemeId(),
                reqDTO.getInspectItemId(),
                reqDTO.getParameterId()
        );
        // 生成占位符索引（不包含方案信息，默认使用生效版本）
        for (ReportDataPointRespDTO dto : list) {
            String index = "${ITEM:" + dto.getInspectItemCode() + "|PARAM:" + dto.getParameterCode() + "|POINT:" + dto.getDataPointName() + "}";
            dto.setIndexPlaceholder(index);
        }
        return CommonPage.convertPage(list);
    }
}


