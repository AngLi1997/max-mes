package com.bmos.web.config;

import com.bmos.adaptor.config.BmosApiAdaptorAutoConfiguration;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.CommonEnumSerializer;
import com.bmos.common.base.enums.KeyValueEnum;
import com.bmos.common.serializer.*;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.web.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Configuration
@AutoConfigureAfter({BmosApiAdaptorAutoConfiguration.class})
public class BmosWebAutoConfiguration implements WebMvcConfigurer {

    private final Logger log = LoggerFactory.getLogger(BmosWebAutoConfiguration.class);

    @Resource
    private MessageSource messageSource;

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler(messageSource);
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        /*
         * 1. 新增LocalDateTime序列化
         * 2. 新增Date序列化
         * 3. 新增BigDecimal序列化
         */
        simpleModule
                .addSerializer(BigDecimal.class, BigDecimalSerializer.INSTANCE)
                .addSerializer(Date.class, DateSerializer.INSTANCE)
                .addSerializer(Long.class, LongSerializer.INSTANCE)
                .addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE)
                .addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE)
                .addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE)
                .addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE)
                .addSerializer(KeyValueEnum.class, CommonEnumSerializer.INSTANCE);
        objectMapper.registerModules(simpleModule);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS); // 示例配置
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonUtils.init(objectMapper);
        return objectMapper;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.
                addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:/swagger-ui/index.html");
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        //允许所有域名进行跨域调用
//        config.addAllowedOriginPattern("*");
//        //允许跨越发送cookie
//        config.setAllowCredentials(true);
//        //放行全部原始头信息
//        config.addAllowedHeader("*");
//        //允许所有请求方法跨域调用
//        config.addAllowedMethod("*");
//        // 有效期 1800秒
//        config.setMaxAge(1800L);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }

}
