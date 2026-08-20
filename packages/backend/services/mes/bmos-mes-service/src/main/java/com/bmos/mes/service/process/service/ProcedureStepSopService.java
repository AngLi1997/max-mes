package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.bmos.mes.service.process.model.ProcedureStepSop;

import java.util.List;
import java.util.Set;

/**
 * @author renjinguang
 */
public interface ProcedureStepSopService {

    void saveBatch(List<ProcedureStepSop> sopList);

    List<ProcedureStepSop> queryListByStepModelId(Set<Long> stepIds);

    void updateBatchSops(List<ProcedureStepDTO> stepsInDB);

    void deleteBatchByStepModelIds(List<Long> modelIds);
}
