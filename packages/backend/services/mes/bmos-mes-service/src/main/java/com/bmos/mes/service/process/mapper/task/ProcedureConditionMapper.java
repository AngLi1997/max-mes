package com.bmos.mes.service.process.mapper.task;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcedureConditionMapper extends BaseMapperX<ProcedureCondition> {

    default List<ProcedureCondition> selectListByExpressionIdList(List<Long> expressionIdList) {
        return selectList(new LambdaQueryWrapperX<ProcedureCondition>()
                .in(ProcedureCondition::getExpressionId, expressionIdList));
    }

    default void deleteByExpressionId(List<Long> id) {
        if (CollUtil.isEmpty(id)) {
            return;
        }
        delete(new LambdaQueryWrapperX<ProcedureCondition>()
                .in(ProcedureCondition::getExpressionId, id));
    }

    default void deleteByProcedureStepModelIds(List<Long> modelIds) {
        delete(new LambdaQueryWrapperX<ProcedureCondition>()
                .in(ProcedureCondition::getProcedureStepModelId, modelIds));
    }

    default List<ProcedureCondition> getByProcedureStepModelIds(List<Long> modeIds){
        return selectList(new LambdaQueryWrapperX<ProcedureCondition>()
                .in(ProcedureCondition::getProcedureStepModelId, modeIds));
    }

    default List<ProcedureCondition> getStepModelCondition(List<Long> stepModelId,String nodeType,List<String> conditionType){
        return selectList(new LambdaQueryWrapperX<ProcedureCondition>()
                .in(ProcedureCondition::getProcedureStepModelId, stepModelId)
                .eq(ProcedureCondition::getConditionNodeType,nodeType)
                .in(ProcedureCondition::getConditionType,conditionType));
    }
}
