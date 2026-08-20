package com.bmos.mes.service.plan.document.service;

import com.bmos.mes.service.plan.document.controller.vo.PlanEasyVO;
import com.bmos.mes.service.plan.document.controller.vo.TemplateVersionHistoryVO;
import com.bmos.mes.service.plan.document.service.dto.BatchTemplateLogSaveDTO;
import com.bmos.mes.service.plan.document.service.dto.TemplateVersionOperateDTO;

import java.util.List;

/**
 * 批记录模板日志
 */
public interface BatchTemplateLogService {

    /**
     * 新增批记录模板操作日志
     * @param dto
     */
    void saveTemplateLog(BatchTemplateLogSaveDTO dto);


    /**
     * 查询批记录模板版本历史记录
     * @param dto
     * @return
     */
    List<TemplateVersionHistoryVO> templateVersionHistory(TemplateVersionOperateDTO dto);

}
