package com.bmos.mq.kafka.annotation;


import com.bmos.mq.kafka.config.BmosKafkaConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(BmosKafkaConfiguration.class)
public @interface EnableBmosKafka {

}
