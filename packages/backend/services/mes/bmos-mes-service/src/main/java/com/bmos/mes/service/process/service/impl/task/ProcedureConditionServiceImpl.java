package com.bmos.mes.service.process.service.impl.task;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.service.process.mapper.task.ProcedureConditionMapper;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.service.task.ProcedureConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Service
public class ProcedureConditionServiceImpl extends ServiceImpl<ProcedureConditionMapper, ProcedureCondition> implements ProcedureConditionService {

    @Autowired
    private ProcedureConditionMapper conditionMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatch(List<ProcedureCondition> conditions) {
        if (CollUtil.isEmpty(conditions)){
            return;
        }
        conditionMapper.insertBatch(conditions);
    }

    @Override
    public List<ProcedureCondition> selectListByExpressionIdList(List<Long> expressionIdList) {
        return conditionMapper.selectListByExpressionIdList(expressionIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByExpressionIdlList(List<Long> expressionIds) {
        conditionMapper.deleteByExpressionId(expressionIds);
    }

    @Override
    public void deleteByProcedureStepModelIds(List<Long> modelIds) {
        conditionMapper.deleteByProcedureStepModelIds(modelIds);
    }

    /**
     * 通过步骤模型id获取条件
     *
     * @param modeIds 步骤模型id
     * @return 获取结果
     */
    @Override
    public List<ProcedureCondition> getByProcedureStepModelIds(List<Long> modeIds) {
        return conditionMapper.getByProcedureStepModelIds(modeIds);
    }

    @Override
    public void deleteByIds(List<Long> conditionIds) {
        if (CollUtil.isEmpty(conditionIds)){
            return;
        }
        conditionMapper.deleteBatchIds(conditionIds);
    }

    @Override
    public List<String> getStepModelCondition(List<Long> stepModelId,List<String> conditionType) {
        List<ProcedureCondition> stepModelCondition = conditionMapper.getStepModelCondition(stepModelId,
                NodeTypeEnum.STEP_OR_TASK.getValue(), conditionType);
        return CollectionUtils.convertList(stepModelCondition, ProcedureCondition::getConditionDetails);
    }

    @Override
    public List<ProcedureCondition> selectMaterialConditionListByStepModelId(List<Long> convertList, String type) {
        return conditionMapper.getStepModelCondition(convertList, NodeTypeEnum.STEP_OR_TASK.getValue(), Collections.singletonList(type));
    }
}
