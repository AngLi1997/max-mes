package com.bmos.mes.service.process.service.impl.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.convert.Task.ProcedureConditionConverter;
import com.bmos.mes.service.process.convert.Task.ProcedureExpressionConverter;
import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.bmos.mes.service.process.dto.task.CheckoutExpressionDTO;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.bmos.mes.service.process.mapper.task.ProcedureExpressionMapper;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.service.task.ProcedureConditionService;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;
import com.bmos.mes.service.utils.ConditionExecuteUtil;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcedureExpressionServiceImpl extends ServiceImpl<ProcedureExpressionMapper, ProcedureExpression> implements ProcedureExpressionService {

    @Autowired
    private ProcedureExpressionMapper expressionMapper;

    @Autowired
    private ProcedureConditionService conditionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertBatch(List<ProcedureExpression> expressions) {
        if (CollUtil.isEmpty(expressions)) {
            return;
        }
        expressionMapper.insertBatch(expressions);
    }

    @Override
    public Map<String, List<ExpressionDetailVO>> getListByProcedureModelId(Long procedureModelId) {
        List<ProcedureExpression> expressionList = expressionMapper.getListByProcedureModelId(procedureModelId);
        if (CollUtil.isEmpty(expressionList)) {
            return Collections.emptyMap();
        }
        List<Long> expressionIdList = CollectionUtils.convertList(expressionList, ProcedureExpression::getId);
        List<Long> stepModelIdList = CollectionUtils.convertList(expressionList, ProcedureExpression::getProcedureStepModelId);
        List<ProcedureCondition> conditions = conditionService.selectListByExpressionIdList(expressionIdList);
        List<ProcedureCondition> conditionList = CollectionUtils.filterList(conditions, item -> stepModelIdList.contains(item.getProcedureStepModelId()));
        List<ExpressionDetailVO> expressionDetailList =
                ProcedureExpressionConverter.INSTANCE.convertToDetailVo(expressionList);
        List<ConditionDetailVO> detailList = ProcedureConditionConverter.INSTANCE.convertToDetailVo(conditionList);
        Map<Long, List<ConditionDetailVO>> conditionMap = CollectionUtils.convertMultiMap(detailList,
                ConditionDetailVO::getExpressionId);
        expressionDetailList.forEach(item ->
                item.setConditionList(conditionMap.get(item.getId())));
        return CollectionUtils.convertMultiMap(expressionDetailList, ExpressionDetailVO::getExpressionType);
    }

    @Override
    public Map<Long, List<ProcedureExpression>> getMapByProcedureStepModeIds(Set<Long> stepModelId) {
        if (CollUtil.isEmpty(stepModelId)) {
            return Collections.emptyMap();
        }
        List<ProcedureExpression> expressionList = expressionMapper.getListByProcedureStepModelIds(stepModelId);
        if (CollUtil.isEmpty(expressionList)) {
            return Collections.emptyMap();
        }
        List<Long> expressionIdList = CollectionUtils.convertList(expressionList, ProcedureExpression::getId);
        List<ProcedureCondition> conditions = conditionService.selectListByExpressionIdList(expressionIdList);
        List<ProcedureCondition> conditionList = CollectionUtils.filterList(conditions, item -> stepModelId.contains(item.getProcedureStepModelId()));
        Map<Long, List<ProcedureCondition>> conditionMap = CollectionUtils.convertMultiMap(conditionList,
                ProcedureCondition::getExpressionId);
        expressionList.forEach(item ->
                item.setConditions(conditionMap.get(item.getId())));
        return CollectionUtils.convertMultiMap(expressionList, ProcedureExpression::getProcedureStepModelId);
    }

    @Override
    public Boolean checkoutExpression(CheckoutExpressionDTO dto) {
        return ConditionExecuteUtil.calculateExpression(dto);
    }

    @Override
    public List<ProcedureExpression> getMapByStepOrTask(List<Long> ids) {
        List<ProcedureExpression> expressionList = expressionMapper.queryListByStepModeIdAndNodeType(ids,NodeTypeEnum.STEP_OR_TASK.getValue());
        if (CollUtil.isEmpty(expressionList)) {
            return Collections.emptyList();
        }
        List<Long> expressionIdList = CollectionUtils.convertList(expressionList, ProcedureExpression::getId);
        List<ProcedureCondition> conditions = conditionService.selectListByExpressionIdList(expressionIdList);
        Map<Long, List<ProcedureCondition>> conditionMap = CollectionUtils.convertMultiMap(conditions,
                ProcedureCondition::getExpressionId);
        expressionList.forEach(item ->
                item.setConditions(conditionMap.get(item.getId())));
        return expressionList;
    }

    @Override
    public List<ProcedureExpression> getExpressionListByStepModelIds(List<Long> taskIds) {
        if (CollUtil.isEmpty(taskIds)) {
            return Collections.emptyList();
        }
        return expressionMapper.queryListByStepModeIdAndNodeType(taskIds,NodeTypeEnum.STEP_OR_TASK.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(List<ProcedureStepDTO> items, ProcedureModel procedureModel) {
        if (CollUtil.isEmpty(items)) {
            return;
        }

    }
    @Override
    public void deleteByIds(List<ExpressionSaveDTO> dto) {
        List<Long> expressionIds = CollectionUtils.convertList(dto, ExpressionSaveDTO::getId);
        if (ObjectUtil.isNull(expressionIds)) {
            throw new BmosException(MesResponseCode.PROCESS_EXPRESSION_ERROR);
        }
        expressionMapper.deleteByIds(expressionIds);
    }

    /**
     * 通过工步模型id删除表达式
     * @param modelIds 工步模型id
     */
    @Override
    public void deleteByProcedureStepModelIds(List<Long> modelIds) {
        expressionMapper.deleteByProcedureStepModelIds(modelIds);
    }

    @Override
    public List<ProcedureExpression> getByProcedureStepModelIds(List<Long> stepModeIds) {
      return  expressionMapper.getByProcedureStepModelIds(stepModeIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProcedureExpression(List<ProcedureModel> procedureModels) {
        List<ProcedureExpression> expressionList = new ArrayList<>();
        List<ProcedureCondition> conditionList = new ArrayList<>();
        procedureModels.forEach(model->{
            if (ObjectUtil.isEmpty(model.getCompleteCondition()) || CollUtil.isEmpty(model.getCompleteCondition().getConditionList())){
                return;
            }
            ProcedureExpression expression = ProcedureExpressionConverter.INSTANCE.convertToExpression(model.getCompleteCondition());
            expression.setId(IdUtils.getSnowflake());
            expression.setProcedureModelId(model.getId());
            expression.setExpressionType(ExpressionTypeEnum.COMPLETE_CONDITION.getValue());
            expression.setExpressionNodeType(NodeTypeEnum.PROCEDURE.getValue());
            expression.setNodeId(model.getNodeId());
            expressionList.add(expression);
            conditionList.addAll(ProcedureConditionConverter.INSTANCE.convertConditionList(model.getCompleteCondition().getConditionList(),
                    expression.getId(), null,NodeTypeEnum.PROCEDURE.getValue()));
        });
        expressionMapper.insertBatch(expressionList);
        conditionService.insertBatch(conditionList);
    }

    private void updateExpressionAndCondition(List<Long> procedureModelIds,List<ExpressionSaveDTO> expressionList) {
        List<Long> expressionId = CollectionUtils.convertList(expressionList, ExpressionSaveDTO::getId);
        //找到工序完成条件下的表达式
        List<ProcedureExpression> expressions = expressionMapper.selectListByProcedureModelIdAndNodeType(procedureModelIds,
                NodeTypeEnum.PROCEDURE.getValue());
        if (CollUtil.isEmpty(expressions)){
            return;
        }
        List<Long> expressionIds = CollectionUtils.convertList(expressions, ProcedureExpression::getId);
        List<Long> conditionIds = CollectionUtils.convertList(conditionService.selectListByExpressionIdList(expressionIds), ProcedureCondition::getId);
        List<ProcedureCondition> conditionList = ProcedureConditionConverter.INSTANCE.convertToConditionList(expressionList);
        if (CollUtil.isNotEmpty(expressions)){
            if (CollUtil.isNotEmpty(conditionList)){
                //找打需要删除的表达式id与条件id
                List<Long> refreshIdList = CollectionUtils.convertList(conditionList, ProcedureCondition::getId);
                expressionIds  = CollectionUtils.filterList(expressionIds,item -> !expressionId.contains(item));
                conditionIds = CollectionUtils.filterList(conditionIds,item-> !refreshIdList.contains(item));
            }
            expressionMapper.deleteByIds(expressionIds);
            conditionService.deleteByIds(conditionIds);
        }
        if (CollUtil.isEmpty(conditionList)){
            return;
        }
        expressionMapper.updateBatchExpression(ProcedureExpressionConverter.INSTANCE.convertExpressionList(expressionList));
        conditionService.saveOrUpdateBatch(conditionList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExpressionAndCondition(List<ProcedureModel> procedureModels) {
        List<ProcedureModel> conditionModelList = CollectionUtils.filterList(procedureModels, item ->
                ObjectUtil.isNotEmpty(item.getCompleteCondition()));
        if (CollUtil.isEmpty(conditionModelList)){
            return;
        }
        Map<Boolean, List<ProcedureModel>> partition =
                conditionModelList.stream().collect(Collectors.partitioningBy(e -> ObjectUtil.isNotNull(e.getCompleteCondition().getId())));
        List<ProcedureModel> updateModelList = partition.get(true);
        if (CollUtil.isNotEmpty(updateModelList)){
            List<Long> modelIdList = CollectionUtils.convertList(updateModelList, ProcedureModel::getId);
            List<ExpressionSaveDTO> expressionList = CollectionUtils.convertList(updateModelList, ProcedureModel::getCompleteCondition);
            updateExpressionAndCondition(modelIdList,expressionList);
        }
        List<ProcedureModel> saveModelList = partition.get(false);
        if (CollUtil.isNotEmpty(saveModelList)){
            saveProcedureExpression(saveModelList);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByProcedureModelIds(List<Long> modelIdList) {
        if (CollUtil.isEmpty(modelIdList)){
            return;
        }
        List<ProcedureExpression> expressions = expressionMapper.selectListByProcedureModelIdAndNodeType(modelIdList, NodeTypeEnum.PROCEDURE.getValue());
        if (CollUtil.isEmpty(expressions)){
            return;
        }
        List<Long> expressionIds = CollectionUtils.convertList(expressions, ProcedureExpression::getId);
        expressionMapper.deleteByIds(expressionIds);
        conditionService.deleteByExpressionIdlList(expressionIds);
    }

    @Override
    public List<ExpressionDetailVO> selectByModelId(List<Long> procedureModelId,String nodeType) {
        List<ProcedureExpression> expressions = expressionMapper.selectListByProcedureModelIdAndNodeType(procedureModelId, nodeType);
        if (CollUtil.isEmpty(expressions)) {
            return new ArrayList<>();
        }
        List<Long> expressionIdList = CollectionUtils.convertList(expressions, ProcedureExpression::getId);
        List<ProcedureCondition> conditions = conditionService.selectListByExpressionIdList(expressionIdList);
        List<ExpressionDetailVO> expressionDetailList =
                ProcedureExpressionConverter.INSTANCE.convertToDetailVo(expressions);
        List<ConditionDetailVO> detailList = ProcedureConditionConverter.INSTANCE.convertToDetailVo(conditions);
        Map<Long, List<ConditionDetailVO>> conditionMap = CollectionUtils.convertMultiMap(detailList,
                ConditionDetailVO::getExpressionId);
        expressionDetailList.forEach(item ->
                item.setConditionList(conditionMap.get(item.getId())));
        return expressionDetailList;
    }

    @Override
    public List<String> getConfigByModelId(List<Long> procedureModelId) {
        if (CollUtil.isEmpty(procedureModelId)){
            return new ArrayList<>();
        }
        return expressionMapper.getConfigByModelId(procedureModelId, NodeTypeEnum.PROCEDURE.getValue());
    }

    @Override
    public List<String> getStepModelCondition(List<Long> stepModelId,List<String> conditionType) {
        if (ObjectUtil.isNull(stepModelId)){
            return new ArrayList<>();
        }
        return conditionService.getStepModelCondition(stepModelId,conditionType);
    }

    @Override
    public List<ProcedureExpression> startPlanConditionList(Set<Long> stepModelIds,Set<Long> ids) {
        return expressionMapper.selectList( new LambdaQueryWrapperX<ProcedureExpression>()
                .in(ProcedureExpression::getProcedureStepModelId, stepModelIds)
                .in(ProcedureExpression::getId, ids)
                .eq(ProcedureExpression::getExpressionType, ExpressionTypeEnum.EXECUTE_CONDITION.getValue())
                .eq(ProcedureExpression::getExpressionNodeType, NodeTypeEnum.STEP_OR_TASK.getValue()));
    }

    @Override
    public List<ProcedureCondition> selectMaterialConditionListByStepModelId(List<Long> convertList, String type) {
        if (ObjectUtil.isNull(convertList)){
            return new ArrayList<>();
        }
        return conditionService.selectMaterialConditionListByStepModelId(convertList,type);
    }
}
