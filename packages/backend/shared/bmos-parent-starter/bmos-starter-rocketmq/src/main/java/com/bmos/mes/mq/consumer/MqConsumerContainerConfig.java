package com.bmos.mes.mq.consumer;

import com.bmos.mes.mq.BaseMqTopic;
import com.bmos.mes.mq.annotation.Consumer;
import com.bmos.mes.mq.annotation.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.lang.NonNull;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author yigaohui
 * @Description consumer 容器配置
 * @Date 2023/7/21 10:11
 */
@Slf4j
@Configuration
public class MqConsumerContainerConfig implements ApplicationContextAware, SmartInitializingSingleton {

    private ConfigurableApplicationContext applicationContext;

    private final StandardEnvironment environment;

    @Resource
    private ConsumerContainerBuilder consumerContainerBuilder;

    public MqConsumerContainerConfig(StandardEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(Consumer.class).entrySet().stream()
                .filter(entry -> !ScopedProxyUtils.isScopedTarget(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        beans.forEach(this::buildConsumerContainer);
    }

    private void buildConsumerContainer(String beanName, Object bean) {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

        if (!BaseMqTopic.class.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(clazz + " is not instance of " + BaseMqTopic.class.getName());
        }

        Consumer consumerGroupAnnotation = clazz.getAnnotation(Consumer.class);

        Type genericInterface = clazz.getGenericInterfaces()[0];
        Class<?> parameterizedType = (Class<?>) genericInterface;
        Topic messageQueueAnnotation = parameterizedType.getAnnotation(Topic.class);

        String topic = this.environment.resolvePlaceholders(messageQueueAnnotation.value());

        if (consumerContainerBuilder == null) {
            log.warn("消费者未启动");
            return;
        }
        consumerContainerBuilder.registerContainer(clazz, beanName, bean, topic, consumerGroupAnnotation);
    }
}
