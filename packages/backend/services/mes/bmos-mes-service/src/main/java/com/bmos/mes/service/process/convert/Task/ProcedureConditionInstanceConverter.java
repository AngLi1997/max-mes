package com.bmos.mes.service.process.convert.Task;

import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstanceHistory;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProcedureConditionInstanceConverter {
    ProcedureConditionInstanceConverter INSTANCE = Mappers.getMapper(ProcedureConditionInstanceConverter.class);

    default List<ProcedureConditionInstance> convertToInstance(ProcedureExpression expression,
                                                               List<ProcedureCondition> conditions, Long planId) {
        return conditions.stream().map(item -> {
            ProcedureConditionInstance instance = new ProcedureConditionInstance();
            instance.setCode(item.getCode());
            instance.setConditionDetails(item.getConditionDetails());
            instance.setName(item.getName());
            instance.setPlanId(planId);
            instance.setConditionType(item.getConditionType());
            instance.setExpressionId(item.getExpressionId());
            instance.setTaskType(expression.getExpressionType());
            instance.setProcedureModelId(expression.getProcedureModelId());
            instance.setDefaultResult(item.getDefaultResult());
            instance.setProcedureStepModelId(expression.getProcedureStepModelId());
            instance.setConditionId(item.getId());
            return instance;
        }).collect(Collectors.toList());
    }

    List<ProcedureConditionInstanceHistory> convertToHistory(List<ProcedureConditionInstance> list);

}
