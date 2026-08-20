package com.bmos.platform.facade.equipment.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentPropertyFeignVO {

    /**
     * 标签属性编码
     */
    private String code;

    /**
     * 标签属性名称
     */
    private String name;

    /**
     * 标签值
     */
    private String value;

}
