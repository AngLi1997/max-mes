package com.bmos.mes.mq.config;

import com.bmos.mes.mq.annotation.MessageQueueScan;
import com.bmos.mes.mq.consumer.MqConsumerContainerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author yigaohui
 * @Description mq 自动装配类
 * @Date 2023/7/21 10:52
 */
@Configuration
@MessageQueueScan("com.bmos.mes.**.mq.**")
@Import({ OpenSourceMqConfig.class, MqConsumerContainerConfig.class})
public class RocketMqAutoConfig {
}
