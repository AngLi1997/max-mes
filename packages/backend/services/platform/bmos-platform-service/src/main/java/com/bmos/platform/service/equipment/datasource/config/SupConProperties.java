package com.bmos.platform.service.equipment.datasource.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 中控的配置
 *
 * @className: SupConProperties
 * @author: yigaohui
 * @date: 2024/11/29 9:59
 * @Version: 1.0
 * @description:
 */

@Data
public class SupConProperties {

    @ApiModelProperty("mqtt 服务地址")
    private String mqttAddress;
}
