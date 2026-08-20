package com.bmos.mes.service.process.service.condition.event;

import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.service.condition.ConditionCalculateContext;
import lombok.Data;

import java.util.List;

/**
 * @author yigaohui
 * @date 2024/7/10
 **/
@Data
public class WeighingIngredientSignType extends ConditionChangeType {

    private Boolean signAll;

    private List<Long> stepModelId;

    public WeighingIngredientSignType(Long planId, Boolean signAll,List<Long> stepModelId) {
        super(planId);
        this.signAll = signAll;
        this.stepModelId = stepModelId;
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.DOSING_SIGNATURE;
    }

    @Override
    public void innerCalculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                              ConditionCalculateContext conditionCalculateContext) {
        changeConditionInstances.forEach(instance -> {
            if (stepModelId.contains(instance.getProcedureStepModelId())){
                instance.setTaskResult(this.signAll);
            }
        });
    }
}
