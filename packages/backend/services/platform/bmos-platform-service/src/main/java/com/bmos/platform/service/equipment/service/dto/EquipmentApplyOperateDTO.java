package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 设备占用操作
 */
@Getter
@Setter
@ApiModel("设备占用操作入参")
public class EquipmentApplyOperateDTO {

    @ApiModelProperty(value = "设备id", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "生产批号", required = true)
    @NotNull
    private String batchNo;

    @ApiModelProperty(value = "产品名称", required = true)
    @NotNull
    private String productName;

    @ApiModelProperty(value = "工位id", required = true)
    @NotNull
    private Long stationId;

}
