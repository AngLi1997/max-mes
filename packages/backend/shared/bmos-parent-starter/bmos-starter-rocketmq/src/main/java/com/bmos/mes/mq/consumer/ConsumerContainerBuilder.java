package com.bmos.mes.mq.consumer;

import com.bmos.mes.mq.spring.SpringContextUtil;
import com.bmos.mes.mq.annotation.Consumer;

/**
 * @Author yigaohui
 * @Description builder
 * @Date 2023/7/21 10:09
 */
public interface ConsumerContainerBuilder {
    /**
     * 注册容器
     *
     * @param targetClass 目标类
     * @param beanName    bean名称
     * @param bean        消费者的bean
     * @param topic       topic名字
     * @param groupId     消费组id
     * @param consumer    消费者配置
     */
    void doRegisterContainer(Class<?> targetClass, String beanName, Object bean, String topic, String groupId,
                             Consumer consumer);

    /**
     * 注册容器
     *
     * @param targetClass 目标类
     * @param beanName    bean名称
     * @param bean        消费者的bean
     * @param topic       topic名字
     * @param consumer    消费者配置
     */
    default void registerContainer(Class<?> targetClass, String beanName, Object bean, String topic,
                                   Consumer consumer) {
        String groupId = SpringContextUtil.getSpringValue(consumer.groupId());
        doRegisterContainer(targetClass, beanName, bean, topic, groupId, consumer);
    }
}
