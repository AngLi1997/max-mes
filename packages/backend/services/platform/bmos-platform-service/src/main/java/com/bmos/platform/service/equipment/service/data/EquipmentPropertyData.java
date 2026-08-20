package com.bmos.platform.service.equipment.service.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备的标签属性
 */
@Getter
@Setter
public class EquipmentPropertyData {

    /**
     * 标签属性编码
     */
    private String code;

    /**
     * 标签属性名称
     */
    private String name;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 标签值
     */
    private String value;

    /**
     * 是否内置
     */
    private Boolean embed;
}
