package com.bmos.platform.service.factory.service.dto;

import lombok.Data;

/**
 * @className: 房间环境配置DTO
 * @author: yigaohui
 * @date: 2024/12/30 10:57
 * @Version: 1.0
 * @description:
 */

@Data
public class RoomEnvPropertyDTO {
    /**
     * 房间id
     */
    private Long roomId;

    private Long equipmentId;

    private String equipmentCode;

    private String equipmentName;
    /**
     * 设备数据编码
     */
    private String equipmentDataPropertyCode;
    private String equipmentDataPropertyName;

    private String envPropertyCode;
    private String envPropertyName;
}
