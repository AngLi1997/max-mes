package com.bmos.mes.mq.spring;

import org.springframework.beans.factory.FactoryBean;

import static com.bmos.mes.mq.producer.MessageQueueInvocationHandler.generateProxyClass;

/**
 * @Author yigaohui
 * @Description queue factory bean
 * @Date 2023/7/21 10:48
 */
public class MessageQueueFactoryBean<T> implements FactoryBean<T> {

    public final Class<T> type;

    public MessageQueueFactoryBean(Class<T> type) {
        this.type = type;
    }


    @SuppressWarnings("unchecked")
    @Override
    public T getObject() {
        return (T) generateProxyClass(type);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }
}
