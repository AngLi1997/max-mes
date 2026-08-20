package com.bmos.mes.service.process.convert.Task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.service.process.dto.task.ConditionSaveDTO;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProcedureConditionConverter {
    ProcedureConditionConverter INSTANCE = Mappers.getMapper(ProcedureConditionConverter.class);

    default List<ProcedureCondition> convertConditionList(List<ConditionSaveDTO> conditionList, Long expressionId,
                                                          Long stepModelId,String nodeType) {
        return conditionList.stream().map(item->{
            ProcedureCondition condition = new ProcedureCondition();
            condition.setId(ObjectUtil.isNotNull(item.getId()) ? item.getId() : IdUtils.getSnowflake());
            condition.setCode(item.getCode());
            condition.setConditionType(item.getConditionType());
            condition.setName(item.getName());
            condition.setExpressionId(expressionId);
            condition.setDefaultResult(item.getDefaultResult());
            condition.setConditionDetails(JsonUtils.toJsonString(item));
            condition.setProcedureStepModelId(stepModelId);
            condition.setConditionNodeType(nodeType);
            return condition;
        }).collect(Collectors.toList());
    }

    default List<ConditionDetailVO> convertToDetailVo(List<ProcedureCondition> conditions){
       return conditions.stream().map(item->{
           ConditionDetailVO detailVO = JsonUtils.parseObject(item.getConditionDetails(), ConditionDetailVO.class);
           detailVO.setId(item.getId());
           detailVO.setDefaultResult(item.getDefaultResult());
           detailVO.setConditionType(item.getConditionType());
           detailVO.setExpressionId(item.getExpressionId());
           detailVO.setConditionNodeType(item.getConditionNodeType());
           detailVO.setProcedureStepModelId(item.getProcedureStepModelId());
           return detailVO;
        }).collect(Collectors.toList());
    }

    default List<ProcedureCondition> convertToConditionList(List<ExpressionSaveDTO> expressionList){
        List<ProcedureCondition> refreshCondition = new ArrayList<>();
        expressionList.forEach(expressions->{
            if (CollUtil.isEmpty(expressions.getConditionList())){
                return;
            }
            List<ProcedureCondition> collect = expressions.getConditionList()
                    .stream().map(item -> {
                        ProcedureCondition condition = convertToCondition(item);
                        if (ObjectUtil.isNull(condition.getId())) {
                            condition.setId(IdUtils.getSnowflake());
                            condition.setExpressionId(expressions.getId());
                            condition.setConditionNodeType(NodeTypeEnum.PROCEDURE.getValue());
                        }
                        condition.setConditionDetails(JsonUtils.toJsonString(item));
                        return condition;
                    }).collect(Collectors.toList());
            refreshCondition.addAll(collect);
        });
        return refreshCondition;
    }

    ProcedureCondition convertToCondition(ConditionSaveDTO item);
}
