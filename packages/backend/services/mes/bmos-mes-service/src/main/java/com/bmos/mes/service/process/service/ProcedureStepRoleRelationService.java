package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.model.ProcedureStepRole;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;

@Validated
public interface ProcedureStepRoleRelationService {
    void saveBatch(List<ProcedureStepRole> roles);

    List<ProcedureStepRole> getListByProcedureStepIds(Set<Long> procedureStepIds);

    void deleteByProcedureStepIds(List<Long> convertList);
}
