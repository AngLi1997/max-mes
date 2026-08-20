package com.bmos.mes.service.audit.service;

import com.bmos.mes.service.audit.vo.FlowAuditCategoryVO;

import java.util.List;

public interface FlowAuditCategoryService {


    List<FlowAuditCategoryVO> listFlowAuditCategory();

    List<FlowAuditCategoryVO> flowAuditHistoryCategory();

    List<String> queryByCode(String categoryCode);
}
