package com.bmos.mes.service.process.service.impl.task;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.service.process.convert.Task.ProcedureConditionInstanceConverter;
import com.bmos.mes.service.process.mapper.task.ProcedureConditionInstanceHistoryMapper;
import com.bmos.mes.service.process.mapper.task.ProcedureExpressionMapper;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.service.task.ProcedureConditionInstanceHistoryService;
import com.bmos.mes.service.process.service.task.ProcedureConditionInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcedureConditionInstanceHistoryServiceImpl implements ProcedureConditionInstanceHistoryService {

    @Autowired
    private ProcedureExpressionMapper expressionMapper;

    @Autowired
    private ProcedureConditionInstanceService conditionInstanceService;

    @Autowired
    private ProcedureConditionInstanceHistoryMapper historyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveConditionHistory(List<Long> stepModeId, Long planId,List<Long> procedureModelId) {
        //找打工步上的条件
        List<ProcedureExpression> expressions = expressionMapper.queryListByStepModeIdAndNodeType(stepModeId, NodeTypeEnum.STEP_OR_TASK.getValue());
        //找到工序上的条件
        List<ProcedureExpression> procedureExpression = expressionMapper.selectListByProcedureModelIdAndNodeType(procedureModelId, NodeTypeEnum.PROCEDURE.getValue());
        if (CollUtil.isEmpty(expressions) && CollUtil.isEmpty(procedureExpression)){
            return;
        }
        List<Long> expressionIdList = CollectionUtils.convertList(expressions, ProcedureExpression::getId);
        expressionIdList.addAll(CollectionUtils.convertList(procedureExpression,ProcedureExpression::getId));
        List<ProcedureConditionInstance> conditionInstances = conditionInstanceService.queryByExpressionIds(expressionIdList,planId);
        historyMapper.saveOrUpdateBatch(ProcedureConditionInstanceConverter.INSTANCE.convertToHistory(conditionInstances));
        conditionInstanceService.deleteByIds(CollectionUtils.convertList(conditionInstances,ProcedureConditionInstance::getId));
    }
}
