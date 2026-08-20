package com.bmos.logging.config;

import com.bmos.common.util.i18n.I18nUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "log-properties")
@Data
public class LogPropertiesConfig {

    private Map<String, Map<String, String>> localeMap;

    public Map<String, String> getLocalMenuMap() {
        if (localeMap == null) {
            return new HashMap<>();
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        Locale locale = I18nUtils.getLocale(request);
        String languageTag = locale.toLanguageTag();
        return localeMap.getOrDefault(languageTag, new HashMap<>());
    }

}
