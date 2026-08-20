package com.bmos.expire.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.expire.annotation.ExpireMessageListener;
import com.bmos.expire.init.ExpireInitProcessor;
import com.bmos.expire.listener.ExpireListener;
import com.bmos.expire.properties.ExpireMessageProperty;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExpireMessageConfiguration implements ApplicationContextAware {

    private Map<String, List<ExpireListener>> expireListenerMap;

    private ApplicationContext applicationContext;

    @EventListener(ApplicationReadyEvent.class)
    public void initExpireListener() throws Exception {
        expireListenerMap = new HashMap<>();
        Map<String, ExpireListener> expireAllListenerMap = applicationContext.getBeansOfType(ExpireListener.class);
        if (CollectionUtil.isEmpty(expireAllListenerMap)){
            return ;
        }
        for (ExpireListener expireListener : expireAllListenerMap.values()) {
            ExpireMessageListener expireMessageListener = AnnotationUtils.findAnnotation(expireListener.getClass(),ExpireMessageListener.class);
            if (ObjectUtil.isNull(expireMessageListener)){
                continue;
            }
            String tag = expireMessageListener.value();
            if (StrUtil.isEmpty(tag)){
                continue;
            }
            if (expireListenerMap.containsKey(tag)){
                expireListenerMap.get(tag).add(expireListener);
            }else {
                expireListenerMap.put(tag, CollectionUtil.newArrayList(expireListener));
            }
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<ExpireListener> getExpireListener(String tag) {
        return expireListenerMap.get(tag);
    }
}
