package com.bmos.mq.kafka.config;

import com.bmos.mq.kafka.properties.KafkaProperties;
import com.bmos.mq.kafka.consumer.KafkaConsumerFactoryBean;
import com.bmos.mq.kafka.producer.KafkaProducer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;

@EnableKafka
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class BmosKafkaConfiguration {

    @Resource
    private KafkaProperties kafkaProperties;

    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;


    @Bean
    public KafkaConsumerFactoryBean kafkaListenerFactoryBean() {
        return new KafkaConsumerFactoryBean(kafkaProperties);
    }

    @Bean
    public KafkaProducer kafkaProducer(){
        return new KafkaProducer(kafkaTemplate);
    }
}
