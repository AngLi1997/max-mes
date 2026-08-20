package com.bmos.mq;

import cn.hutool.core.util.StrUtil;

public class MessageQueueTopic {
    private final String topic;

    public MessageQueueTopic(String topic) {
        if (StrUtil.isBlank(topic)) {
            //todo 国际化
            throw new RuntimeException("kafka topic 不能为空");
        }
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
