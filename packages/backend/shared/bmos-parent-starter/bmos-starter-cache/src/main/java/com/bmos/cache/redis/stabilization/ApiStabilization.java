package com.bmos.cache.redis.stabilization;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiStabilization {
    String value();

    long interval() default 3L;
}
