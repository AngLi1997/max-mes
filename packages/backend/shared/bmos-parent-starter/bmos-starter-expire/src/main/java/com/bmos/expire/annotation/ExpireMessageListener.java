package com.bmos.expire.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExpireMessageListener {

    /**
     * 对应的消息存放队列的路径
     * @return
     */
     String value();

}
