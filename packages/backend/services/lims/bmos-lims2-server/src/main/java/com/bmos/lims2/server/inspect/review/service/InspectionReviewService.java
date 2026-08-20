package com.bmos.lims2.server.inspect.review.service;

import com.bmos.lims2.server.inspect.entry.dto.AnalysisItemEntryDTO;
import com.bmos.lims2.server.inspect.entry.dto.AnalysisItemEntryQueryDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryDTO;
import com.bmos.lims2.server.inspect.entry.dto.InspectionOrderEntryQueryDTO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

/**
 * 检验复核服务
 */
public interface InspectionReviewService {

    /**
     * 分析项复核列表（同录入列表，限定待复核）
     */
    CommonPage<AnalysisItemEntryDTO> getAnalysisItemReviewPage(AnalysisItemEntryQueryDTO queryDTO);

    /**
     * 检验单复核列表（同录入列表，限定待复核）
     */
    CommonPage<InspectionOrderEntryDTO> getInspectionOrderReviewPage(InspectionOrderEntryQueryDTO queryDTO);

    /**
     * 批量复核通过/不通过（web端和app端共用）
     * 规则：
     * - 任务必须处于待复核
     * - 复核人不可为录入人
     */
    void batchReview(List<Long> taskIds, String reviewerId, boolean approve, String reason);
}



