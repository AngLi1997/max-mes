package com.bmos.common.util.i18n;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Optional;

/**
 * @author yigaohui
 * @date 2022/9/5 12:31
 */
@Slf4j
public class LocaleUtil {

    public static Locale getCurrentLocale() {

        Optional<HttpServletRequest> requestOptional = getOptionalRequest();
        return requestOptional.map(req -> getCurrentLocale(req)).orElse(Locale.getDefault());
    }

    public static Locale getCurrentLocale(HttpServletRequest request) {
        LocaleResolver localeResolver = (SpringUtil.getBean(LocaleResolver.class));
        return localeResolver.resolveLocale(request);
    }

    public static HttpServletRequest getRequiredRequest() {
        return getOptionalRequest().orElseThrow(() -> {
            return new IllegalArgumentException("request required");
        });
    }

    public static Optional<HttpServletRequest> getOptionalRequest() {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(servletRequestAttributes == null ? null : servletRequestAttributes.getRequest());
    }
}
