package com.bmos.platform.common.enums.expire;

import lombok.AllArgsConstructor;
import lombok.Getter;


public interface ExpireListenerConstants {

    /**
     * 房间过期监听器
     */
    String ROOM_EXPIRE = "ROOM_EXPIRE";

    /**
     * 设备过期监听器
     */
    String EQUIPMENT_EXPIRE = "EQUIPMENT_EXPIRE";

    /**
     * 用户解锁过期监听器
     */
    String USER_UNLOCK_EXPIRE = "USER_UNLOCK_EXPIRE";


}
