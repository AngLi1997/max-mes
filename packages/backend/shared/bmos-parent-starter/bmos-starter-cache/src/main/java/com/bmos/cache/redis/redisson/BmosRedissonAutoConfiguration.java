package com.bmos.cache.redis.redisson;

import com.bmos.cache.redis.lock.DistributedLockAspect;
import com.bmos.cache.redis.stabilization.ApiStabilizationAspect;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.io.IOException;


@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RedisTemplate.class})
@EnableConfigurationProperties(RedissonProperties.class)
public class BmosRedissonAutoConfiguration {

    private final Logger log = LoggerFactory.getLogger(BmosRedissonAutoConfiguration.class);

    @Autowired
    private RedissonProperties redissonProperties;

    @Autowired
    private ApplicationContext ctx;

    @Bean
    public RedissonClient redisson() throws IOException {
        log.info("========== 初始化 redisson ===========");
        String config = redissonProperties.getConfig();
        if (!StringUtils.hasText(config)) {
            throw new RuntimeException("未找到 redisson 配置");
        }
        return Redisson.create(Config.fromJSON(config));
    }

    @Bean
    public DistributedLockAspect distributedLockAspect(RedissonClient redissonClient) {
        return new DistributedLockAspect(redissonClient);
    }

    @Bean
    public ApiStabilizationAspect redissonService(RedissonClient redissonClient) {
        return new ApiStabilizationAspect(redissonClient);
    }
}
