package com.bmos.mes.service.process.mapper.task;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mapper
public interface ProcedureExpressionMapper extends BaseMapperX<ProcedureExpression> {

    default List<ProcedureExpression> getListByProcedureModelId(Long procedureModelId) {
        return selectList(new LambdaQueryWrapperX<ProcedureExpression>()
                .eq(ProcedureExpression::getProcedureModelId, procedureModelId));
    }

    default Boolean updateBatchExpression(List<ProcedureExpression> expression) {
        return Db.saveOrUpdateBatch(expression);
    }

    default List<ProcedureExpression> selectListByIds(List<Long> expressionIds) {
        return selectList(new LambdaQueryWrapperX<ProcedureExpression>()
                .in(ProcedureExpression::getId, expressionIds));
    }

    default Boolean deleteByIds(List<Long> expressionIds) {
        if (CollUtil.isEmpty(expressionIds)){
            return true;
        }
        return Db.removeByIds(expressionIds, ProcedureExpression.class);
    }

    default List<ProcedureExpression> getListByProcedureStepModelIds(Set<Long> ids) {
        return selectList(new LambdaQueryWrapperX<ProcedureExpression>()
                .in(ProcedureExpression::getProcedureStepModelId, ids));
    }

    default List<ProcedureExpression> queryListByStepModeIdAndNodeType(List<Long> stepModeId,String expressionNodeType) {
        if (CollUtil.isEmpty(stepModeId)) {
            return Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<ProcedureExpression>()
                .in(ProcedureExpression::getProcedureStepModelId, stepModeId)
                .eq(ProcedureExpression::getExpressionNodeType,expressionNodeType));
    }

    default void deleteByProcedureStepModelIds(List<Long> modelIds) {
        delete(new LambdaQueryWrapperX<ProcedureExpression>()
                .in(ProcedureExpression::getProcedureStepModelId, modelIds));
    }

    default List<ProcedureExpression> getByProcedureStepModelIds(List<Long> stepModeIds) {
        return selectList(new LambdaQueryWrapperX<ProcedureExpression>()
                .in(ProcedureExpression::getProcedureStepModelId, stepModeIds));
    }

    default List<ProcedureExpression> selectListByProcedureModelIdAndNodeType(List<Long> procedureModelIds, String value){
        return selectList(new LambdaQueryWrapperX<ProcedureExpression>()
                .in(ProcedureExpression::getProcedureModelId,procedureModelIds)
                .eqIfPresent(ProcedureExpression::getExpressionNodeType,value));
    }

    List<String> getConfigByModelId(@Param("procedureModelIds") List<Long> procedureModelId,@Param("nodeType") String nodeType);
}
