package com.bmos.platform.service.factory.expire;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.expire.init.ExpireInitProcessor;
import com.bmos.expire.properties.ExpireMessage;
import com.bmos.expire.properties.ExpireMessageProperty;
import com.bmos.platform.common.enums.expire.ExpireListenerConstants;
import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import com.bmos.platform.service.factory.mapper.FactoryRoomMapper;
import com.bmos.platform.service.factory.model.FactoryRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoomExpireInitialization implements ExpireInitProcessor {

    @Autowired
    FactoryRoomMapper roomMapper;

    @Override
    public List<ExpireMessageProperty> init() {
        List<FactoryRoom> roomList = roomMapper.selectByStatus(RoomStatusEnum.CLEANED.getCode());
        List<ExpireMessageProperty> expireMessageProperties = new ArrayList<>();
        for (FactoryRoom factoryRoom : roomList) {
            if (ObjectUtil.isNull(factoryRoom.getExpireTime())) {
                continue;
            }
            ExpireMessageProperty expireMessageProperty = new ExpireMessageProperty();
            expireMessageProperty.setTag(ExpireListenerConstants.ROOM_EXPIRE);
            ExpireMessage expireMessage = new ExpireMessage();
            expireMessage.setUniqueId(factoryRoom.getId());
            expireMessage.setExpireTime(convert2TimeStamp(factoryRoom.getExpireTime()));
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
