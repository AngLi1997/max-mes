package com.bmos.gateway;

import com.bmos.cache.redis.annotation.EnableBmosRedis;
import com.bmos.gateway.properties.BmosAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableBmosRedis
@EnableConfigurationProperties(BmosAuthProperties.class)
public class BmosGatewayApplication {


    public static void main(String[] args) {
        SpringApplication.run(BmosGatewayApplication.class, args);
    }
}
