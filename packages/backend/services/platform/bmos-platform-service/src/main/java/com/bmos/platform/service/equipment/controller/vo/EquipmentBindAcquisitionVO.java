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
@ApiModel("设备数据匹配采集点VO")
public class EquipmentBindAcquisitionVO {

    /**
     * 标签属性编码
     */
    @ApiModelProperty(value = "数据属性编码")
    private String code;

    /**
     * 标签值
     */
    @ApiModelProperty(value = "标签值")
    private String value;
}
