package com.bmos.mes.service.process.service.condition.event;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.service.condition.ConditionCalculateContext;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 任务完成事件
 *
 * @author yigaohui
 * @date 2024/7/10
 **/
@Data
@Slf4j
public class TaskCompleteType extends ConditionChangeType {
    private Long taskNodeId;

    public TaskCompleteType(Long planId, Long taskNodeId) {
        super(planId);
        this.taskNodeId = taskNodeId;
    }


    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.TASK_NODE_COMPLETE;
    }

    @Override
    public void innerCalculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                              ConditionCalculateContext conditionCalculateContext) {
        changeConditionInstances.forEach(item -> {
            ConditionDetailVO conditionDetailVO = JSONUtil.toBean(item.getConditionDetails(), ConditionDetailVO.class);
            if (this.taskNodeId.equals(conditionDetailVO.getTaskNodeId())) {
                log.info("计划id：【{}】步骤模型id:【{}】条件【{}】配置的任务【{}】完成条件完成", this.planId, item.getProcedureStepModelId(),
                        item.getName(), conditionDetailVO.getTaskNodeName());
                item.setTaskResult(true);
            }
        });
    }
}
