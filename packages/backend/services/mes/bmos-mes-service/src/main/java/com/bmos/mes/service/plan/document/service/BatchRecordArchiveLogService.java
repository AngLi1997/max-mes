package com.bmos.mes.service.plan.document.service;

import com.bmos.mes.service.plan.document.controller.vo.ArchiveHistoryVO;
import com.bmos.mes.service.plan.document.service.dto.ArchiveSaveLogDTO;
import com.bmos.mes.service.plan.document.service.dto.BatchRecordArchiveOperateDTO;

import java.util.List;

public interface BatchRecordArchiveLogService {

    /**
     * 保存档案操作历史
     *
     * @param saveLogDTOList
     */
    void saveLog(List<ArchiveSaveLogDTO> saveLogDTOList);
    
    /**
     * 查询批记录操作历史
     * @param dto
     * @return
     */
    List<ArchiveHistoryVO> archiveHistoryList(BatchRecordArchiveOperateDTO dto);
}
