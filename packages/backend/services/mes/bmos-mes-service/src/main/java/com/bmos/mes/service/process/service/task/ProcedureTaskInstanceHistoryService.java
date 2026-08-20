package com.bmos.mes.service.process.service.task;

import com.bmos.mes.service.plan.info.dto.PlanRetraceInfoPageDTO;
import com.bmos.mes.service.plan.info.vo.PlanRetraceExecutePageVO;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstance;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstanceHistory;
import com.bmos.mes.service.workflow.dto.query.PlanSubRecordQueryDTO;
import com.bmos.mes.service.workflow.dto.query.WorkFlowProcedureStepDTO;
import com.bmos.mes.service.workflow.vo.PlanSubRecordVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface ProcedureTaskInstanceHistoryService {


    void save(List<ProcedureTaskInstance> taskInstancesHistory);

    List<ProcedureTaskInstanceHistory> selectHistoryTask(WorkFlowProcedureStepDTO stepDTO);

    /**
     * 查询任务实例中的辅助记录
     * @param dto
     * @return
     */
    List<PlanSubRecordVO> queryPlanSubRecord(PlanSubRecordQueryDTO dto);

    ProcedureTaskInstanceHistory selectHistoryTaskById(String taskInstanceId);
    List<ProcedureTaskInstanceHistory> queryListPlanIdAndCompleteState(Long planId,String state);

    CommonPage<PlanRetraceExecutePageVO> executeTracePage(PlanRetraceInfoPageDTO dto);
}
