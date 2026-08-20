package com.bmos.platform.service.equipment.datasource.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * @author yigaohui
 * @date
 **/
@Configuration
@Data
public class HubProperties {
    private String endpoint;

    private String username;

    private String password;

    private String tenantId;

    private HubPathProperties path;
}
