package com.bmos.lims2.server.inspect.scheme.service;

import com.bmos.lims2.server.inspect.scheme.dto.request.ReportDataPointPageReqDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.ReportDataPointRespDTO;
import com.bmos.mybatis.page.CommonPage;

/**
 * 报告数据点查询服务
 */
public interface InspectionSchemeReportService {

    /**
     * 分页查询报告数据点（基于生效版本，过滤报告项和报告显示的数据点）
     */
    CommonPage<ReportDataPointRespDTO> pageReportDataPoints(ReportDataPointPageReqDTO reqDTO);
}


