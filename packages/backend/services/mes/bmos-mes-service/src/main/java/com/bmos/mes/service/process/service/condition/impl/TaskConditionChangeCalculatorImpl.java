package com.bmos.mes.service.process.service.condition.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.cache.redis.lock.DistributedLock;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.plan.ProductTaskStatusEnum;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.common.enums.process.task.NodeTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.formula.service.ProductFormulaConfigureService;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.convert.Task.ProcedureExpressionConverter;
import com.bmos.mes.service.process.dto.task.CheckoutConditionDTO;
import com.bmos.mes.service.process.dto.task.CheckoutExpressionDTO;
import com.bmos.mes.service.process.mapper.task.ProcedureTaskInstanceMapper;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstance;
import com.bmos.mes.service.process.service.condition.ConditionCalculateContext;
import com.bmos.mes.service.process.service.condition.ITaskConditionCalculator;
import com.bmos.mes.service.process.service.condition.event.ConditionChangeType;
import com.bmos.mes.service.process.service.task.ProcedureConditionInstanceService;
import com.bmos.mes.service.process.service.task.ProcedureExpressionService;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.storage.manage.mapper.IStorageMaterialReserveMapper;
import com.bmos.mes.service.storage.manage.vo.BatchReservedMaterialVO;
import com.bmos.mes.service.utils.ConditionExecuteUtil;
import com.bmos.mes.service.workflow.enums.WorkflowType;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.RoomInfoFeignVO;
import com.bmos.unit.service.UnitCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yigaohui
 * @date 2024/7/10
 **/
