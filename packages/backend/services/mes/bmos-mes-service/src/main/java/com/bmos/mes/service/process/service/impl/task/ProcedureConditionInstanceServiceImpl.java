package com.bmos.mes.service.process.service.impl.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.convert.Task.ProcedureConditionInstanceConverter;
import com.bmos.mes.service.process.dto.task.CheckoutConditionDTO;
import com.bmos.mes.service.process.dto.task.CheckoutExpressionDTO;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.mapper.task.ProcedureConditionInstanceMapper;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.service.task.ProcedureConditionInstanceService;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.mes.service.utils.ConditionExecuteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProcedureConditionInstanceServiceImpl extends ServiceImpl<ProcedureConditionInstanceMapper,
        ProcedureConditionInstance> implements ProcedureConditionInstanceService {

    @Autowired
    private ProcedureConditionInstanceMapper mapper;


    @Autowired
    private ProcedureStepModelMapper stepModelMapper;

    @Autowired
    private ProcedureExpressionService expressionService;


    @Override
    public List<ProcedureConditionInstance> queryByExpressionIds(List<Long> expressionIds, Long planId) {
        return mapper.queryByExpressionIds(expressionIds, planId);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        mapper.deleteBatchIds(ids);
    }

    @Override
    public List<ProcedureConditionInstance> selectByExpressionIds(List<Long> expressionIdS,Long planId) {
        return mapper.selectByExpressionIds(expressionIdS,planId);
    }

    @Override
    public List<ProcedureConditionInstance> selectConditionList(String conditionType, Long planId) {
        return mapper.selectConditionList(conditionType, planId);
    }


    /**
     * 根据条件类型和计划id查询
     *
     * @param planId         计划id
     * @param stepModeId     步骤或者任务模型id
     * @param expressionType 表达式类型
     * @return 查询结果
     */
    @Override
    public List<ProcedureConditionInstance> selectByPlanAndExpressionType(Long planId, Long stepModeId,
                                                                          String expressionType) {
        return mapper.selectByPlanAndExpressionType(planId, stepModeId, expressionType);
    }

    @Override
    public void initConditionInstance(Plan plan) {
        //任务和步骤共用模型配置
        List<ProcedureStepModel> stepModels = stepModelMapper.getStepModelByProcessIdAndVersion(plan.getProcessId(),
                plan.getProcessVersion());
        List<ProcedureExpression> procedureExpressionList = expressionService.getMapByStepOrTask(
                CollectionUtils.convertList(stepModels, ProcedureStepModel::getId));
        // 先初始化条件
        this.initStepModelConditions(procedureExpressionList, plan.getId());
    }

    @Override
    public List<ProcedureConditionInstance> selectByPlanIdAndStepModelIds(List<Long> taskId, Long planId) {
        return null;
    }

    @Override
    public List<ProcedureExpression> startPlanConditionList(List<Long> planId) {
        List<ProcedureExpression> expressionList = new ArrayList<>();
        List<ProcedureConditionInstance> conditionInstanceList = mapper.selectByPlanAndExpressionTypes(
                planId, ExpressionTypeEnum.EXECUTE_CONDITION.getValue());
        if (CollUtil.isEmpty(conditionInstanceList)){
            return new ArrayList<>();
        }
        Set<Long> stepModelIds = CollectionUtils.convertSet(conditionInstanceList, ProcedureConditionInstance::getProcedureStepModelId);
        Set<Long> idS = CollectionUtils.convertSet(conditionInstanceList, ProcedureConditionInstance::getExpressionId);
        List<ProcedureExpression> expressions = expressionService.startPlanConditionList(stepModelIds,idS);
        Map<Long, List<ProcedureConditionInstance>> map = CollectionUtils.convertMultiMap(conditionInstanceList, ProcedureConditionInstance::getExpressionId);
        for (ProcedureExpression expression : expressions) {
            String expressionStr = expression.getExpression();
            List<ProcedureConditionInstance> conditionInstanceList1 = map.get(expression.getId());
            if (ObjectUtil.isEmpty(expressionStr) || CollUtil.isEmpty(conditionInstanceList1)){
                continue;
            }
            Map<Long, List<ProcedureConditionInstance>> map1 = CollectionUtils.convertMultiMap(conditionInstanceList1, ProcedureConditionInstance::getPlanId);
            CheckoutExpressionDTO checkoutExpressionDTO = new CheckoutExpressionDTO();
            checkoutExpressionDTO.setExpression(expressionStr);
            map1.forEach((key,value)->{
                ProcedureExpression procedureExpression = new ProcedureExpression();
                List<CheckoutConditionDTO> checkoutConditionDTOS = value.stream().map(item -> {
                    CheckoutConditionDTO checkoutConditionDTO = new CheckoutConditionDTO();
                    checkoutConditionDTO.setCode(item.getCode());
                    checkoutConditionDTO.setResult(item.getTaskResult());
                    return checkoutConditionDTO;
                }).collect(Collectors.toList());
                checkoutExpressionDTO.setConditionList(checkoutConditionDTOS);
                Boolean aBoolean = ConditionExecuteUtil.calculateExpression(checkoutExpressionDTO);
                procedureExpression.setProcedureStepModelId(expression.getProcedureStepModelId());
                procedureExpression.setNodeId(expression.getNodeId());
                procedureExpression.setPlanId(CollectionUtils.getFirst(value).getPlanId());
                procedureExpression.setResults(false);
                if (aBoolean){
                    procedureExpression.setResults(true);
                }
                expressionList.add(procedureExpression);
            });
        }
        return expressionList;
    }


    private void initStepModelConditions(List<ProcedureExpression> expressionList, Long planId) {

        if (CollectionUtil.isEmpty(expressionList)) {
            return;
        }
        List<ProcedureConditionInstance> conditionList = new ArrayList<>();
        expressionList.forEach(executeExpression -> {
            List<ProcedureConditionInstance> conditionInstance =
                    ProcedureConditionInstanceConverter.INSTANCE.convertToInstance(executeExpression,
                            executeExpression.getConditions(), planId);
            conditionList.addAll(conditionInstance);
        });
        this.saveBatch(conditionList);
    }
}
