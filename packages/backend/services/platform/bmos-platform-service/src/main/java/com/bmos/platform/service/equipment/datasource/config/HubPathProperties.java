package com.bmos.platform.service.equipment.datasource.config;

import lombok.Data;

/**
 * @author yigaohui
 * @date
 *
 * HUB点位的配置信息
 **/
@Data
public class HubPathProperties {
    private String getData;

    private String writeData;

    private String getAccessToken;

    private String tagUpHis;

    private String tagId;

    private String mqttCredential;
}
