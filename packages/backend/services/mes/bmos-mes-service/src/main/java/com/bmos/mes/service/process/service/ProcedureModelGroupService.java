package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.model.ProcedureModelGroup;
import com.bmos.mes.service.process.vo.ProcessConfigVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProcedureModelGroupService {
    void saveBatch(List<ProcedureModelGroup> groups);

    void deleteByProcedureModelIds(List<Long> ids);

    Map<Long, List<Long>> getByProcedureModelIds(Set<Long> procedureModelIds);

    List<Long> getByProcedureModelId(Long procedureModelId);

    List<ProcessConfigVO> getDeleteByProcedureModelId(Set<Long> procedureModelIds);
}
