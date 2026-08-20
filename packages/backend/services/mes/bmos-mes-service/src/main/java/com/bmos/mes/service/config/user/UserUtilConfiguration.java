package com.bmos.mes.service.config.user;

import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.cache.redis.RedisService;
import com.bmos.mes.service.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserUtilConfiguration implements CommandLineRunner {

    @Autowired
    private RedisService redisService;

    @Autowired
    private PlatformApiAdaptor platformApiAdaptor;

    @Override
    public void run(String... args) {
        UserUtils.init(platformApiAdaptor, redisService);
    }
}
