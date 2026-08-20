package com.bmos.autoconfigure;

import annotation.EnableBmosDataSource;
import annotation.EnableBmosLocale;
import com.bmos.adaptor.annotation.EnableBmosAdaptor;
import com.bmos.cache.redis.annotation.EnableBmosRedis;
import com.bmos.mybatis.annotation.EnableBmosMybatis;
import com.bmos.unit.annotation.auto.EnableBmosUnit;
import com.bmos.web.annotation.EnableBmosWeb;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableBmosAdaptor
@EnableBmosWeb
@EnableBmosMybatis
@EnableBmosRedis
@EnableBmosUnit
@EnableBmosDataSource
@EnableBmosLocale
public @interface EnableBmosAutoConfiguration {

}
