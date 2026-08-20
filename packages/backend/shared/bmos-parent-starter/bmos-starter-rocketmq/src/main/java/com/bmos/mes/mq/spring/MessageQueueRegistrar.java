package com.bmos.mes.mq.spring;

import com.bmos.mes.mq.annotation.MessageQueueScan;
import com.bmos.mes.mq.annotation.Topic;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;

import java.util.Map;

/**
 * @Author yigaohui
 * @Description bean definition registrar
 * @Date 2023/7/21 10:48
 */
public class MessageQueueRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(MessageQueueScan.class.getName());
        ClassPathMessageQueueScanner classPathMessageQueueScanner = new ClassPathMessageQueueScanner(registry);
        classPathMessageQueueScanner.addIncludeFilter(new AnnotationTypeFilter(Topic.class));
        assert annotationAttributes != null;
        classPathMessageQueueScanner.scan((String[]) annotationAttributes.get("value"));
    }
}