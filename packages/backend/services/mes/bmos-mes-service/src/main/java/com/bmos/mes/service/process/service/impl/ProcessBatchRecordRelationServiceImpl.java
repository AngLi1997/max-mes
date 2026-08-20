package com.bmos.mes.service.process.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.convert.ProcessBatchRecordRelationConverter;
import com.bmos.mes.service.process.dto.modify.ProcessModifyDTO;
import com.bmos.mes.service.process.mapper.ProcessBatchRecordRelationMapper;
import com.bmos.mes.service.process.model.ProcessBatchRecordRelation;
import com.bmos.mes.service.process.service.ProcessBatchRecordRelationService;
import com.bmos.mes.service.record.enums.RecordStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcessBatchRecordRelationServiceImpl implements ProcessBatchRecordRelationService {

    @Autowired
    private ProcessBatchRecordRelationMapper processBatchRecordRelationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<ProcessBatchRecordRelation> relations) {
        processBatchRecordRelationMapper.insertBatch(relations);
    }

    @Override
    public List<ProcessBatchRecordRelation> getListByProcessVersionId(Long processVersionId) {
        return processBatchRecordRelationMapper.selectListByProcessVersionId(processVersionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyBatch(ProcessModifyDTO dto) {
        processBatchRecordRelationMapper.deleteByProcessVersion(dto.getId());
        List<ProcessBatchRecordRelation> relations = ProcessBatchRecordRelationConverter.INSTANCE.convertList(dto);
        processBatchRecordRelationMapper.insertBatch(relations);
    }

    @Override
    public void validateBatchRecord(Long processVersionId) {
        Long count = processBatchRecordRelationMapper.existNonEnableBatchRecord(processVersionId,
                        Integer.valueOf(RecordStateEnum.CERTAIN.getValue()));
        if (count > 0) {
            throw new BmosException(MesResponseCode.PROCESS_RELATION_RECORD_NON_CONFIRM);
        }
    }

    @Override
    public List<ProcessBatchRecordRelation> selectByProcessVersionIdList(List<Long> processVersionIdList) {
        return processBatchRecordRelationMapper.selectByProcessVersionIdList(processVersionIdList);
    }

    @Override
    public List<Long> getByRecordVersionIds(List<Long> batchRecordVersionIds) {
        return processBatchRecordRelationMapper.getByRecordVersionIds(batchRecordVersionIds);
    }


}
