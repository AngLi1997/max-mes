package com.bmos.platform.common.enums;

import com.bmos.common.redis.RedisKeyDefine;

import java.time.Duration;

public interface PlatformRedisKeyDefine {

    RedisKeyDefine EQUIPMENT_APPLY_HEART = new RedisKeyDefine("设备",
            "bmos:platform:equipment:heart:%s",
            RedisKeyDefine.KeyTypeEnum.STRING,
            String.class,
            Duration.ofSeconds(10));
}
