package com.bmos.platform.service.equipment.expire;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.expire.listener.ExpireListener;
import com.bmos.expire.properties.ExpireMessage;
import com.bmos.expire.properties.ExpireMessageProperty;
import com.bmos.platform.common.enums.equipment.EquipmentStatusLogChangeType;
import com.bmos.platform.service.equipment.mapper.EquipmentPropertyInfoMapper;
import com.bmos.platform.service.equipment.model.EquipmentPropertyInfo;
import com.bmos.platform.service.equipment.service.EquipmentInfoService;
import com.bmos.platform.service.equipment.service.EquipmentStatusHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class EquipmentExpireListener implements ExpireListener {

    @Autowired
    EquipmentInfoService equipmentInfoService;

    @Autowired
    EquipmentPropertyInfoMapper equipmentPropertyInfoMapper;

    @Autowired
    EquipmentStatusHandler equipmentStatusHandler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onExpire(ExpireMessageProperty message) {
        ExpireMessage messageBody = message.getExpireMessage();
        Long uniqueId = messageBody.getUniqueId();
        EquipmentPropertyInfo equipmentPropertyInfo = equipmentPropertyInfoMapper.selectById(uniqueId);
        if (ObjectUtil.isEmpty(equipmentPropertyInfo)) {
            return;
        }
        if (!equipmentPropertyInfo.getFinishStatus()) {
            return;
        }
        String actualValue = equipmentPropertyInfo.getActualValue();
        // 此为过期时间
        if (StrUtil.isEmpty(actualValue)) {
            return;
        }
        equipmentPropertyInfo.setFinishStatus(Boolean.FALSE);
        equipmentPropertyInfo.setActualValue(null);
        equipmentPropertyInfoMapper.updateById(equipmentPropertyInfo);

        // 重新计算设备状态
        equipmentInfoService.analyseEquipmentStatus(equipmentPropertyInfo.getEquipmentId(), EquipmentStatusLogChangeType.EXPIRE);
    }

    Long convert2TimeStamp(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }
}
