package com.bmos.platform.service.equipment.expire;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.expire.init.ExpireInitProcessor;
import com.bmos.expire.properties.ExpireMessage;
import com.bmos.expire.properties.ExpireMessageProperty;
import com.bmos.platform.common.GlobalConstants;
import com.bmos.platform.common.enums.equipment.PropertyTypeEnum;
import com.bmos.platform.common.enums.expire.ExpireListenerConstants;
import com.bmos.platform.service.equipment.mapper.EquipmentPropertyInfoMapper;
import com.bmos.platform.service.equipment.model.EquipmentPropertyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class EquipmentExpireInitialization implements ExpireInitProcessor {

    @Autowired
    EquipmentPropertyInfoMapper equipmentPropertyInfoMapper;

    @Override
    public List<ExpireMessageProperty> init() {
        List<EquipmentPropertyInfo> equipmentPropertyInfoList = equipmentPropertyInfoMapper.selectAllByFinisehd(PropertyTypeEnum.EQUIPMENT_STATUS.getCode(), Boolean.TRUE);
        List<ExpireMessageProperty> expireMessageProperties = new ArrayList<>();
        for (EquipmentPropertyInfo equipmentPropertyInfo : equipmentPropertyInfoList) {
            if (StrUtil.isEmpty(equipmentPropertyInfo.getActualValue())) {
                continue;
            }
            ExpireMessageProperty expireMessageProperty = new ExpireMessageProperty();
            expireMessageProperty.setTag(ExpireListenerConstants.EQUIPMENT_EXPIRE);
            ExpireMessage expireMessage = new ExpireMessage();
            expireMessage.setUniqueId(equipmentPropertyInfo.getId());
            expireMessage.setExpireTime(convert2TimeStamp(LocalDateTimeUtil.parse(equipmentPropertyInfo.getActualValue(), GlobalConstants.DATE_TIME_FORMAT)));
            expireMessageProperty.setExpireMessage(expireMessage);
            expireMessageProperties.add(expireMessageProperty);
        }
        return expireMessageProperties;
    }

    Long convert2TimeStamp(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }
}
