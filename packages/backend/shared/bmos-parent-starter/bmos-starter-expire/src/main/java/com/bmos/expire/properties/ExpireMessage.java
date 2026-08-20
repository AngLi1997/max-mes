package com.bmos.expire.properties;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExpireMessage {

    /**
     * 消息唯一id, 通过此校验是否是同一个过期实例
     */
    private Long uniqueId;

    /**
     * 过期时间 以秒为单位 时间戳
     */
    private Long expireTime;

    /**
     * 过期消息
     */
    private Object message;

}
