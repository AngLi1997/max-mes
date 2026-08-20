package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.model.ProcessRelation;

import java.util.List;

public interface ProcessRelationService {
    void saveBatch(List<ProcessRelation> relations);


    List<ProcessRelation> getListByProcessId(Long processId);

    void deleteByProcessId(Long processId);

    List<ProcessRelation> getList();

}
