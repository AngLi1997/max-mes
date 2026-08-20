package com.bmos.platform.service.factory.service.dto;

import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import lombok.Data;

/**
 * @className: 房间环境配置DTO
 * @author: yigaohui
 * @date: 2024/12/30 10:57
 * @Version: 1.0
 * @description:
 */

@Data
public class RoomEnvPropertyWithAcquitPointDTO extends RoomEnvPropertyDTO {
    private Long acquitPointId;

    private Long acquitPointCode;

    private String dataPointName;


    private AcquisitionPlatformEnum acquisitionPlatform;
}
