package com.bmos.mq.kafka.consumer;


import com.bmos.mq.MessageQueueTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.function.Consumer;

/**
 * kafka 消费者
 */
public class KafkaConsumer {

    private final KafkaConsumerFactoryBean kafkaConsumerFactoryBean;

    public KafkaConsumer(KafkaConsumerFactoryBean kafkaConsumerFactoryBean) {
        this.kafkaConsumerFactoryBean = kafkaConsumerFactoryBean;
    }

    public void listen(MessageQueueTopic topic, Consumer<ConsumerRecord<String,String>> consumer){
        kafkaConsumerFactoryBean.register(topic.getTopic(),consumer);
    }
}
