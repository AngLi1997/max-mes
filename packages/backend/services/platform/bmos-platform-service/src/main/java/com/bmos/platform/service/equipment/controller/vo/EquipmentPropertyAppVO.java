package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 标签属性VO
 */
@Getter
@Setter
@ApiModel("app端设备属性VO")
@EqualsAndHashCode(of = {"propertyCode"})
public class EquipmentPropertyAppVO {

    /**
     * 标签属性编码
     */
    @ApiModelProperty(value = "属性编码")
    private String propertyCode;

    /**
     * 标签属性名称
     */
    @ApiModelProperty(value = "标签属性名称")
    private String name;
}
