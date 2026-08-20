package com.bmos.lims2.server.audit;


import com.bmos.lims2.server.audit.vo.FlowAuditCategoryVO;

import java.util.List;

public interface FlowAuditCategoryService {


    List<FlowAuditCategoryVO> listFlowAuditCategory();

    List<FlowAuditCategoryVO> flowAuditHistoryCategory();

    List<String> queryByCode(String categoryCode);
}
