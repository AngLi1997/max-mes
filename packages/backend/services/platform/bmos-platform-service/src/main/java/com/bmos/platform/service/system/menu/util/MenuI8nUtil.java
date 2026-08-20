package com.bmos.platform.service.system.menu.util;


import com.bmos.common.util.i18n.I18nUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class MenuI8nUtil {

    private static MessageSource messageSource;

    public static void setMessageSource(MessageSource messageSource){
        MenuI8nUtil.messageSource = messageSource;
    }


    public static String convertById(Long menuId){
        return messageSource.getMessage(String.valueOf(menuId),null,
                I18nUtils.getLocale(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest()));
    }


}
