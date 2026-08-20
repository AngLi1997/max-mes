package com.bmos.web.swagger.config;

import com.bmos.web.swagger.base.ApiModelEnumPropertyBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.OperationReader;
import springfox.documentation.spring.web.scanners.ApiDescriptionLookup;

import javax.annotation.Resource;

@Primary
@Configuration
@EnableOpenApi
public class Swagger3Configuration {

    @Resource
    @Qualifier("cachingOperationReader")
    private OperationReader operationReader;

    @Resource
    private DocumentationPluginsManager pluginsManager;

    @Resource
    private ApiDescriptionLookup lookup;

    @Bean
    public BmosSwaggerRequestHandlerCombiner combiner() {
        return new BmosSwaggerRequestHandlerCombiner();
    }


    @Bean
    public BmosSwaggerApiDescriptionReader bmosApiDescriptionReader() {
        return new BmosSwaggerApiDescriptionReader(operationReader, pluginsManager, lookup);
    }

    @Bean
    @ConditionalOnMissingBean(BmosSwaggerProcessor.class)
    public BmosSwaggerProcessor bmosSwaggerProcessor() {
        return new BmosSwaggerProcessor();
    }


    @Bean
    @Primary
    @Order(1)
    public ApiModelEnumPropertyBuilder apiModelEnumPropertyBuilder(){
        return new ApiModelEnumPropertyBuilder();
    }
}
