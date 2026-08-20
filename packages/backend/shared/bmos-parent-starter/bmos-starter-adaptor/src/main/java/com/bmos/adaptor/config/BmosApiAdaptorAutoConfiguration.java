package com.bmos.adaptor.config;

import com.bmos.adaptor.file.FileManagerApiAdaptor;
import com.bmos.adaptor.file.feign.FileManagerOpenFeign;
import com.bmos.adaptor.file.impl.FileManagerApiAdaptorImpl;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.adaptor.platform.feign.PlatformOpenFeign;
import com.bmos.adaptor.platform.impl.PlatformApiAdaptorImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableFeignClients(basePackages = "com.bmos.adaptor")
public class BmosApiAdaptorAutoConfiguration {

    @Resource
    private PlatformOpenFeign platformOpenFeign;

    @Resource
    private FileManagerOpenFeign fileManagerOpenFeign;

    @Bean
    @ConditionalOnMissingBean(PlatformApiAdaptor.class)
    public PlatformApiAdaptor platformApiAdaptor() {
        return new PlatformApiAdaptorImpl(platformOpenFeign);
    }

    @Bean
    @ConditionalOnMissingBean(FileManagerApiAdaptor.class)
    public FileManagerApiAdaptor fileManagerApiAdaptor() {
        return new FileManagerApiAdaptorImpl(fileManagerOpenFeign);
    }
}


