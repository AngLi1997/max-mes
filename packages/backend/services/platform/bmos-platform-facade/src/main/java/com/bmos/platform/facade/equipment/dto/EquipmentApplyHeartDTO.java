package com.bmos.platform.facade.equipment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 设备占用心跳入参
 */
@Getter
@Setter
public class EquipmentApplyHeartDTO {

    /**
     * 设备id
     */
    @NotNull
    private Long id;

    /**
     * 生产批号
     */
    @NotNull
    private String batchNo;

    /**
     * 产品名称
     */
    @NotNull
    private String productName;
}
