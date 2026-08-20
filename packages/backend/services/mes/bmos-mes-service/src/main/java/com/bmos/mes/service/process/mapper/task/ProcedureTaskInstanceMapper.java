package com.bmos.mes.service.process.mapper.task;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.common.constant.ProcessConstant;
import com.bmos.mes.common.enums.plan.ProductTaskStatusEnum;
import com.bmos.mes.service.plan.info.dto.PlanAuditProgressDetailQueryDTO;
import com.bmos.mes.service.plan.info.vo.PlanAuditProgressDetailVO;
import com.bmos.mes.service.plan.info.vo.PlanAuditingCountVO;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstance;
import com.bmos.mes.service.workflow.change.dto.ChangeTeamDTO;
import com.bmos.mes.service.workflow.dto.ProcedureRestartDTO;
import com.bmos.mes.service.workflow.dto.query.WorkFlowProcedureStepDTO;
import com.bmos.mes.service.workflow.dto.query.WorkFlowStepProgressDTO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProcedureTaskInstanceMapper extends BaseMapperX<ProcedureTaskInstance> {


    default List<ProcedureTaskInstance> selectByIds(List<Long> taskIdList) {
        return selectList(new LambdaQueryWrapperX<ProcedureTaskInstance>()
                .in(ProcedureTaskInstance::getId, taskIdList));
    }

    default List<ProcedureTaskInstance> selectListByPlanIdAndStepModel(Long planId, List<Long> modelId,Long procedureModelId) {
        return selectList(new LambdaQueryWrapperX<ProcedureTaskInstance>()
                .eq(ProcedureTaskInstance::getPlanId, planId)
                .eq(ProcedureTaskInstance::getProcedureModelId,procedureModelId)
                .in(ProcedureTaskInstance::getProcedureStepModelId, modelId));
    }

    default List<ProcedureTaskInstance> queryByProcedureModelIdAndPlanId(ProcedureRestartDTO dto) {
        return selectList(new LambdaQueryWrapperX<ProcedureTaskInstance>()
                .eq(ProcedureTaskInstance::getProcedureModelId, dto.getProcedureModelId())
                .eq(ProcedureTaskInstance::getPlanId,dto.getPlanId())
                .eqIfPresent(ProcedureTaskInstance::getProcessChangeNumber,dto.getProcessChangeNumber())
                .eqIfPresent(ProcedureTaskInstance::getProcedureChangeNumber,dto.getProcedureChangeNumber())
                .orderByDesc(ProcedureTaskInstance::getProcedureChangeNumber));
    }

    default void deleteInstanceByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        Db.removeByIds(ids, ProcedureTaskInstance.class);
    }

    default Boolean saveOrUpdateEntity(ProcedureTaskInstance instance) {
        return Db.saveOrUpdate(instance);
    }

    default List<ProcedureTaskInstance> selectListByPlanId(Long id) {
        return selectList(new LambdaQueryWrapperX<ProcedureTaskInstance>()
                .eq(ProcedureTaskInstance::getPlanId, id));
    }

    List<ProcedureTaskInstance> getTaskTodoPage(@Param("planIds") List<Long> planIds);


    default List<ProcedureTaskInstance> selectTaskNotCompleteByTaskIds( Long planId) {
        LambdaQueryWrapper<ProcedureTaskInstance> ql = new LambdaQueryWrapperX<ProcedureTaskInstance>()
                .eq(planId != null, ProcedureTaskInstance::getPlanId, planId)
                .in(ProcedureTaskInstance::getFlowState, "DISABLE", "ENABLE");
        return selectList(ql);
    }

    default List<ProcedureTaskInstance> queryByProcessInstanceId(String processInstanceId){
        return selectList(new LambdaQueryWrapperX<ProcedureTaskInstance>()
                .eq(ProcedureTaskInstance::getProcessInstanceId,processInstanceId));
    }

    default void updateActivatedTask(List<ProcedureTaskInstance> activatedTask){
        if (CollUtil.isEmpty(activatedTask)){
            return;
        }
        activatedTask.forEach(task-> {
            task.setCompleteTime(LocalDateTime.now());
            task.setFlowState(ProductTaskStatusEnum.COMPLETE.getValue());
            task.setType(ProcessConstant.IS_END);
        });
        saveOrUpdateBatch(activatedTask);
    }

    default List<ProcedureTaskInstance> selectChangeListByPlanIdAndStepModeId(WorkFlowProcedureStepDTO stepDTO,
                                                                              List<Long> modelIds){
        LambdaQueryWrapperX<ProcedureTaskInstance> wrapper = new LambdaQueryWrapperX<ProcedureTaskInstance>()
                .eq(ProcedureTaskInstance::getPlanId, stepDTO.getPlanId())
                .eq(ProcedureTaskInstance::getProcedureModelId, stepDTO.getProcedureModelId())
                .eq(ProcedureTaskInstance::getProcedureChangeNumber, stepDTO.getProcedureChangeNumber())
                .eq(ProcedureTaskInstance::getProcessChangeNumber, stepDTO.getProcessChangeNumber())
                .in(ProcedureTaskInstance::getProcedureStepModelId, modelIds);
        return selectList(wrapper);
    }

    List<ProcedureTaskInstance> listTaskProgress(@Param("dto") WorkFlowStepProgressDTO dto);

    List<PlanAuditProgressDetailVO> selectAuditProgressDetailVO(PlanAuditProgressDetailQueryDTO dto);

    List<PlanAuditingCountVO> selectAuditingCount(@Param("ids") List<Long> ids);

    default List<ProcedureTaskInstance> queryChangeTeamListByStepModelIdAndPlanId(Long procedureStepModelId, Long planId){
        return selectList(new LambdaQueryWrapperX<ProcedureTaskInstance>()
                .eq(ProcedureTaskInstance::getPlanId,planId)
                .eq(ProcedureTaskInstance::getProcedureStepModelId,procedureStepModelId)
                .orderByDesc(ProcedureTaskInstance::getStartTime));
    }

    default List<ProcedureTaskInstance> selectTask(ChangeTeamDTO teamDTO){
        return selectList(new LambdaQueryWrapperX<ProcedureTaskInstance>()
                .eq(ProcedureTaskInstance::getPlanId,teamDTO.getPlanId())
                .eq(ProcedureTaskInstance::getProcedureStepModelId,teamDTO.getProcedureStepModelId())
                .eq(ProcedureTaskInstance::getProcedureChangeNumber,teamDTO.getProcedureChangeNumber())
                .eq(ProcedureTaskInstance::getProcessChangeNumber,teamDTO.getProcessChangeNumber()));
    }

    List<ProcedureTaskInstance> queryCompleteTaskByPlanId(@Param("planId") Long planId,@Param("flowState") String flowState);

    default List<ProcedureTaskInstance> selectListByModelIdAndProcedureId(List<Long> stepModelId, Long procedureModelId,
                                                                          Integer procedureChangeNumber, Integer processChangeNumber,
                                                                          Long planId){
        return selectList(new LambdaQueryWrapperX<ProcedureTaskInstance>()
                .in(ProcedureTaskInstance::getProcedureStepModelId,stepModelId)
                .eq(ProcedureTaskInstance::getProcedureModelId,procedureModelId)
                .eq(ProcedureTaskInstance::getProcedureChangeNumber,procedureChangeNumber)
                .eq(ProcedureTaskInstance::getProcessChangeNumber,processChangeNumber)
                .eq(ProcedureTaskInstance::getPlanId,planId));

    }
}
