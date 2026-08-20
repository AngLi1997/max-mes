package com.bmos.lims2.server.eln.record.redis;

import com.bmos.common.redis.RedisKeyDefine;
import com.bmos.lims2.server.eln.record.util.Graph;

public interface RecordRedisKeyDefine {

    RedisKeyDefine GRAPH = new RedisKeyDefine("记录图信息保存",
            "bmos:graph:%s",
            RedisKeyDefine.KeyTypeEnum.STRING,
            Graph.class,
            RedisKeyDefine.TimeoutTypeEnum.FOREVER);
}
