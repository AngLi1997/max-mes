package com.bmos.platform.service.equipment.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.service.equipment.model.EquipmentInfo;
import com.bmos.platform.service.equipment.service.EquipmentStatusHandler;
import com.bmos.platform.service.equipment.service.data.EquipmentStatusData;
import com.bmos.platform.service.equipment.service.data.EquipmentTagStatusData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class EquipmentStatusHandlerImpl implements EquipmentStatusHandler {

    @Override
    public EquipmentStatusData analyzeEffectiveEquipment(List<EquipmentTagStatusData> equipmentTagStatusDataList, Long equipmentId) {
        EquipmentStatusData equipmentStatusData = new EquipmentStatusData();
        equipmentStatusData.setEquipmentId(equipmentId);
        if (CollectionUtil.isEmpty(equipmentTagStatusDataList)){
            // 没状态代表可用且没有效期
            equipmentStatusData.setStatus(EquipmentStatusCodeEnum.AVAILABLE.getCode());
            return equipmentStatusData;
        }
        boolean equipmentStatus = true;
        LocalDateTime expireMinDate = null;
        for (EquipmentTagStatusData equipmentTagStatusData : equipmentTagStatusDataList) {
            equipmentStatus = equipmentStatus && equipmentTagStatusData.getFinishStatus();
            if (Objects.isNull(equipmentTagStatusData.getExpireDateTime())){
                // 若有一个设备状态为空 则将设备总过期时间设置为null 因为设备不可用时 设备没有有效期说法
                expireMinDate = null;
                break;
            }
            if (expireMinDate == null || equipmentTagStatusData.getExpireDateTime().isBefore(expireMinDate)){
                expireMinDate = equipmentTagStatusData.getExpireDateTime();
            }
        }
        equipmentStatusData.setStatus(equipmentStatus ? EquipmentStatusCodeEnum.AVAILABLE.getCode() : EquipmentStatusCodeEnum.UNAVAILABLE.getCode());
        equipmentStatusData.setExpireDateTime(expireMinDate);
        return equipmentStatusData;
    }

    @Override
    public void analyzeEffectiveReleaseEquipment(List<EquipmentTagStatusData> equipmentTagStatusDataList, EquipmentInfo equipmentInfo) {
        // 若设备状态为故障 则状态无需变更
        if (Objects.equals(equipmentInfo.getStatus(), EquipmentStatusCodeEnum.FAULT.getCode())
        || Objects.equals(equipmentInfo.getStatus(), EquipmentStatusCodeEnum.OCCUPY.getCode())){
            return;
        }
        EquipmentStatusData equipmentStatusData = analyzeEffectiveEquipment(equipmentTagStatusDataList, equipmentInfo.getId());
        equipmentInfo.setStatus(equipmentStatusData.getStatus());
        equipmentInfo.setExpireDateTime(equipmentStatusData.getExpireDateTime());
    }

}
