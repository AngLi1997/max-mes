package com.bmos.platform.service.factory.expire;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.expire.annotation.ExpireMessageListener;
import com.bmos.expire.listener.ExpireListener;
import com.bmos.expire.properties.ExpireMessage;
import com.bmos.expire.properties.ExpireMessageProperty;
import com.bmos.mq.listener.Event.StateEvent;
import com.bmos.mq.listener.enums.StateEventTypeEnum;
import com.bmos.platform.common.enums.expire.ExpireListenerConstants;
import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import com.bmos.platform.service.factory.mapper.FactoryRoomMapper;
import com.bmos.platform.service.factory.model.FactoryRoom;
import com.bmos.platform.service.feign.CommonFeignClient;
import com.bmos.platform.service.feign.CommonFeignClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@ExpireMessageListener(ExpireListenerConstants.ROOM_EXPIRE)
public class ExpireRoomListener implements ExpireListener {

    @Autowired
    private FactoryRoomMapper roomMapper;

    @Autowired
    private CommonFeignClientFactory commonFeignClientFactory;

    @Override
    public void onExpire(ExpireMessageProperty message) {
        ExpireMessage messageBody = message.getExpireMessage();
        Long uniqueId = messageBody.getUniqueId();
        FactoryRoom factoryRoom = roomMapper.selectById(uniqueId);
        if (ObjectUtil.isNull(factoryRoom)){
            return ;
        }
        LocalDateTime expireTime = factoryRoom.getExpireTime();
        if (ObjectUtil.isEmpty(expireTime)){
            return ;
        }
        if (convert2TimeStamp(expireTime) > messageBody.getExpireTime()){
            return ;
        }
        if (ObjectUtil.equals(RoomStatusEnum.BE_CLEANED.getCode(),factoryRoom.getStatus())){
            return ;
        }
        if (ObjectUtil.equals(RoomStatusEnum.CLEANED.getCode(),factoryRoom.getStatus())){
            factoryRoom.setStatus(RoomStatusEnum.BE_CLEANED.getCode());
            factoryRoom.setExpireTime(null);
            roomMapper.updateById(factoryRoom);
            StateEvent stateEvent = new StateEvent();
            stateEvent.setId(factoryRoom.getId());
            stateEvent.setState(String.valueOf(factoryRoom.getStatus()));
            stateEvent.setType(StateEventTypeEnum.ROOM.getCode());
            CommonFeignClient feignClient = commonFeignClientFactory.getFeignClient("bmos-mes-service");
            feignClient.conditionUpdate(stateEvent);

        }
        // 房间状态变更记录操作日志

    }

    Long convert2TimeStamp(LocalDateTime localDateTime){
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }
}
