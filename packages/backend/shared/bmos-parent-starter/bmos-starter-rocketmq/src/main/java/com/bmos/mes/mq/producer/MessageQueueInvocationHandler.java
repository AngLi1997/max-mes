package com.bmos.mes.mq.producer;

import com.bmos.mes.mq.spring.SpringContextUtil;
import com.bmos.mes.mq.annotation.Topic;
import com.bmos.mes.mq.config.MqConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author yigaohui
 * @Description rocketmq template封装
 * @Date 2023/7/21 10:42
 */
@Slf4j
public class MessageQueueInvocationHandler extends RocketMQTemplate implements InvocationHandler {

    /**
     * 缓存topic 对应的注解
     */
    private static final Map<Class<?>, Topic> MESSAGE_QUEUE_MAP = new ConcurrentHashMap<>();

    private MessageQueueProducerStrategy messageQueueProducerStrategy;

    public static Object generateProxyClass(Class<?> type) {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new MessageQueueInvocationHandler());
    }

    private MessageQueueProducerStrategy getMessageQueueProducerStrategy() {
        if (messageQueueProducerStrategy != null) {
            return messageQueueProducerStrategy;
        }
        messageQueueProducerStrategy = SpringContextUtil.getBean(OpenSourceRocketMqProducer.class);
        return messageQueueProducerStrategy;
    }

    private Topic getMessageQueue(Object proxy) {
        Topic messageQueue;
        if ((messageQueue = MESSAGE_QUEUE_MAP.get(proxy.getClass())) != null) {
            return messageQueue;
        }
        Type[] genericInterfaces = proxy.getClass().getGenericInterfaces();
        Class<?> genericInterface = (Class<?>) genericInterfaces[0];
        Topic annotation = genericInterface.getAnnotation(Topic.class);
        MESSAGE_QUEUE_MAP.put(proxy.getClass(), annotation);
        return annotation;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {

        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        MqConfig mqConfig = null;
        Object body;

        if (args.length == 2) {
            mqConfig = (MqConfig) args[0];
            body = args[1];
        } else if (args.length == 1) {
            body = args[0];
        } else {
            log.warn("mq 发送失败，代理方法有问题");
            return null;
        }

        Topic messageQueue = getMessageQueue(proxy);

        String topic = SpringContextUtil.getSpringValue(messageQueue.value());


        MessageQueueProducerStrategy messageQueueProducer = getMessageQueueProducerStrategy();
        return messageQueueProducer.producer(topic, mqConfig, body);
    }

}