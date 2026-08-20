package com.bmos.mes.mq.producer;

import com.alibaba.fastjson.JSONObject;
import com.bmos.mes.mq.spring.SpringContextUtil;
import com.bmos.mes.mq.config.MqConfig;
import com.bmos.mes.mq.exception.MqException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Objects;

/**
 * @Author yigaohui
 * @Description mq producer
 * @Date 2023/7/21 10:45
 */
@Slf4j
@Component
public class OpenSourceRocketMqProducer extends RocketMQTemplate implements MessageQueueProducerStrategy {

    @Override
    public SendResult producer(String topic, MqConfig mqConfig, Object body) {
        Assert.notNull(body, "消息体不能为空");
        RocketMQTemplate rocketTemplate = SpringContextUtil.getBean("rocketMQTemplate", RocketMQTemplate.class);
        DefaultMQProducer producer = rocketTemplate.getProducer();

        HashMap<String, Object> headersMap = new HashMap<>();
        MessageHeaders messageHeaders = new MessageHeaders(headersMap);
        Message<Object> message = MessageBuilder.createMessage(body, messageHeaders);

        setMessageConverter(rocketTemplate.getMessageConverter());
        Message<?> msg = super.doConvert(message.getPayload(), message.getHeaders(), null);
        org.apache.rocketmq.common.message.Message rocketMessage =
                RocketMQUtil.convertToRocketMessage(rocketTemplate.getMessageConverter(), "UTF-8", topic, msg);

        String hashKey = null;
        if (mqConfig != null) {
            if (mqConfig.getTag() != null) {
                rocketMessage.setTags(mqConfig.getTag());
            }
            hashKey = mqConfig.getHashKey();
            if (mqConfig.getDelayLevel() != null) {
                rocketMessage.setDelayTimeLevel(mqConfig.getDelayLevel().getCode());
            }
        }

        try {
            SendResult result;
            if (Objects.isNull(hashKey) || hashKey.isEmpty()) {
                result = producer.send(rocketMessage, producer.getSendMsgTimeout());
            } else {
                result = producer.send(rocketMessage, rocketTemplate.getMessageQueueSelector(), hashKey,
                        producer.getSendMsgTimeout());
            }
            log.info("topic：{},send result:{}", topic, JSONObject.toJSONString(result));
            return result;
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            log.error("topic：{}，body:{},mq send error message：{}", topic, JSONObject.toJSONString(body), e);
            throw new MqException("mq发送消息失败", e);
        }
    }
}
