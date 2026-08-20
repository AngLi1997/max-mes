package com.bmos.mes.service.process.service.condition;

import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;

import java.util.List;

/**
 * 条件改变计算器
 *
 * @author yigaohui
 * @date 2024/7/12
 **/
public interface ConditionChangeCalculator {
    void calculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                  ConditionCalculateContext conditionCalculateContext);
}
