package com.bmos.unit.config;

import com.bmos.unit.runner.UnitCacheRunner;
import com.bmos.unit.service.UnitCache;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UnitAutoConfiguration
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/27 00:04
 */
@Configuration
@EnableFeignClients(basePackages = "com.bmos.unit")
public class UnitAutoConfiguration {
    @Bean
    public UnitCache unitCache() {
        return new UnitCache();
    }

    @Bean
    public UnitCacheRunner unitCacheRunner(UnitCache unitCache) {
        return new UnitCacheRunner(unitCache);
    }

}
