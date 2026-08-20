package com.bmos.mes.service.record.redis;

import com.bmos.common.redis.RedisKeyDefine;
import com.bmos.mes.common.utils.Graph;

public interface RecordRedisKeyDefine {

    RedisKeyDefine GRAPH = new RedisKeyDefine("记录图信息保存",
            "bmos:graph:%s",
            RedisKeyDefine.KeyTypeEnum.STRING,
            Graph.class,
            RedisKeyDefine.TimeoutTypeEnum.FOREVER);
}
