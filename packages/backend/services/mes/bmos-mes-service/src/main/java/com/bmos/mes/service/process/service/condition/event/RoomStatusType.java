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
 * 房间状态变更事件
 *
 * @author yigaohui
 * @date 2024/7/10
 **/
@Data
@Slf4j
public class RoomStatusType extends ConditionChangeType {

    private Long roomId;

    private Integer roomState;

    public RoomStatusType(Long planId, Long roomId, Integer roomState) {
        super(planId);
        this.roomId = roomId;
        this.roomState = roomState;
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return ConditionTypeEnum.ROOM_STATE;
    }

    @Override
    public void innerCalculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                              ConditionCalculateContext conditionCalculateContext) {
        changeConditionInstances.forEach(item -> {
            ConditionDetailVO conditionDetailVO = JSONUtil.toBean(item.getConditionDetails(), ConditionDetailVO.class);
            if (this.getRoomId().equals(conditionDetailVO.getRoomId())) {
                log.info("步骤模型id:【{}】条件【{}】配置的房间【{}】状态发生变更，当前房间状态【{}】，预期房间状态【{}】", item.getProcedureStepModelId(),
                        item.getName(), conditionDetailVO.getRoomName(), conditionDetailVO.getRoomState(),
                        this.roomState);
                item.setTaskResult(conditionDetailVO.getRoomState().equals(this.roomState));
            }
        });
    }
}
