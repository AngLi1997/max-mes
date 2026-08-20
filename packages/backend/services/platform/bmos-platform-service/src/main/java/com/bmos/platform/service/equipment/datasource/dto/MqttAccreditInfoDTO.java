package com.bmos.platform.service.equipment.datasource.dto;

import lombok.Data;

/**
 * HUB MQTT授权信息
 *
 * @className: HubMqttAccreditInfoDTO
 * @author: yigaohui
 * @date: 2024/11/28 16:41
 * @Version: 1.0
 * @description:
 */

@Data
public class MqttAccreditInfoDTO {
    private String username;

    private String password;
}
