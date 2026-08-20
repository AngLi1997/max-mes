package listener;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.bmos.common.exception.BmosException;
import error.I18nResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import property.*;
import resource.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * 当NACOS配置刷新时需要进行监听
 */
@Component
public class BmosRefreshScopeListener {

    private static final Logger log = LoggerFactory.getLogger(BmosRefreshScopeListener.class);

    @Resource
    MessageResourceRegister messageResourceRegister;

    @EventListener
    public void handleRefreshScopeRefreshedEvent(RefreshScopeRefreshedEvent event) throws Exception {
        // 在这里执行自定义操作
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>RefreshScope start refreshing!");
        messageResourceRegister.propertyChange();
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>RefreshScope has been refreshed!");
    }

    public static class BmosI18nListener implements Listener {

        private final I18nMessageResource messageSource;

        private final Locale locale;

        private final I18nProperties.NacosI18nLocations nacosI18nLocations;


        public BmosI18nListener(Locale locale, I18nMessageResource messageSource, I18nProperties.NacosI18nLocations nacosI18nLocations) {
            this.locale = locale;
            this.messageSource = messageSource;
            this.nacosI18nLocations = nacosI18nLocations;
        }

        @Override
        public Executor getExecutor() {
            return null;
        }

        @Override
        public void receiveConfigInfo(String configInfo) {
            try{
                messageSource.loadPropertiesByConfig(configInfo, nacosI18nLocations, locale);
            } catch (Exception e){
                log.error("国际化配置刷新失败", e);
                throw new BmosException(I18nResponseItem.RESOURCE_REFRESH_LOAD_ERROR);
            }

        }
    }

}
