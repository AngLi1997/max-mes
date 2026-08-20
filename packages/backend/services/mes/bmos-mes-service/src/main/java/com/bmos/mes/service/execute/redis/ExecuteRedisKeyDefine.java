package com.bmos.mes.service.execute.redis;

import com.bmos.common.redis.RedisKeyDefine;

public interface ExecuteRedisKeyDefine {

    RedisKeyDefine LOCK_STEP = new RedisKeyDefine("用户Token对应UserId信息缓存",
            "bmos:execute:lock:%s",
            RedisKeyDefine.KeyTypeEnum.STRING,
            String.class,
            RedisKeyDefine.TimeoutTypeEnum.FOREVER);
}
