package com.bmos.mes.service.config.mcp;

import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import org.noear.solon.web.servlet.SolonServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 21:49
 */
@Configuration
@Slf4j
public class McpServerConfig {

    @PostConstruct
    public void start() {
        log.info("启动mcp服务....");
        Solon.start(McpServerConfig.class, new String[]{"--cfg=mcp-server.yml"});
        log.info("mcp服务启动成功....");
    }

    @PreDestroy
    public void stop() {
        if (Solon.app() != null) {
            Solon.stopBlock(false, Solon.cfg().stopDelay());
        }
    }

    @Bean
    public FilterRegistrationBean<SolonServletFilter> mcpFilterRegistration(){
        FilterRegistrationBean<SolonServletFilter> filter = new FilterRegistrationBean<>();
        filter.setName("SolonFilter");
        filter.addUrlPatterns("/mcp/*");
        filter.setOrder(0);
        filter.setFilter(new SolonServletFilter());
        return filter;
    }
}
