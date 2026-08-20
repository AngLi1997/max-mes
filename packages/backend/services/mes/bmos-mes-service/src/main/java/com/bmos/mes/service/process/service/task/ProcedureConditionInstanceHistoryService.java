package com.bmos.mes.service.process.service.task;


import java.util.List;

public interface ProcedureConditionInstanceHistoryService {


    void saveConditionHistory(List<Long> stepModeId,Long planId,List<Long> procedureModelId);
}
