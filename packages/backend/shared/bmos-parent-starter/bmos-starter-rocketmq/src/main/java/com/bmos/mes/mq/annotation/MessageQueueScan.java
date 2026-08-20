package com.bmos.mes.mq.annotation;

import com.bmos.mes.mq.spring.MessageQueueRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author yigaohui
 * @Description message queue scan
 * @Date 2023/7/21 10:08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MessageQueueRegistrar.class)
public @interface MessageQueueScan {
    String [] value();
}
