package com.bmos.mes.service.trace.material.service;

import com.bmos.mes.service.output.finished.model.FinishedProductOutput;
import com.bmos.mes.service.output.finished.model.FinishedProductOutputResult;
import com.bmos.mes.service.trace.material.dto.MaterialTraceHistoryDTO;

import java.util.Collection;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/21 15:00
 */
public interface IMaterialTraceHistoryService {

    void saveTraceHistory(MaterialTraceHistoryDTO historyTrace);

    void saveTraceHistory(Collection<MaterialTraceHistoryDTO> historyTraces);

    void saveTraceHistory(Long procedureStepModelId, FinishedProductOutput finishedProductOutput, List<FinishedProductOutputResult> finishedProductOutputResults);
}
