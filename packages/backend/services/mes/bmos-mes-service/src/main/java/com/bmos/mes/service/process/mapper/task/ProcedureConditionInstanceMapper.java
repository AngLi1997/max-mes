package com.bmos.mes.service.process.mapper.task;

import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcedureConditionInstanceMapper extends BaseMapperX<ProcedureConditionInstance> {


    default List<ProcedureConditionInstance> selectByExpressionIdsAndResult(List<Long> expressionIds,Boolean result,Long planId) {
        return selectList(new LambdaQueryWrapperX<ProcedureConditionInstance>()
                .in(ProcedureConditionInstance::getExpressionId, expressionIds)
                .eq(ProcedureConditionInstance::getTaskResult,result)
                .eq(ProcedureConditionInstance::getPlanId,planId));
    }

    default List<ProcedureConditionInstance> selectByIds(List<Long> ids) {
        return selectList(new LambdaQueryWrapperX<ProcedureConditionInstance>()
                .in(ProcedureConditionInstance::getId, ids));
    }

    default List<ProcedureConditionInstance> queryByExpressionId(Long id,Long planId){
        return selectList(new LambdaQueryWrapperX<ProcedureConditionInstance>()
                .eq(ProcedureConditionInstance::getExpressionId,id)
                .eq(ProcedureConditionInstance::getPlanId,planId));
    }

    default List<ProcedureConditionInstance> queryByExpressionIds(List<Long> expressionIds,Long planId){
        return selectList(new LambdaQueryWrapperX<ProcedureConditionInstance>()
                .in(ProcedureConditionInstance::getExpressionId,expressionIds)
                .eq(ProcedureConditionInstance::getPlanId,planId));
    }

    default List<ProcedureConditionInstance> selectByPlanId(Long id){
        return selectList(new LambdaQueryWrapperX<ProcedureConditionInstance>()
                .eq(ProcedureConditionInstance::getPlanId,id));
    }

    default List<ProcedureConditionInstance> selectByTypeAndResult(List<String> type, Boolean result){
        return selectList(new LambdaQueryWrapperX<ProcedureConditionInstance>()
                .eq(ProcedureConditionInstance::getTaskResult,result)
                .in(ProcedureConditionInstance::getConditionType,type));
    }

    default List<ProcedureConditionInstance> selectByExpressionIds(List<Long> expressionIdS,Long planId){
        return selectList(new LambdaQueryWrapperX<ProcedureConditionInstance>()
                .eq(planId !=null ,ProcedureConditionInstance::getPlanId,planId)
                .in(ProcedureConditionInstance::getExpressionId,expressionIdS));
    }

    default List<ProcedureConditionInstance> querybYExpressionIdAndConditionTypeAndPlanId(Long id, String value, Long planId){
        return selectList(new LambdaQueryWrapperX<ProcedureConditionInstance>()
                .eq(ProcedureConditionInstance::getExpressionId,id)
                .eq(ProcedureConditionInstance::getConditionType,value)
                .eq(ProcedureConditionInstance::getPlanId,planId));
    }

    List<ProcedureConditionInstance> selectConditionList(@Param("conditionType") String conditionType,
                                                                @Param("planId") Long planId);

    List<ProcedureConditionInstance> selectByPlanAndExpressionType(@Param("planId") Long planId,
                                                                   @Param("stepModeId") Long stepModeId,
                                                                   @Param("expressionType") String expressionType);

    List<ProcedureConditionInstance> selectByPlanAndExpressionTypes(@Param("planIds") List<Long> planId,
                                                                   @Param("expressionType") String expressionType);
}
