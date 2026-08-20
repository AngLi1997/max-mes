package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 标签属性VO
 */
@Getter
@Setter
@ApiModel("标签属性VO")
public class EquipmentTagPropertyVO {

    /**
     * 标签属性编码
     */
    @ApiModelProperty(value = "标签属性编码")
    private String code;

    /**
     * 标签属性名称
     */
    @ApiModelProperty(value = "标签属性名称")
    private String name;


    @ApiModelProperty("类型")
    private Integer propertyType;

    /**
     * 是否必填
     */
    @ApiModelProperty(value = "是否必填")
    private Boolean required;

    /**
     * 是否内置
     */
    @ApiModelProperty(value = "是否内置")
    private Boolean embed;
}
