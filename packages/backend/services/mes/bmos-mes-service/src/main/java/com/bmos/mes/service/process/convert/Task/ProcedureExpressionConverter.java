package com.bmos.mes.service.process.convert.Task;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProcedureExpressionConverter {
    ProcedureExpressionConverter INSTANCE = Mappers.getMapper(ProcedureExpressionConverter.class);

    default ProcedureExpression convertExpression(ExpressionSaveDTO dto, ProcedureStepModel model, String type) {
        ProcedureExpression expression = new ProcedureExpression();
        expression.setId(IdUtils.getSnowflake());
        expression.setExpression(dto.getExpression());
        expression.setNodeId(model.getNodeId());
        expression.setExpressionType(type);
        expression.setProcedureModelId(model.getProcedureModelId());
        expression.setProcedureStepModelId(model.getId());
        expression.setResult(dto.getResult());
        expression.setExpressionNodeType(NodeTypeEnum.STEP_OR_TASK.getValue());
        return expression;
    }

    default ProcedureExpression convertSaveExpression(ExpressionSaveDTO dto, ProcedureStepDTO stepDto, String type,Long procudeModeId){
        ProcedureExpression expression = new ProcedureExpression();
        expression.setId(dto.getId()==null?IdUtils.getSnowflake():dto.getId());
        expression.setExpression(dto.getExpression());
        expression.setNodeId(stepDto.getNodeId());
        expression.setExpressionType(type);
        expression.setProcedureModelId(procudeModeId);
        expression.setProcedureStepModelId(stepDto.getId());
        expression.setResult(dto.getResult());
        expression.setExpressionNodeType(NodeTypeEnum.STEP_OR_TASK.getValue());
        return expression;
    }

    ProcedureExpression convertToExpression(ExpressionSaveDTO dto);

    default List<ExpressionDetailVO> convertToDetailVo(List<ProcedureExpression> expressions) {
        return expressions.stream().map(item -> {
            ExpressionDetailVO vo = new ExpressionDetailVO();
            vo.setId(item.getId());
            vo.setResult(item.getResult());
            vo.setExpression(item.getExpression());
            vo.setExpressionType(item.getExpressionType());
            vo.setStepTaskId(item.getProcedureStepModelId());
            vo.setProcedureModelId(item.getProcedureModelId());
            vo.setExpressionNodeType(item.getExpressionNodeType());
            return vo;
        }).collect(Collectors.toList());
    }

    List<ProcedureExpression> convertExpressionList(List<ExpressionSaveDTO> expressionList);

    ProcedureExpression convertToExpression(ExpressionDetailVO vo);
}
