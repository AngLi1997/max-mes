package com.bmos.platform.service.message.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @className: EquipmentDefaultMessageVO
 * @author: yigaohui
 * @date: 2025/1/8 14:02
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("设备故障消息上下文")
public class EquipmentFaultMessageContext extends MessageContextDTO {
    private String equipmentName;

    private String equipmentCode;
}
