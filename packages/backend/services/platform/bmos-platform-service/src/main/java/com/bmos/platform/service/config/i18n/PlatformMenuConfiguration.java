package com.bmos.platform.service.config.i18n;

import com.bmos.platform.service.system.menu.util.MenuI8nUtil;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;


@lombok.extern.slf4j.Slf4j
@Configuration
@Slf4j
public class PlatformMenuConfiguration {

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        MenuI8nUtil.setMessageSource(messageSource);
    }

}
