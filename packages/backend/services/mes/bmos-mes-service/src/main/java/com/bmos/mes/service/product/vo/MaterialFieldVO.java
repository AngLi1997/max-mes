package com.bmos.mes.service.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义字段
 */
@Getter
@Setter
@ApiModel(value = "自定义字段")
public class MaterialFieldVO {

    /**
     * 字段
     */
    @ApiModelProperty(value = "字段Code")
    private String field;

    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    /**
     * 字段值
     */
    @ApiModelProperty(value = "字段值")
    private String fieldValue;

}
