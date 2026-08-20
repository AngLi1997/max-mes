package com.bmos.unit.constant;

import com.bmos.cache.redis.objects.CommonUnit;
import com.bmos.common.redis.RedisKeyDefine;

import java.time.Duration;

import static com.bmos.common.redis.RedisKeyDefine.KeyTypeEnum;

public interface UnitRedisKeyDefine {

    RedisKeyDefine UNIT_INFO_CACHE = new RedisKeyDefine("单位信息缓存",
            "bmos:unit:info:%s",
            KeyTypeEnum.STRING,
            CommonUnit.class,
            Duration.ofHours(2));
}
