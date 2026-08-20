package com.bmos.mes.service.process.service.condition.event;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.service.condition.ConditionCalculateContext;
import com.bmos.mes.service.process.service.condition.ConditionChangeCalculator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 条件变化事件
 *
 * @author yigaohui
 * @date 2024/7/10
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public abstract class ConditionChangeType implements ConditionChangeCalculator {

    protected Long planId;

    public abstract ConditionTypeEnum getConditionType();

    @Override
    public void calculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                         ConditionCalculateContext conditionCalculateContext) {
        if (CollectionUtil.isEmpty(changeConditionInstances)) {
            return;
        }
        log.info("接收到条件变更事件【{}】", getConditionType());
        // 找到和当前事件类型相同的条件实例
        List<ProcedureConditionInstance> instanceList =
                changeConditionInstances.stream().filter(item -> item.getConditionType().equals(getConditionType().getValue())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(instanceList)) {
            return;
        }
        this.innerCalculateConditionChange(instanceList, conditionCalculateContext);
    }

    protected abstract void innerCalculateConditionChange(List<ProcedureConditionInstance> instanceList,
                                                          ConditionCalculateContext conditionCalculateContext);
}
