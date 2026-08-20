package com.bmos.mes.service.process.service.condition.event;

import cn.hutool.json.JSONUtil;
import com.bmos.mes.common.enums.process.task.ConditionTypeEnum;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.service.condition.ConditionCalculateContext;
import com.bmos.mes.service.process.vo.Task.ConditionDetailVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 设备状态变更事件
 *
 * @author yigaohui
 * @date 2024/7/10
 **/
@Data
@Slf4j
public class EquipmentStatusType extends ConditionChangeType {
    private Long equipmentId;

    private Integer equipmentState;

    public EquipmentStatusType(Long planId, Long equipmentId, Integer equipmentState) {
        super(planId);
        this.equipmentId = equipmentId;
        this.equipmentState = equipmentState;
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.EQUIPMENT_USE_STATE;
    }

    /**
     * 房间状态变更事件类型
     *
     * @param changeConditionInstances  条件实例
     * @param conditionCalculateContext 上下文
     */
    @Override
    public void innerCalculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                              ConditionCalculateContext conditionCalculateContext) {
        changeConditionInstances.forEach(item -> {
            ConditionDetailVO conditionDetailVO = JSONUtil.toBean(item.getConditionDetails(), ConditionDetailVO.class);
            if (this.equipmentId.equals(conditionDetailVO.getEquipmentId())) {
                log.info("步骤模型id:【{}】条件【{}】配置的设备【{}】状态发生变更，当前设备状态【{}】，预期设备状态【{}】", item.getProcedureStepModelId(),
                        item.getName(), conditionDetailVO.getEquipmentName(), conditionDetailVO.getDeviceState(),
                        this.equipmentState);
                item.setTaskResult(conditionDetailVO.getDeviceState().equals(this.equipmentState));
            }
        });
    }
}
