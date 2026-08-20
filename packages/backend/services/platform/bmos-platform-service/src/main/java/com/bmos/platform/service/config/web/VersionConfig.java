package com.bmos.platform.service.config.web;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource(value = "classpath:/config/version.properties",encoding = "UTF-8")
public class VersionConfig {

    @Value("${systemVersion}")
    private String systemVersion;

}
