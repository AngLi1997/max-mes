package com.bmos.wms.service;

import com.bmos.autoconfigure.EnableBmosAutoConfiguration;
import com.bmos.expression.config.EnableBmosExpressionAutoConfiguration;
import com.bmos.logging.annotation.EnableBmosLogAutoConfiguration;
import com.bmos.orchestrator.engine.autoconfigure.annotation.EnableProcessEngineAutoConfiguration;
import com.bmos.platform.facade.auth.annotation.EnableBmosAuth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * WMS Service Application
 *
 */
@SpringBootApplication
@EnableBmosAutoConfiguration
@EnableProcessEngineAutoConfiguration
@EnableBmosExpressionAutoConfiguration
@EnableFeignClients(basePackages = {"com.bmos.wms","com.bmos.lims2"})
@EnableDiscoveryClient
@EnableBmosAuth
@EnableBmosLogAutoConfiguration
public class BmosWmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BmosWmsApplication.class, args);
    }
}
