package com.bmos.mes.service.process.service.condition.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.service.condition.ConditionCalculateContext;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.factory.vo.RoomInfoFeignVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * 工序重做类型
 *
 * @author yigaohui
 * @date 2024/7/11
 **/
@Data
@Slf4j
public class ProcedureRestartType extends ConditionChangeType {
    private List<Long> stepModelId;

    public ProcedureRestartType(Long planId,List<Long> stepModelId) {
        super(planId);
        this.stepModelId = stepModelId;
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return null;
    }

    /**
     * 工序重做
     *
     * @param changeConditionInstances 条件实例
     * @param context                  上下文
     */
    @Override
    public void calculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                         ConditionCalculateContext context) {
        //找到当前计划下的所有条件
        List<ProcedureConditionInstance> conditionInstanceList = CollectionUtils.filterList(changeConditionInstances, item ->
                item.getPlanId().equals(planId));
        if (CollUtil.isEmpty(conditionInstanceList)) {
            return;
        }
        //将当前工序下的条件完成状态全部置为false
        conditionInstanceList.forEach(item -> {
            //将绑定了工序下的步骤与任务条件置为false
            ConditionDetailVO conditionDetailVO = JSONUtil.toBean(item.getConditionDetails(), ConditionDetailVO.class);
            if (stepModelId.contains(conditionDetailVO.getStepId()) || stepModelId.contains(conditionDetailVO.getTaskNodeId())) {
                item.setTaskResult(false);
            }
        });
        //调用taskInitType方法更新重做工序下条件的房间状态以及设备状态
        TaskInitType initType = new TaskInitType(planId);
        initType.calculateConditionChange(conditionInstanceList, context);
    }

    @Override
    public void innerCalculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                              ConditionCalculateContext conditionCalculateContext) {
    }
}
