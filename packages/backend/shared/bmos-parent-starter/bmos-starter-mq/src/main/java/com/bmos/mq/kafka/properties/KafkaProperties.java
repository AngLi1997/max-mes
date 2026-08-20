package com.bmos.mq.kafka.properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.kafka")
public class KafkaProperties {
    private String bootstrapServer;
    private Consumer<?,?> consumer;
    private Producer<?,?> producer;


    public String getBootstrapServer() {
        return bootstrapServer;
    }

    public void setBootstrapServer(String bootstrapServer) {
        this.bootstrapServer = bootstrapServer;
    }

    public Consumer<?, ?> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<?, ?> consumer) {
        this.consumer = consumer;
    }

    public Producer<?, ?> getProducer() {
        return producer;
    }

    public void setProducer(Producer<?, ?> producer) {
        this.producer = producer;
    }
}
