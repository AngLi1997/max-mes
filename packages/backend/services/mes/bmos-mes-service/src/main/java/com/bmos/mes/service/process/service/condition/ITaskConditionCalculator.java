package com.bmos.mes.service.process.service.condition;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.model.task.ProcedureTaskInstance;
import com.bmos.mes.service.process.service.condition.event.ConditionChangeType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * 条件结果处理器
 *
 * @author yigaohui
 * @date 2024/7/10
 **/
public interface ITaskConditionCalculator extends IService<ProcedureTaskInstance> {

    /**
     * 事件中可能没有传planId，如果planId为空，表示该事件会触发所有计划没有完成的任务的条件计算
     *
     * @param changeType 事件
     */
    void refreshConditionResult(ConditionChangeType changeType);


    /**
     * 计算任务或者步骤的表达式结果
     *
     * @param planId         计划id
     * @param taskOrStepId   任务或者步骤id
     * @param expressionType 计算类型
     * @return left：执行结果，right：任务对应的条件实例
     */
    Pair<Boolean, List<ProcedureConditionInstance>> calculateTaskOrStepExpression(Long planId, Long taskOrStepId,
                                                                                  ExpressionTypeEnum expressionType);

    /**
     * 计算工序配置完成条件表达式结果
     *
     * @Param: planId
     * @Param: procedureModelId
     * @Param: procedureChangeNumber
     * @Param: processChangeNumber
     */
    Pair<Boolean,List<ProcedureConditionInstance>> calculateProcedureModelExpression(Long planId,Long procedureModelId,
                                                                                     Integer procedureChangeNumber,Integer processChangeNumber);
}
