package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.model.ProcessRelationMaterial;

import java.util.List;
import java.util.Set;

public interface ProcessRelationMaterialService {
    void saveBatch(List<ProcessRelationMaterial> relationMaterials);

    List<ProcessRelationMaterial> getListByProcessRelationIds(Set<Long> processRelationIds);

    void deleteByProcessId(Long processId);
}
