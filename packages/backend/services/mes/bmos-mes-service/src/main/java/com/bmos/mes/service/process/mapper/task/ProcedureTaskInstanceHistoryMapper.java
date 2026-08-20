package com.bmos.mes.service.process.mapper.task;

import com.bmos.mes.common.enums.plan.ProductTaskStatusEnum;
import com.bmos.mes.service.plan.info.dto.PlanAuditProgressDetailQueryDTO;
import com.bmos.mes.service.plan.info.vo.PlanAuditProgressDetailVO;
import com.bmos.mes.service.plan.info.vo.PlanAuditingCountVO;
import com.bmos.mes.service.plan.info.vo.PlanRetraceExecutePageVO;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstanceHistory;
import com.bmos.mes.service.workflow.dto.query.PlanSubRecordQueryDTO;
import com.bmos.mes.service.workflow.dto.query.WorkFlowProcedureStepDTO;
import com.bmos.mes.service.workflow.vo.PlanSubRecordVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcedureTaskInstanceHistoryMapper extends BaseMapperX<ProcedureTaskInstanceHistory> {


    default List<ProcedureTaskInstanceHistory> selectHistoryTask(WorkFlowProcedureStepDTO stepDTO) {
        LambdaQueryWrapperX<ProcedureTaskInstanceHistory> wrapper = new LambdaQueryWrapperX<ProcedureTaskInstanceHistory>()
                .eq(ProcedureTaskInstanceHistory::getPlanId, stepDTO.getPlanId())
                .eq(ProcedureTaskInstanceHistory::getProcedureChangeNumber, stepDTO.getProcedureChangeNumber())
                .eq(ProcedureTaskInstanceHistory::getProcessChangeNumber, stepDTO.getProcessChangeNumber())
                .eq(ProcedureTaskInstanceHistory::getFlowState, ProductTaskStatusEnum.COMPLETE.getValue())
                .eq(ProcedureTaskInstanceHistory::getProcedureModelId, stepDTO.getProcedureModelId());
       return selectList(wrapper);
    }

    List<PlanAuditProgressDetailVO> selectAuditProgressDetailVO(PlanAuditProgressDetailQueryDTO dto);

    List<PlanAuditingCountVO> selectAuditingCount(@Param("ids") List<Long> ids);

    List<PlanSubRecordVO> selectPlanSubRecord(PlanSubRecordQueryDTO dto);

    List<ProcedureTaskInstanceHistory> queryListPlanIdAndCompleteState(@Param("planId") Long planId,@Param("state")String state);

    List<PlanRetraceExecutePageVO> queryListByPlanId(@Param("planId") Long planId);

    default List<ProcedureTaskInstanceHistory> selectByProductPlanId(Long id){
        return selectList(new LambdaQueryWrapperX<ProcedureTaskInstanceHistory>()
                .eq(ProcedureTaskInstanceHistory::getPlanId, id));
    }
}
