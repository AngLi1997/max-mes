package com.bmos.platform.service.equipment.datasource.dto;

import lombok.Data;

/**
 * @author yigaohui
 * @date
 *
 * hub accessToken 获取DTO
 **/
@Data
public class HubAccessTokenRequestDTO {
    private String loginName;

    private String password;
}
