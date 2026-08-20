package com.bmos.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.bmos.mybatis.handler.DefaultDBFieldHandler;
import com.bmos.mybatis.interceptor.EscapeUnderlineSelectInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MybatisAutoConfiguration {
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler(){
        return new DefaultDBFieldHandler(); // 自动填充参数类
    }

    @Bean
    public DefaultSqlInjector mySqlInjector(){
        return new MySqlInjector(); // 自动填充参数类
    }

    @Bean
    public CustomIdGenerator customIdGenerator() {
        return new CustomIdGenerator();
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new EscapeUnderlineSelectInterceptor());
        return interceptor;
    }
}
