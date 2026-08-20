package com.bmos.lims2.web;

import com.bmos.audit.engine.autoconfigure.annotation.EnableAuditEngineAutoConfiguration;
import com.bmos.autoconfigure.EnableBmosAutoConfiguration;
import com.bmos.expression.config.EnableBmosExpressionAutoConfiguration;
import com.bmos.logging.annotation.EnableBmosLogAutoConfiguration;
import com.bmos.orchestrator.engine.autoconfigure.annotation.EnableProcessEngineAutoConfiguration;
import com.bmos.platform.facade.auth.annotation.EnableBmosAuth;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
/**
 * LIMS2.0 Service Application
 *
 */
@EnableBmosAutoConfiguration
@EnableBmosExpressionAutoConfiguration
@EnableFeignClients(basePackages = {"com.bmos.lims2.*"})
@EnableDiscoveryClient
@EnableBmosAuth
@EnableBmosLogAutoConfiguration
@EnableAuditEngineAutoConfiguration
@EnableProcessEngineAutoConfiguration
@SpringBootApplication(scanBasePackages = {"com.bmos.lims2.*"})
@MapperScan(basePackages = {"com.bmos.lims2.server.**.mapper"})
public class BmosLims2Application {
    public static void main(String[] args) {
        SpringApplication.run(BmosLims2Application.class, args);
    }
}
