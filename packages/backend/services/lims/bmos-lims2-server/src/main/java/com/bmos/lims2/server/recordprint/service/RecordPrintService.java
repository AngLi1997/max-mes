package com.bmos.lims2.server.recordprint.service;

import com.bmos.lims2.server.recordprint.dto.PrintItemReqDTO;
import com.bmos.lims2.server.recordprint.dto.PrintableAnalysisItemDTO;
import com.bmos.lims2.server.recordprint.dto.RecordPrintPageReqDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;

import java.util.List;

/**
 * @Description: 记录打印服务
 * @Author: yigaohui
 * @Date: 2025/11/25 10:20
 */
public interface RecordPrintService {

    /**
     * 可打印检验单分页查询（已确认 + 存在复核通过任务）
     * @param reqDTO 查询参数
     * @return 分页数据（外层Controller封装为BasePage）
     */
    com.bmos.mybatis.page.CommonPage<InspectionOrderDTO> pagePrintableInspections(RecordPrintPageReqDTO reqDTO);

    /**
     * 查询检验单下可打印（复核通过）的分析项，按编码排序
     * @param inspectionId 检验单ID
     * @return 可打印分析项列表
     */
    List<PrintableAnalysisItemDTO> listPrintableAnalysisItems(Long inspectionId);

    /**
     * 单个分析项PDF预览（带水印）
     * @param taskId 任务ID
     * @return PDF字节数组
     */
    byte[] previewAnalysisPdf(Long taskId);

    /**
     * 合并打印（多个分析项）生成完整PDF
     * @param inspectionId 检验单ID
     * @param items 打印项（顺序即打印顺序）
     * @return 合并后的PDF字节数组
     */
    byte[] mergePrintPdf(Long inspectionId, List<PrintItemReqDTO> items);
}


