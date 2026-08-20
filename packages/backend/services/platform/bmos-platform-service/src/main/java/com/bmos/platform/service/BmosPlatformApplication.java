package com.bmos.platform.service;

import com.bmos.autoconfigure.EnableBmosAutoConfiguration;
import com.bmos.expression.config.EnableBmosExpressionAutoConfiguration;
import com.bmos.logging.annotation.EnableBmosLogAutoConfiguration;
import com.bmos.platform.facade.auth.annotation.EnableBmosAuth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableBmosAutoConfiguration
@EnableBmosExpressionAutoConfiguration
@EnableFeignClients(basePackages = "com.bmos.platform.service")
@EnableBmosAuth
@EnableBmosLogAutoConfiguration
public class BmosPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmosPlatformApplication.class, args);
    }

}
