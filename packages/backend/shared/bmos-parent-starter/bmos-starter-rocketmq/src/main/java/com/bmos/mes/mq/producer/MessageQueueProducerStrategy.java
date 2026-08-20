package com.bmos.mes.mq.producer;

import com.bmos.mes.mq.config.MqConfig;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * @Author yigaohui
 * @Description mq producer
 * @Date 2023/7/21 10:40
 */
public interface MessageQueueProducerStrategy {
    /**
     * mq生产者
     *
     * @param topic    消息主题
     * @param mqConfig 消息参数配置
     * @param body     消息内容
     */
    SendResult producer(String topic, MqConfig mqConfig, Object body);
}
