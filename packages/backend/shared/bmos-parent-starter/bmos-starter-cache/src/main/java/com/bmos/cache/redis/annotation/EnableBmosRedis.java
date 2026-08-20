package com.bmos.cache.redis.annotation;

import com.bmos.cache.redis.config.BmosRedisAutoConfiguration;
import com.bmos.cache.redis.redisson.BmosRedissonAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({BmosRedisAutoConfiguration.class, BmosRedissonAutoConfiguration.class})
public @interface EnableBmosRedis {
}
