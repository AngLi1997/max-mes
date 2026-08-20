package com.bmos.mes.service.process.service.condition.event;

import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.service.condition.ConditionCalculateContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 产出称量事件
 *
 * @author yigaohui
 * @date 2024/7/10
 **/
@Data
@Slf4j
public class OutputWeighSignType extends ConditionChangeType {

    private Boolean signAll;

    private List<Long> stepModelId;

    public OutputWeighSignType(Long planId, Boolean signAll,List<Long> stepModelId) {
        super(planId);
        this.signAll = signAll;
        this.stepModelId = stepModelId;
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.OUTPUT_SIGNATURE;
    }

    @Override
    public void innerCalculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                         ConditionCalculateContext conditionCalculateContext) {
        log.info("计划id【{}】的产出签名发生变化，当前签名：【{}】",this.planId,this.signAll);
        changeConditionInstances.forEach(instance -> {
            if (stepModelId.contains(instance.getProcedureStepModelId())){
                instance.setTaskResult(this.signAll);
            }
        });
    }
}
