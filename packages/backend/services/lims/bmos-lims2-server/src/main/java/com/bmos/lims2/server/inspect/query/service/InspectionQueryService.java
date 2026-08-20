package com.bmos.lims2.server.inspect.query.service;

import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderPageQueryDTO;
import com.bmos.lims2.server.inspect.entry.dto.EntryByItemQueryDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectItemTabDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO;
import com.bmos.lims2.server.inspect.entry.dto.EntryRecordsGroupedByAnalysisItemDTO;
import com.bmos.lims2.server.inspect.sample.ledger.entity.SampleLedger;
import com.bmos.lims2.server.inspect.sample.ledger.dto.SampleLedgerListDTO;
import com.bmos.lims2.server.inspect.sample.ledger.dto.SampleLedgerPageQueryDTO;
import com.bmos.lims2.server.report.dto.ReportGeneratedItemDTO;

import java.util.List;

/**
 * @Description: 检验查询聚合服务
 * @Author: yigaohui
 * @Date: 2025/09/05 10:30
 */
public interface InspectionQueryService {

    com.bmos.mybatis.page.CommonPage<com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO> page(InspectionOrderPageQueryDTO queryDTO);



    com.bmos.mybatis.page.CommonPage<SampleLedgerListDTO> sampleLedgerPage(SampleLedgerPageQueryDTO queryDTO);


    /**
     * 获取检验单信息与标志位
     */
    OrderInfoDTO getOrderInfo(Long orderId);


    /**
     * 按检验单查询已生成的报告（投影项，包含模板/版本等字段）
     */
    java.util.List<ReportGeneratedItemDTO> listGeneratedReportsByOrderId(Long orderId);

    /**
     * 检验单样品列表（详情页用）
     */
    java.util.List<OrderSampleDTO> listOrderSamples(Long orderId);

    /**
     * 获取检验项目分页签
     */
    java.util.List<InspectItemTabDTO> listInspectItemTabs(Long orderId);

    /**
     * 按检验项目查询录入记录，并按分析项分组（不分页）
     */
    java.util.List<EntryRecordsGroupedByAnalysisItemDTO> listEntriesByItem(EntryByItemQueryDTO queryDTO);
}


