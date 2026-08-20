package com.bmos.platform.facade.equipment.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备简单信息
 */
@Getter
@Setter
@ApiModel(value = "设备简单信息")
public class EquipmentEasyInfoFeignVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 设备编码
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

}
