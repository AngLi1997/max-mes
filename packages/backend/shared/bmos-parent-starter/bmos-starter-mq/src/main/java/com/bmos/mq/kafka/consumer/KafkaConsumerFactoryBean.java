package com.bmos.mq.kafka.consumer;

import com.bmos.mq.kafka.properties.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class KafkaConsumerFactoryBean {
    private final ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory;
    private final KafkaProperties kafkaProperties;

    public KafkaConsumerFactoryBean(KafkaProperties kafkaProperties) {
        this.kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        this.kafkaProperties = kafkaProperties;
    }

    public void register(String topic, Consumer<ConsumerRecord<String,String>> consumer) {
        ContainerProperties containerProperties = new ContainerProperties(topic);
        containerProperties.setMessageListener((MessageListener<String, String>) consumer::accept);

        ConsumerFactory<String, String> consumerFactory = createConsumerFactory();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        kafkaListenerContainerFactory.createContainer(Objects.requireNonNull(containerProperties.getTopics())).start();
    }


    private ConsumerFactory<String, String> createConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServer());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().groupMetadata().groupId());
        //todo 配置文件？
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // 设置你的JSON包扫描路径
        return new DefaultKafkaConsumerFactory<>(config);
    }
}
