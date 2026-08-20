package com.bmos.mq.kafka.producer;

import com.bmos.common.util.json.JsonUtils;
import com.bmos.mq.MessageQueueTopic;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(MessageQueueTopic topic, Object payload) {
        kafkaTemplate.send(topic.getTopic(), JsonUtils.toJsonString(payload));
    }
}
