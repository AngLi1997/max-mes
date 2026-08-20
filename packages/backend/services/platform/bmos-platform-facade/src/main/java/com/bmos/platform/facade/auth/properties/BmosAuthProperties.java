package com.bmos.platform.facade.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("bmos.auth")
public class BmosAuthProperties {
    /**
     * 白名单
     */
    private List<String> activeUrls = new ArrayList<>();

    /**
     * 白名单
     */
    private List<String> excludeUrls = new ArrayList<>();

    /**
     * 忽略需要续期的url
     */
    private List<String> ignoreAuthUrls = new ArrayList<>();

    /**
     * 是否开启权限
     */
    private Boolean enable = true;

    public List<String> getActiveUrls() {
        return activeUrls;
    }

    public void setActiveUrls(List<String> activeUrls) {
        this.activeUrls = activeUrls;
    }

    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    public Boolean isEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public List<String> getIgnoreAuthUrls() {
        return ignoreAuthUrls;
    }

    public void setIgnoreAuthUrls(List<String> ignoreAuthUrls) {
        this.ignoreAuthUrls = ignoreAuthUrls;
    }
}