@Service
@Slf4j
public class TaskConditionChangeCalculatorImpl extends ServiceImpl<ProcedureTaskInstanceMapper,
        ProcedureTaskInstance> implements ITaskConditionCalculator {

    private final String LOCK_KEY = "refreshConditionResult";

    @Autowired
    private ProcedureConditionInstanceService conditionInstanceService;

    @Autowired
    private ProcedureExpressionService procedureExpressionService;

    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private FactoryFeign factoryFeign;
    @Autowired
    private EquipmentConfigFeign equipmentConfigFeign;
    @Resource
    private ProductFormulaConfigureService productFormulaConfigureService;

    @Resource
    private IStorageMaterialReserveMapper storageMaterialReserveMapper;

    @Resource
    private UnitCache unitCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @DistributedLock(key = LOCK_KEY)
    public void refreshConditionResult(ConditionChangeType changeType) {
        List<ProcedureConditionInstance> conditionInstances =
                conditionInstanceService.selectConditionList(changeType.getConditionType() == null ? null :
                                changeType.getConditionType().getValue(),
                        changeType.getPlanId() == null ? null : changeType.getPlanId());
        ConditionCalculateContext conditionCalculateContext = this.getConditionCalculateContext(conditionInstances,
                changeType.getPlanId());
        changeType.calculateConditionChange(conditionInstances, conditionCalculateContext);
        if (CollUtil.isNotEmpty(conditionInstances)) {
            conditionInstanceService.saveOrUpdateBatch(conditionInstances);
        }
        this.calculateTaskEnableStatus(changeType, conditionInstances);
    }

    private ConditionCalculateContext getConditionCalculateContext(List<ProcedureConditionInstance> conditions,
                                                                   Long planId) {
        //如果条件为空直接返回
        if (CollUtil.isEmpty(conditions)){
            return null;
        }
        // 这边只有房间和设备的状态是能够初始化的
        ConditionCalculateContext conditionCalculateContext = new ConditionCalculateContext();
        List<ConditionDetailVO> conditionDetailVOS =
                conditions.stream().map(item -> JSONUtil.toBean(item.getConditionDetails(),
                        ConditionDetailVO.class)).collect(Collectors.toList());
        List<Long> roomIds =
                new ArrayList<>();
        List<Long> equipmentIds =
                new ArrayList<>();
        for (ConditionDetailVO conditionDetailVO : conditionDetailVOS) {
            Long roomId = conditionDetailVO.getRoomId();
            if (roomId != null) {
                roomIds.add(roomId);
            }
            if (conditionDetailVO.getEquipmentId() != null) {
                equipmentIds.add(conditionDetailVO.getEquipmentId());
            }
        }
        if (CollectionUtil.isNotEmpty(roomIds)) {
            ResponseInfo<List<RoomInfoFeignVO>> listResponseInfo =
                    factoryFeign.selectByRoomIds(roomIds);
            conditionCalculateContext.setRoomInfoFeignVOList(listResponseInfo.getData());
        }
        if (CollectionUtil.isNotEmpty(equipmentIds)) {
            ResponseInfo<List<EquipmentInfoFeignVO>> listResponseInfo =
                    equipmentConfigFeign.selectEquipmentByIdList(equipmentIds);
            conditionCalculateContext.setEquipmentStatusFeignVOList(listResponseInfo.getData());
        }
        if (planId == null) {
            return conditionCalculateContext;
        }
        // 如果计划不为空，查询计划相关的上下文
        // 计划
        Plan plan = planMapper.selectById(planId);
        conditionCalculateContext.setPlan(plan);
        // 配方
        ProductFormulaInfo formulaInfo =
                productFormulaConfigureService.getProductFormulaInfoByPlanId(planId);
        conditionCalculateContext.setProductFormulaInfo(formulaInfo);
        // 预定物料
        // 查询已预订暂存物料
        List<BatchReservedMaterialVO> reservedMaterialVOS =
                storageMaterialReserveMapper.queryBatchReservedMaterialByMaterialIds(planId, null);
        conditionCalculateContext.setReserveList(reservedMaterialVOS);
        conditionCalculateContext.setUnitCache(unitCache);
        return conditionCalculateContext;
    }

    private void calculateTaskEnableStatus(ConditionChangeType changeType,
                                           List<ProcedureConditionInstance> conditionValueChange) {
        /*List<Long> stepModelIds =
                conditionValueChange.stream().map(ProcedureConditionInstance::getProcedureStepModelId).collect(Collectors.toList());*/
        List<ProcedureTaskInstance> taskInstances =
                this.getBaseMapper().selectTaskNotCompleteByTaskIds(changeType.getPlanId() == null ?
                        null :
                        changeType.getPlanId());
        if (CollectionUtil.isEmpty(taskInstances)) {
            return;
        }
        //如果条件为空，任务直接变为可用
        if (CollUtil.isEmpty(conditionValueChange)){
            taskInstances.forEach(item->{
                item.setFlowState(ProductTaskStatusEnum.ENABLE.getValue());
                item.setStartTime(item.getCreateTime());
            });
            this.updateBatchById(taskInstances);
            return;
        }
        List<Long> expressionIds =
                conditionValueChange.stream().map(ProcedureConditionInstance::getExpressionId).collect(Collectors.toList());
        List<ProcedureExpression> procedureExpressionList = procedureExpressionService.listByIds(expressionIds);
        List<ProcedureConditionInstance> conditionInstances =
                conditionInstanceService.selectByExpressionIds(expressionIds,changeType.getPlanId() == null ? null : changeType.getPlanId());
        Map<Long, List<ProcedureExpression>> expressionMap =
                procedureExpressionList.stream().collect(Collectors.groupingBy(ProcedureExpression::getProcedureStepModelId));
        log.info("开始计算任务的可用状态");
        taskInstances.forEach(item -> {
            List<ProcedureExpression> procedureExpressions = expressionMap.get(item.getProcedureStepModelId());
            if (CollectionUtil.isEmpty(procedureExpressions)) {
                item.setFlowState(ProductTaskStatusEnum.ENABLE.getValue());
                item.setStartTime(item.getCreateTime());
                log.info("任务【{}】没有配置条件，可用", item.getName());
                return;
            }
            ProcedureExpression expression = CollectionUtils.findFirst(procedureExpressions,
                    items -> StrUtil.equals(items.getExpressionType(),
                            ExpressionTypeEnum.EXECUTE_CONDITION.getValue()));
            if (expression == null) {
                log.info("任务【{}】没有配置执行体条件,可用", item.getName());
                item.setFlowState(ProductTaskStatusEnum.ENABLE.getValue());
                item.setStartTime(item.getCreateTime());
                return;
            }
            List<ProcedureConditionInstance> instances =
                    conditionInstances.stream().filter(conditionInstance ->
                            conditionInstance.getExpressionId().equals(expression.getId())
                                    && conditionInstance.getProcedureStepModelId().equals(item.getProcedureStepModelId())
                                    && conditionInstance.getPlanId().equals(item.getPlanId())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(instances)) {
                log.info("任务【{}】配置的执行条件的条件实例为空，可用", item.getName());
                item.setFlowState(ProductTaskStatusEnum.ENABLE.getValue());
                item.setStartTime(LocalDateTime.now());
                return;
            }
            boolean assertResult = this.calculateExpression(expression, instances);
            item.setFlowState(assertResult ? ProductTaskStatusEnum.ENABLE.getValue() :
                    ProductTaskStatusEnum.DISABLE.getValue());
            item.setStartTime(assertResult ? LocalDateTime.now() : null);
        });
        this.updateBatchById(taskInstances);
    }

    /**
     * 根据数据库中记录的条件实例断言表达式是否满足
     *
     * @param expression 表达式对象
     * @return 结果
     */
    private boolean calculateExpression(ProcedureExpression expression,
                                        List<ProcedureConditionInstance> conditionInstances) {
        String expressionStr = expression.getExpression();
        if (StringUtils.isEmpty(expressionStr) || CollectionUtil.isEmpty(conditionInstances)) {
            return true;
        }
        CheckoutExpressionDTO checkoutExpressionDTO = new CheckoutExpressionDTO();
        checkoutExpressionDTO.setExpression(expressionStr);
        List<CheckoutConditionDTO> checkoutConditionDTOS = conditionInstances.stream().map(item -> {
            CheckoutConditionDTO checkoutConditionDTO = new CheckoutConditionDTO();
            checkoutConditionDTO.setCode(item.getCode());
            checkoutConditionDTO.setResult(item.getTaskResult());
            return checkoutConditionDTO;
        }).collect(Collectors.toList());
        checkoutExpressionDTO.setConditionList(checkoutConditionDTOS);
        return ConditionExecuteUtil.calculateExpression(checkoutExpressionDTO);
    }

    /**
     * 计算任务或者步骤的表达式结果
     *
     * @param stepModeId   任务或者步骤id
     * @param expressionType 计算类型
     * @return 结果
     */
    @Override
    public Pair<Boolean, List<ProcedureConditionInstance>> calculateTaskOrStepExpression(Long planId, Long stepModeId,
                                                                                         ExpressionTypeEnum expressionType) {
        List<ProcedureExpression> expression =
                procedureExpressionService.getExpressionListByStepModelIds(Collections.singletonList(stepModeId));
        Optional<ProcedureExpression> procedureExpression =
                expression.stream().filter(item -> StrUtil.equals(item.getExpressionType(),
                        expressionType.getValue())).findFirst();
        if (!procedureExpression.isPresent()) {
            return Pair.of(true, null);
        }
        List<ProcedureConditionInstance> conditionInstances =
                conditionInstanceService.selectByPlanAndExpressionType(planId, stepModeId, expressionType.getValue());
        boolean b = calculateExpression(procedureExpression.get(), conditionInstances);
        return Pair.of(b, conditionInstances);
    }

    @Override
    public Pair<Boolean, List<ProcedureConditionInstance>> calculateProcedureModelExpression(Long planId, Long procedureModelId,
                                                                                             Integer procedureChangeNumber,Integer processChangeNumber) {
        List<ExpressionDetailVO> expressionDetail = procedureExpressionService.selectByModelId(Collections.singletonList(procedureModelId),
                NodeTypeEnum.PROCEDURE.getValue());
        if (CollUtil.isEmpty(expressionDetail)){
            return Pair.of(true, null);
        }
        ExpressionDetailVO first = CollectionUtils.getFirst(expressionDetail);
        List<ConditionDetailVO> conditionList = first.getConditionList();
        List<Long> stepModelId = CollectionUtils.convertList(conditionList, ConditionDetailVO::getTaskNodeId);
        List<ProcedureTaskInstance> taskInstances = this.getBaseMapper().selectListByModelIdAndProcedureId(stepModelId, procedureModelId,
                procedureChangeNumber, processChangeNumber,planId);
        if (CollUtil.isEmpty(taskInstances)){
            throw new BmosException(MesResponseCode.TASK_ERROR);
        }
        Map<Long, ConditionDetailVO> conditionMap = CollectionUtils.convertMap(conditionList, ConditionDetailVO::getTaskNodeId);
        List<ProcedureConditionInstance> conditionInstanceList = taskInstances.stream().map(item -> {
            ProcedureConditionInstance instance = new ProcedureConditionInstance();
            ConditionDetailVO detailVO = conditionMap.get(item.getProcedureStepModelId());
            instance.setCode(detailVO.getCode());
            instance.setName(detailVO.getName());
            instance.setTaskResult(item.getFlowState().equals(ProductTaskStatusEnum.COMPLETE.getValue()));
            return instance;
        }).collect(Collectors.toList());
        boolean b = calculateExpression(ProcedureExpressionConverter.INSTANCE.convertToExpression(first), conditionInstanceList);
        //当判断结果为false时更新一下任务给一个暂停标识
        if (BooleanUtil.isFalse(b) && StrUtil.isBlank(CollectionUtils.getFirst(taskInstances).getPauseTag())){
            taskInstances.forEach(item->item.setPauseTag(WorkflowType.IS_PAUSE.name()));
            this.getBaseMapper().updateBatch(taskInstances);
        }
        return Pair.of(b, conditionInstanceList);
    }
}
