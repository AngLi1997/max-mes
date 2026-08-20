package com.bmos.mes.service.process.service;

import com.bmos.mes.common.enums.process.StepTaskTypeEnum;
import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.bmos.mes.service.process.dto.ProcedureStepValidateDTO;
import com.bmos.mes.service.process.dto.query.ProcedureStepHistoricQueryDTO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStep;

import java.util.Collection;
import java.util.List;

public interface ProcedureStepService {
    List<ProcedureStep> saveBatch(ProcedureModel procedureModel, List<ProcedureStepDTO> procedureStepDTOS,
                                  StepTaskTypeEnum step);

    List<ProcedureStep> getHistoricList(ProcedureStepHistoricQueryDTO dto);

    ProcedureStep getById(Long id);

    void saveBatch(List<ProcedureStep> steps);

    List<ProcedureStep> getByIds(Collection<Long> ids);

    /**
     * 校验历史工步名称是否存在
     * @param dto
     * @return
     */
    Boolean validateProcedureStepName(ProcedureStepValidateDTO dto);
}
