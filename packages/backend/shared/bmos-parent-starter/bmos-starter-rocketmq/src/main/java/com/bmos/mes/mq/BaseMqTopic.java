package com.bmos.mes.mq;

import com.bmos.mes.mq.config.MqConfig;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * @Author yigaohui
 * @Description mq消息接口
 * @Date 2023/7/21 10:18
 */
public interface BaseMqTopic<T> {
    /**
     * 发送消息
     *
     * @param message 消息体
     */
    default SendResult product(T message) {
        return null;
    }

    /**
     * 自定义发送消息
     *
     * @param mqConfig 消息配置
     * @param message  消息体
     */
    default SendResult product(MqConfig mqConfig, T message) {
        return null;
    }

    /**
     * 消费消息
     *
     * @param message 消息体
     */
    void consume(T message);
}
