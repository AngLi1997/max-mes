package com.bmos.cache.redis.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {


    /**
     * 唯一标识，优先级最高，如果不为空，不会进行后续 表达式 的判断
     */
    String key() default "";

    /**
     * 表达式 ：参数名.属性
     * 如: param.name
     *    id
     */
    String expression() default "";

    /**
     * 锁时长
     */
    long duration() default 1L;

    /**
     * 时长单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
