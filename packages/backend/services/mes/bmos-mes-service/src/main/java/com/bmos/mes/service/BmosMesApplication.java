package com.bmos.mes.service;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.bmos.audit.engine.autoconfigure.annotation.EnableAuditEngineAutoConfiguration;
import com.bmos.autoconfigure.EnableBmosAutoConfiguration;
import com.bmos.expression.config.EnableBmosExpressionAutoConfiguration;
import com.bmos.logging.annotation.EnableBmosLogAutoConfiguration;
import com.bmos.orchestrator.engine.autoconfigure.annotation.EnableProcessEngineAutoConfiguration;
import com.bmos.platform.facade.auth.annotation.EnableBmosAuth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@EnableBmosAutoConfiguration
@EnableProcessEngineAutoConfiguration
@EnableAuditEngineAutoConfiguration
@EnableBmosExpressionAutoConfiguration
@EnableFeignClients(basePackages = {"com.bmos.mes", "com.bmos.api","com.bmos.lims2"})
@EnableDiscoveryClient
@EnableBmosAuth
@EnableBmosLogAutoConfiguration
public class BmosMesApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmosMesApplication.class, args);
    }

}
