package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.dto.modify.ProcessModifyDTO;
import com.bmos.mes.service.process.model.ProcessBatchRecordRelation;

import java.util.List;

public interface ProcessBatchRecordRelationService {
    void saveBatch(List<ProcessBatchRecordRelation> relations);

    List<ProcessBatchRecordRelation> getListByProcessVersionId(Long processVersionId);

    void modifyBatch(ProcessModifyDTO dto);

    void validateBatchRecord(Long processVersionId);

    /**
     * 根据工艺版本id + 记录项id查询关联关系
     * @param processVersionIdList
     * @return
     */
    List<ProcessBatchRecordRelation> selectByProcessVersionIdList(List<Long> processVersionIdList);

    /**
     * 根据记录版本id查询关联关系
     * @param batchRecordVersionIds
     * @return
     */
    List<Long> getByRecordVersionIds(List<Long> batchRecordVersionIds);
}
