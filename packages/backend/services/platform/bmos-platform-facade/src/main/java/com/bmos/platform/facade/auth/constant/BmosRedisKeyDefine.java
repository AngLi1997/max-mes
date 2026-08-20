package com.bmos.platform.facade.auth.constant;

import com.bmos.common.redis.RedisKeyDefine;
import com.bmos.mybatis.dataobject.BaseUserDO;

import java.time.Duration;

import static com.bmos.common.redis.RedisKeyDefine.KeyTypeEnum;

public interface BmosRedisKeyDefine {

    RedisKeyDefine USER_LOGIN_CACHE = new RedisKeyDefine("用户信息缓存",
            "bmos:user:login:%s",
            KeyTypeEnum.STRING,
            String.class,
            Duration.ofHours(2));


    RedisKeyDefine USER_TOKEN_ID_CACHE = new RedisKeyDefine("用户Token对应UserId信息缓存",
            "bmos:user:token:%s",
            KeyTypeEnum.STRING,
            String.class,
            Duration.ofHours(2));

    RedisKeyDefine USER_TOKEN_IP_CACHE = new RedisKeyDefine("用户Token对应ip信息缓存",
            "bmos:user:ip:%s",
            KeyTypeEnum.STRING,
            String.class,
            Duration.ofHours(2));

    RedisKeyDefine USER_INFO_CACHE = new RedisKeyDefine("用户信息缓存",
            "bmos:user:info:%s",
            KeyTypeEnum.STRING,
            BaseUserDO.class,
            RedisKeyDefine.TimeoutTypeEnum.FOREVER);
}
