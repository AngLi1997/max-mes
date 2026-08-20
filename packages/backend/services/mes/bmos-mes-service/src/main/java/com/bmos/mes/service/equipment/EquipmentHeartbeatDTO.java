package com.bmos.mes.service.equipment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设备心跳DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/5/20 17:40
 */
@Data
@ApiModel("设备心跳DTO")
public class EquipmentHeartbeatDTO {

    /**
     * 设备id
     */
    @NotNull
    @ApiModelProperty(value = "设备id", required = true)
    private Long deviceId;

    /**
     * 生产批次编号
     */
    @NotBlank
    @ApiModelProperty(value = "生产批次编号", required = true)
    private String batchNo;

    /**
     * 产品名称
     */
    @NotBlank
    @ApiModelProperty(value = "产品名称", required = true)
    private String productName;
}
