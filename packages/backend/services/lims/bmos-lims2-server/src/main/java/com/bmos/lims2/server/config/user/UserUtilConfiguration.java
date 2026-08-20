package com.bmos.lims2.server.config.user;

import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.cache.redis.RedisService;
import com.bmos.lims2.server.platform.util.UserUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserUtilConfiguration implements CommandLineRunner {

    @Resource
    private RedisService redisService;

    @Resource
    private PlatformApiAdaptor platformApiAdaptor;

    @Override
    public void run(String... args) {
        UserUtils.init(platformApiAdaptor, redisService);
    }
}
