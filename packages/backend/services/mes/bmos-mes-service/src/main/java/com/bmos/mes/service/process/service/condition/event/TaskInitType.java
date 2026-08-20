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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 任务初始化类型
 *
 * @author yigaohui
 * @date 2024/7/11
 **/
@Data
@Slf4j
public class TaskInitType extends ConditionChangeType {

    public TaskInitType(Long planId) {
        super(planId);
    }

    @Override
    public ConditionTypeEnum getConditionType() {
        return null;
    }

    /**
     * 初始化不用短路，条件类型不会记录一个任务初始化的条件类型
     * 初始化时只用全量计算一次房间和设备的状态
     *
     * @param changeConditionInstances 条件实例
     * @param context                  上下文
     */
    @Override
    public void calculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                         ConditionCalculateContext context) {
        if (CollUtil.isEmpty(changeConditionInstances)){
            return;
        }
        //查看是否存在物料预定量条件
        List<ProcedureConditionInstance> materialConditionList = CollectionUtils.filterList(changeConditionInstances, item -> StrUtil.equals(item.getConditionType(),
                ConditionTypeEnum.MATERIAL_RESERVE_NUMBER.getValue()));
        if (CollUtil.isNotEmpty(materialConditionList)){
            MaterialReserveType type = new MaterialReserveType(planId,Collections.emptyList());
            type.innerCalculateConditionChange(changeConditionInstances,context);
        }
        List<RoomInfoFeignVO> roomInfoFeignVOList = context.getRoomInfoFeignVOList();
        List<EquipmentInfoFeignVO> equipmentStatusFeignVOList = context.getEquipmentStatusFeignVOList();
        changeConditionInstances.forEach(item -> {
            ConditionDetailVO conditionDetailVO = JSONUtil.toBean(item.getConditionDetails(), ConditionDetailVO.class);
            // 如果是房间状态类型
            if (StrUtil.equals(ConditionTypeEnum.ROOM_STATE.getValue(), item.getConditionType())) {
                if (CollectionUtil.isEmpty(roomInfoFeignVOList)) {
                    log.info("步骤模型id：【{}】初始化时条件【{}】房间列表为空",item.getProcedureStepModelId(),item.getName());
                    return;
                }
                Long roomId = conditionDetailVO.getRoomId();
                Optional<RoomInfoFeignVO> roomInfoFeignVO =
                        roomInfoFeignVOList.stream().filter(roomItem -> roomItem.getId().equals(roomId)).findFirst();
                if (!roomInfoFeignVO.isPresent()) {
                    log.info("步骤模型id：【{}】初始化时条件【{}】没有找到房间【{}】",
                            item.getProcedureStepModelId(),item.getName(),
                            conditionDetailVO.getRoomId());
                    return;
                }
                RoomInfoFeignVO roomInfo = roomInfoFeignVO.get();
                log.info("步骤模型id：【{}】初始化时条件【{}】,房间【{}】状态【{}】,预期状态【{}】",item.getProcedureStepModelId(),item.getName(),
                        conditionDetailVO.getRoomId(),
                        conditionDetailVO.getRoomState(),roomInfo.getStatus());
                item.setTaskResult(conditionDetailVO.getRoomState().equals(roomInfo.getStatus()));
            }
            // 如果是设备状态
            if (StrUtil.equals(ConditionTypeEnum.EQUIPMENT_USE_STATE.getValue(), item.getConditionType())) {
                if (CollectionUtil.isEmpty(equipmentStatusFeignVOList)) {
                    log.info("步骤模型id：【{}】初始化时条件【{}】设备列表为空",item.getProcedureStepModelId(),item.getName());
                    return;
                }
                Long equipmentId = conditionDetailVO.getEquipmentId();
                Optional<EquipmentInfoFeignVO> equipmentInfoFeignVO =
                        equipmentStatusFeignVOList.stream().filter(equipmentItem -> equipmentItem.getId().equals(equipmentId)).findFirst();
                if (!equipmentInfoFeignVO.isPresent()) {
                    log.info("步骤模型id：【{}】初始化时条件【{}】没有找到设备【{}】",item.getProcedureStepModelId(),item.getName(),
                            conditionDetailVO.getEquipmentId());
                    return;
                }
                EquipmentInfoFeignVO equipmentInfo = equipmentInfoFeignVO.get();
                log.info("步骤模型id：【{}】初始化时条件【{}】,设备【{}】状态【{}】,预期状态【{}】",item.getProcedureStepModelId(),item.getName(),
                        conditionDetailVO.getRoomId(),
                        conditionDetailVO.getDeviceState(),equipmentInfo.getStatus());
                item.setTaskResult(conditionDetailVO.getDeviceState().equals(equipmentInfo.getStatus()));
            }
            //初始化签名条件置为true，签名本身可以不做配料称量以及产出
            if (StrUtil.equals(ConditionTypeEnum.OUTPUT_SIGNATURE.getValue(), item.getConditionType()) ||
                StrUtil.equals(ConditionTypeEnum.DOSING_SIGNATURE.getValue(),item.getConditionType())){
                item.setTaskResult(true);
            }
        });
    }

    @Override
    public void innerCalculateConditionChange(List<ProcedureConditionInstance> changeConditionInstances,
                                              ConditionCalculateContext conditionCalculateContext) {
    }
}
