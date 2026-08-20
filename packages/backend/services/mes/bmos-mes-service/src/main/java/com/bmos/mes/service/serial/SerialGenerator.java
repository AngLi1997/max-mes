package com.bmos.mes.service.serial;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.function.Supplier;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/21 18:03
 */
@Component
public class SerialGenerator {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String CARGO_REDIS_LOCK = "CARGO_REDIS_LOCK";

    private static final String CARGO_REDIS_KEY = "CARGO_REDIS_KEY";

    /**
     * 获取下一个业务编号
     *
     * @param refreshFunc 缓存刷新使用，函数应返回数据库当前最新值，无数据返回null
     * @return
     */
    public Long getNextNo(Supplier<Long> refreshFunc) {
        int i = 3;
        while (i > 0) {
            i--;
            try {
                return tryGetNextNo(refreshFunc);
            } catch (Exception e) {
                if (i == 0) {
                    throw e;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    //ignore
                }
            }
        }
        throw new RuntimeException();
    }

    private Long tryGetNextNo(Supplier<Long> refreshFunc) {
        Long redisValueIncr = stringRedisTemplate.opsForValue().increment(CARGO_REDIS_KEY, 1);
        if (redisValueIncr == null) {
            return doRefreshFunc(refreshFunc);
        }
        return redisValueIncr;
    }

    private Long doRefreshFunc(Supplier<Long> refreshFunc) {
        //加锁
        Boolean lockFlag = stringRedisTemplate.opsForValue().setIfAbsent(CARGO_REDIS_LOCK, String.valueOf(System.currentTimeMillis()));
        if (Boolean.TRUE.equals(lockFlag)) {
            try {
                // 获取业务编号
                Long dbValue = refreshFunc.get();
                if (dbValue != null) {
                    stringRedisTemplate.opsForValue().set(CARGO_REDIS_KEY, dbValue.toString());
                    return dbValue;
                }
                return 1L;
            } catch (Exception e) {
                throw new RuntimeException("获取业务编号失败(并发), 请重试");
            } finally {
                // 解锁
                stringRedisTemplate.delete(CARGO_REDIS_LOCK);
            }
        } else {
            throw new RuntimeException("获取业务编号失败(并发), 请重试");
        }
    }
}
