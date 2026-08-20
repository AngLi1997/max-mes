package com.bmos.platform.facade.auth.config;

import com.bmos.adaptor.active.ActiveApiAdaptor;
import com.bmos.adaptor.config.BmosApiAdaptorAutoConfiguration;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.cache.redis.RedisService;
import com.bmos.cache.redis.config.BmosRedisAutoConfiguration;
import com.bmos.platform.facade.auth.feign.ActiveValidFeign;
import com.bmos.platform.facade.auth.interceptor.TokenValidateInterceptor;
import com.bmos.platform.facade.auth.properties.BmosAuthProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
@EnableFeignClients("com.bmos.platform.facade")
@ConditionalOnClass({BmosRedisAutoConfiguration.class, BmosApiAdaptorAutoConfiguration.class})
@EnableConfigurationProperties(BmosAuthProperties.class)
public class BmosAuthAutoConfiguration implements WebMvcConfigurer {

    @Resource
    private BmosAuthProperties bmosAuthProperties;

    @Resource
    @Lazy
    private PlatformApiAdaptor platformApiAdaptor;

    @Resource
    @Lazy
    private ActiveApiAdaptor activeApiAdaptor;

    @Resource
    @Lazy
    private ActiveValidFeign activeValidFeign;

    @Resource
    RedisService redisService;

    @Bean
    public TokenValidateInterceptor tokenValidateInterceptor() {
        return new TokenValidateInterceptor(bmosAuthProperties, platformApiAdaptor, redisService, activeApiAdaptor, activeValidFeign);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenValidateInterceptor()).addPathPatterns("/**").order(1);
    }

}
