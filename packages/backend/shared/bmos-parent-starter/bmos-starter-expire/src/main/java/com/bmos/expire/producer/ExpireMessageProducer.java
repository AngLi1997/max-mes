package com.bmos.expire.producer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.expire.config.ExpireMessageConfiguration;
import com.bmos.expire.init.ExpireInitProcessor;
import com.bmos.expire.listener.ExpireListener;
import com.bmos.expire.properties.ExpireMessageProperty;
import com.google.common.base.Preconditions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
@ConditionalOnBean(ExpireMessageConfiguration.class)
@AutoConfigureAfter(ExpireMessageConfiguration.class)
public class ExpireMessageProducer implements ApplicationContextAware {

    private static Map<String, List<ExpireMessageProperty>> expireMessagePropertyMap = new ConcurrentHashMap<>();

    @Autowired
    ExpireMessageConfiguration expireMessageConfiguration;

    ApplicationContext applicationContext;

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        applicationContext.getBeansOfType(ExpireInitProcessor.class);
        // 队列进行数据初始化
        Map<String, ExpireInitProcessor> beans = applicationContext.getBeansOfType(ExpireInitProcessor.class);
        if (CollectionUtil.isEmpty(beans)){
            return ;
        }
        List<ExpireMessageProperty> expireInitProcessors = new ArrayList<>();
        for (ExpireInitProcessor value : beans.values()) {
            List<ExpireMessageProperty> init = value.init();
            if (CollectionUtil.isEmpty(init)){
                break;
            }
            expireInitProcessors.addAll(init);
        }
        for (ExpireMessageProperty expireInitProcessor : expireInitProcessors) {
            this.sendAndWeedDuplicates(expireInitProcessor);
        }
        // 开启定时烧苗
        executeExpireMessage();
    }

    /**
     * 定时扫描任务 进行任务执行
     */
    private void executeExpireMessage() {
        // 定义一个线程池
        int coreSize = Runtime.getRuntime().availableProcessors() + 2; // 核心线程数
        int maxSize = coreSize * 2; // 最大线程数
        long keepAliveTime = 60L; // 线程空闲时间，单位秒
        TimeUnit unit = TimeUnit.SECONDS; // 时间单位
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                coreSize,
                maxSize,
                keepAliveTime,
                unit,
                queue
        );
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            if (CollectionUtil.isEmpty(expireMessagePropertyMap)){
                return;
            }
            expireMessagePropertyMap.forEach((key, value) -> {
                for (ExpireMessageProperty expireMessageProperty : value) {
                    executor.execute(() -> {
                        Long currentTime = System.currentTimeMillis() / 1000;
                        if (currentTime < expireMessageProperty.getExpireMessage().getExpireTime()) {
                            return;
                        }
                        synchronized (this){
                            List<ExpireListener> listenerList = expireMessageConfiguration.getExpireListener(expireMessageProperty.getTag());
                            if (CollectionUtil.isEmpty(listenerList)){
                                return ;
                            }
                            for (ExpireListener expireListener : listenerList) {
                                expireListener.onExpire(expireMessageProperty);
                            }
                            // 删除信息
                            value.remove(expireMessageProperty);
                        }
                    });
                }
            });

        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 添加到过期消息集合 并剔除重复消息
     * @param message
     */
    public void sendAndWeedDuplicates(ExpireMessageProperty message){
        validProperty(message);
        List<ExpireMessageProperty> expireMessageProperties = expireMessagePropertyMap.get(message.getTag());
        if (CollectionUtil.isEmpty(expireMessageProperties)){
            expireMessageProperties = CollectionUtil.newArrayList(message);
            List<ExpireMessageProperty> synchronizedList = Collections.synchronizedList(expireMessageProperties);
            expireMessagePropertyMap.put(message.getTag(), synchronizedList);
        } else {
            expireMessageProperties = expireMessageProperties.stream().filter(expireMessageProperty -> !ObjectUtil.equals(expireMessageProperty.getExpireMessage().getUniqueId(), message.getExpireMessage().getUniqueId())).collect(Collectors.toList());
            List<ExpireMessageProperty> synchronizedList = Collections.synchronizedList(expireMessageProperties);
            synchronizedList.add(message);
            expireMessagePropertyMap.put(message.getTag(), synchronizedList);
        }
    }

    /**
     * 添加到过期消息集合 不对重复消息进行剔除
     * @param message
     */
    public void send(ExpireMessageProperty message){
        List<ExpireMessageProperty> expireMessageProperties = expireMessagePropertyMap.get(message.getTag());
        if (CollectionUtil.isEmpty(expireMessageProperties)){
            expireMessageProperties = CollectionUtil.newArrayList(message);
            expireMessagePropertyMap.put(message.getTag(), expireMessageProperties);
        } else {
            expireMessageProperties.add(message);
            expireMessagePropertyMap.put(message.getTag(), expireMessageProperties);
        }
    }

    /**
     * 校验消息属性
     * @param message
     */
    private void validProperty(ExpireMessageProperty message) {
        Preconditions.checkArgument(StrUtil.isNotBlank(message.getTag()), "tag not null");
        Preconditions.checkArgument(Objects.nonNull(message.getExpireMessage()), "message entity not null");
        Preconditions.checkArgument(Objects.nonNull(message.getExpireMessage().getUniqueId()), "message entity uniqueId not null");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
